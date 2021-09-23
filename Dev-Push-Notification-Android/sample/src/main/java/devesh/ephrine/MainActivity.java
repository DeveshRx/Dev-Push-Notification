package devesh.ephrine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import devesh.ephrine.notifications.NotiDataManager;
import devesh.ephrine.notifications.NotificationActivity;

public class MainActivity extends AppCompatActivity {
String TAG="APP: ";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, "FCM TOKEN: "+msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        SubscribeToTopic();





    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openNotification(View v){
        Intent intent=new Intent(this, NotificationActivity.class);

        intent.putExtra(NotificationActivity.ACTIONBAR_COLOR,getColor(R.color.teal_200));
        intent.putExtra(NotificationActivity.ACTIONBAR_ELEVATION,"0f");

        startActivity(intent);

    }

    void SubscribeToTopic(){
        NotiDataManager notiDataManager=new NotiDataManager(this);
        notiDataManager.NotificationSubscribe("general");

        // For Unsubscribe
       // notiDataManager.NotificationUnSubscribe("general");

    }

}