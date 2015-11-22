package com.yeahdev.yeahsleeptimerfree.adapter;

import android.app.Activity;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.model.RvItem;

import java.util.ArrayList;
import java.util.List;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ItemViewHolder> {

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView cvItem;
        private TextView tvItemHeadLine;
        private TextView tvItemTimeLine;
        private TextView tvItemCycleLine;

        public ItemViewHolder(View view) {
            super(view);
            this.cvItem = (CardView) view.findViewById(R.id.cvItem);
            this.tvItemHeadLine = (TextView) view.findViewById(R.id.tvItemHeadLine);
            this.tvItemTimeLine = (TextView) view.findViewById(R.id.tvItemTimeLine);
            this.tvItemCycleLine = (TextView) view.findViewById(R.id.tvItemCycleLine);
        }
    }

    private Activity activity;
    private List<RvItem> itemList;

    public RvAdapter(final Activity activity) {
        this.activity = activity;
        this.itemList = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final RvItem item = this.itemList.get(position);
        holder.tvItemHeadLine.setText(item.getHeadLine());
        holder.tvItemHeadLine.setTextColor(item.getCvForeground());
        holder.tvItemTimeLine.setText(item.getTimeLine());
        holder.tvItemTimeLine.setTextColor(item.getCvForeground());
        holder.tvItemCycleLine.setText(item.getCycleLine());
        holder.tvItemCycleLine.setTextColor(item.getCvForeground());
        holder.cvItem.setBackgroundColor(item.getCvBackground());
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeParts = item.getTimeLine().split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                //
                Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                //
                openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, hour);
                openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                openNewAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, "Yeah! Sleep Timer");
                //
                activity.startActivity(openNewAlarm);
            }
        });
    }

    public void addRvItem(int position, RvItem rvItem) {
        this.itemList.add(position, rvItem);
        notifyItemInserted(position);
    }

    public void clear() {
        this.itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
