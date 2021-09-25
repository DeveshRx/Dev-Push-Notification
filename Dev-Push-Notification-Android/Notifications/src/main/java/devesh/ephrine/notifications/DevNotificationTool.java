package devesh.ephrine.notifications;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Random;

import devesh.ephrine.notifications.room.NotificationAppDatabase;
import devesh.ephrine.notifications.room.NotifictionData;
import devesh.ephrine.notifications.workmanager.NotiWorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


public class DevNotificationTool {
    static final private String TAG="NDM: ";
    private NotificationAppDatabase NotiDB;
   private Context mContext;
public int default_icon=R.drawable.ic_notifications_white_48dp;
    private int lib_default_icon;
    public DevNotificationTool(Context context){
        this.mContext=context;
        this.NotiDB = Room.databaseBuilder(context, NotificationAppDatabase.class, context.getString(R.string.DATABASE_NOTIFICATION))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        createNotificationChannels();
        lib_default_icon=R.drawable.ic_notifications_white_48dp;
    }

    public void NotificationCapture(RemoteMessage remoteMessage){

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String notificationType = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_TYPE));

            NotifictionData data = new NotifictionData();
            data.Title = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_TITLE));
            data.Short_Message = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_SHORT_SUMMARY));
            data.Long_Message = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_LONG_SUMMARY));
            data.icon = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_ICON));
            data.img = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_IMG));
            data.Type = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_TYPE));
            data.Url = remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_URL));
            data.id = Integer.parseInt(remoteMessage.getData().get(mContext.getString(R.string.NOTIFICATION_ID)));
             data.time = System.currentTimeMillis();

             int IconRes=default_icon;

            NormalNotification(data,IconRes);



        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

     void Save(NotifictionData data, int IconRes){
        NotiDB.notificationDAO().insertAll(data);

        NotificationShoot(data);

    }
    void NotificationShoot(NotifictionData data) {
        Intent intent = new Intent(mContext, NotificationActivity.class);
      //  intent.putExtra("flavor",AppFlavor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        int IconRes=default_icon;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.notification_channel_id_general))
               .setSmallIcon(IconRes)
                .setContentTitle(data.Title)
                .setContentText(data.Short_Message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence Channel_name = "News & Updates";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.notification_channel_id_general), Channel_name, importance);
            channel.setDescription(description);
             NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        Random rand = new Random();
        // Generate random integers in range 0 to 999
        int noti_id = rand.nextInt(999);
        notificationManager.notify(noti_id, builder.build());
    }

    void NormalNotification(NotifictionData data, int IconRes) {

        WorkRequest notiWorkRequest =
                new OneTimeWorkRequest.Builder(NotiWorkManager.class)
                        .addTag(String.valueOf(data.id))
                        .setInputData(
                                new Data.Builder()
                                        .putString("title", data.Title)
                                        .putString("short_summary", data.Short_Message)
                                        .putString("long_summary", data.Long_Message)
                                        .putString("icon", data.icon)
                                        .putString("img", data.img)
                                        .putString("type", data.Type)
                                        .putString("url", data.Url)
                                        .putString("id", String.valueOf(data.id))
                                        .putString("time", String.valueOf(data.time))
                                       // .putString("flavor", BuildConfig.FLAVOR)
                                        .putString("flavor", "default")
                                        .putInt("icontray", IconRes)
                                        .build())
                        .build();

        WorkManager
                .getInstance(mContext)
                .enqueue(notiWorkRequest);



    }

    private void createNotificationChannels() {
        //   Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
  /*          NotificationChannel basicNotificationChanel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.notification_channel_id_call),
                    NotificationManager.IMPORTANCE_DEFAULT);
*/
            NotificationChannel priorityNotificationChannel = new NotificationChannel(mContext.getString(R.string.notification_channel_id_general),
                    mContext.getString(R.string.notification_channel_id_general),
                    NotificationManager.IMPORTANCE_DEFAULT);


            NotificationManager notificationManager =mContext.getSystemService(NotificationManager.class);
            //  notificationManager.createNotificationChannel(basicNotificationChanel);
            notificationManager.createNotificationChannel(priorityNotificationChannel);
        }
    }

    public void NotificationSubscribe(String TopicName){
        if(TopicName==null){
            TopicName="general";
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TopicName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = " Notification Subscribed: SUCCESS ";
                        if (!task.isSuccessful()) {
                            msg =" Notification Subscribed: Failed ";
                        }
                        Log.d(TAG, "FCM: "+msg);
                    }
                });

    }
    public void NotificationUnSubscribe(String TopicName){
        if(TopicName==null){
            TopicName="general";
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TopicName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = " Notification UnSubscribed: SUCCESS ";
                        if (!task.isSuccessful()) {
                            msg =" Notification UnSubscribed: FAILED";
                        }
                        Log.d(TAG, "FCM: "+msg);
                    }
                });

    }


}
