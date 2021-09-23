package devesh.ephrine.notifications;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import devesh.ephrine.notifications.recycleview.NotiAdapter;
import devesh.ephrine.notifications.room.NotificationAppDatabase;
import devesh.ephrine.notifications.room.NotifictionData;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotificationActivity extends AppCompatActivity {
    public static final String ACTIONBAR_ELEVATION = "actionbar_elevation";
    public static final String ACTIONBAR_COLOR = "actionbar_color";
  //  public static final String ACTIONBAR_TEXT_COLOR = "actionbar_text_color";

    NotificationAppDatabase NotiDB;
    RecyclerView recycleView;
    List<NotifictionData> nData;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    String TAG = "Notification Act:";
    NotiAdapter mAdapter;
    String AppFlavor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // TODO: intent data from notification click
        /*try {
            Bundle b = getIntent().getExtras();// add these lines of code to get data from notification
            String someData = b.getString(getString(R.string.NOTIFICATION_OPEN_ACTIVITY_INTENT_NAME));
        } catch (Exception e) {
            Log.e(TAG, "onCreate: NULL INTENT" + e);
        }*/


// TODO: Custom Theme Colors
    /*    try {
            if (getIntent().getStringExtra(ACTIONBAR_ELEVATION) != null) {
                String elvSTR = getIntent().getStringExtra(ACTIONBAR_ELEVATION);
                float elv = Float.valueOf(elvSTR); //0f
                getSupportActionBar().setElevation(elv);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ACTIONBAR_ELEVATION ERROR " + e);
        }


        try {
            if (getIntent().getStringExtra(ACTIONBAR_COLOR) != null) {
                String colorSTR = getIntent().getStringExtra(ACTIONBAR_COLOR);
                int ac_color = Integer.parseInt(colorSTR);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac_color));
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ACTIONBAR_COLOR " + e);
        }
*/


      //  Intent intent = getIntent();
      //  AppFlavor = intent.getStringExtra("flavor");

        recycleView = findViewById(R.id.recycleview_notification);

        NotiDB = Room.databaseBuilder(this, NotificationAppDatabase.class, getString(R.string.DATABASE_NOTIFICATION))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        nData = NotiDB.notificationDAO().getAllbyTime();


        recycleView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        mAdapter = new NotiAdapter(this, nData);
        recycleView.setAdapter(mAdapter);

        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.d(TAG, "onMove: ");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                final int position = viewHolder.getAdapterPosition();
                Log.d(TAG, "onSwiped: " + position);

                NotifictionData nd = mAdapter.getItemData(position);
                mAdapter.delete(position);
                NotiDB.notificationDAO().delete(nd);
                //mAdapter.notifyItemChanged(position);
                /*
                  switch (direction) {
                    case ItemTouchHelper.LEFT:
                        //Profile
                        NotiDB.notificationDAO().delete(nd);
                        break;

                    case ItemTouchHelper.RIGHT:
                        // Call
                        NotiDB.notificationDAO().delete(nd);
                        break;
                }
                */

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                Log.d(TAG, "onChildDraw: dX:" + dX + " dY:" + dY);
                //  final View foregroundView = ((MyContactAdapter.MyViewHolder) viewHolder).viewForeground;

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        //   .addSwipeLeftBackgroundColor(ContextCompat.getColor(NotificationActivity.this, R.color.design_default_color_on_primary))
                        //   .addSwipeLeftActionIcon(R.drawable.ic_baseline_account_circle_30)
                        //.addSwipeLeftLabel("Profile")
                        // .setSwipeLeftLabelColor(R.color.white)
                        //  .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Call_Green))
                        //  .addSwipeRightActionIcon(R.drawable.ic_baseline_videocam_30)
                        //.addSwipeRightLabel("Video Call")
                        // .setSwipeRightLabelColor(R.color.white)
                        //.setSwipeLeftLabelTextSize(25,TypedValue.COMPLEX_UNIT_SP)
                        //.setActionIconTint(R.color.white)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };


        ItemTouchHelper ith = new ItemTouchHelper(itemTouchHelperCallback);
        ith.attachToRecyclerView(recycleView);


    }

    public void openWeb(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }


}

/*

https://firebasestorage.googleapis.com/v0/b/ephrinelab.appspot.com/o/public%2Fapp.apk?alt=media&token=35f6ace6-b20d-4b35-859e-8663b2e86711

  SpannableString FormattedText = new SpannableString(Html.fromHtml(ProductDesc));

        // ProductDescTextView.setText(ProductDesc);
        ProductDescTextView.setText(FormattedText, TextView.BufferType.SPANNABLE);


 */