package devesh.ephrine.notifications.recycleview;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import devesh.ephrine.notifications.NotificationActivity;
import devesh.ephrine.notifications.R;
import devesh.ephrine.notifications.room.NotifictionData;
import devesh.ephrine.notifications.utils.URLSpanNoUnderline;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewHolder> {

    private static final Random random = new Random();
    private final List<NotifictionData> mDataset;
    public String TAG = "NotiAdapter";
    public Context mContext;

    public NotiAdapter(Context Context, List<NotifictionData> myDataset) {
        mDataset = myDataset;
        mContext = Context;
        Glide.get(mContext).clearMemory();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item, parent, false);
        // Give the view as it is
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.nTitle.setText(mDataset.get(position).Title);

        Spannable s = (Spannable) Html.fromHtml(mDataset.get(position).Long_Message);
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }

        holder.nLongSummary.setText(s);
        holder.nLongSummary.setMovementMethod(LinkMovementMethod.getInstance());

        if (mDataset.get(position).Url.contains("http") || mDataset.get(position).Url.contains("https")) {
            holder.CardItem.setTag(mDataset.get(position).Url);
            holder.CardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //view.getTag();
                    Log.d(TAG, "onClick: " + view.getTag());
                    if (view.getTag() != null) {
                        String tag = view.getTag().toString();
                        if (tag.contains("http") || tag.contains("https")) {
                            if (mContext instanceof NotificationActivity) {
                                ((NotificationActivity) mContext).openWeb(tag);
                            }
                        }
                    }
                }
            });


        }


        Log.d(TAG, "onBindViewHolder: " + mDataset.get(position).icon);
        if (mDataset.get(position).icon.contains("http") || mDataset.get(position).icon.contains("https")) {
            Log.d(TAG, "onBindViewHolder: img ICON");
            Glide.with(mContext).load(mDataset.get(position).icon).into(holder.nIcon);
        } else {
            holder.nIcon.setVisibility(View.GONE);
        }

        Log.d(TAG, "onBindViewHolder: " + mDataset.get(position).img);
        if (mDataset.get(position).img.contains("http") || mDataset.get(position).img.contains("https")) {
            Log.d(TAG, "onBindViewHolder: img THUMB ");
            Glide.with(mContext).load(mDataset.get(position).img).into(holder.thumbImg);
        } else {
            holder.thumbImg.setVisibility(View.GONE);
        }


    }

    public NotifictionData getItemData(int pos) {
        if (mDataset.get(pos) != null) {
            return mDataset.get(pos);
        } else {
            return null;
        }

    }

    public void delete(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

     public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nTitle;
        public TextView nLongSummary;
        public CardView CardItem;
        public ImageView nIcon;
        public ImageView thumbImg;

        public MyViewHolder(View v) {
            super(v);
            nTitle = v.findViewById(R.id.nTitle);
            nLongSummary = v.findViewById(R.id.nLongSummary);
            CardItem = v.findViewById(R.id.CardItem);
            nIcon = v.findViewById(R.id.nIcon);
            thumbImg = v.findViewById(R.id.thumbImg);
        }
    }


}
