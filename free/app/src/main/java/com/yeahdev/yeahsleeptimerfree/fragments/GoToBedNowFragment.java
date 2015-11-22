package com.yeahdev.yeahsleeptimerfree.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.adapter.RvAdapter;
import com.yeahdev.yeahsleeptimerfree.helper.CalcSleepTime;
import com.yeahdev.yeahsleeptimerfree.helper.Util;
import com.yeahdev.yeahsleeptimerfree.model.RvItem;

import java.util.Calendar;


public class GoToBedNowFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvGoToBedNow;
    private RvAdapter rvAdapterNow;

    public GoToBedNowFragment() {}
    public static GoToBedNowFragment newInstance() { return new GoToBedNowFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        rvAdapterNow = new RvAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_to_bed_now, container, false);

        initComponents(view);
        setupSwipeContainer();
        removeGoToBedTimes();
        getGoToBedTimes();

        return view;
    }

    private void initComponents(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerGoToBedNow);
        //
        rvGoToBedNow = (RecyclerView) view.findViewById(R.id.rvGoToBedNow);
        rvGoToBedNow.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGoToBedNow.setItemAnimator(new DefaultItemAnimator());
        //
        rvGoToBedNow.setAdapter(rvAdapterNow);
    }

    private void setupSwipeContainer() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGoToBedTimes();
            }
        });
    }

    private void getGoToBedTimes() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean useFallAsleepTime = preferences.getBoolean("perform_updates_fallasleep", false);
        int userFallAsleepTime = 0;
        if (useFallAsleepTime) {
            userFallAsleepTime = Integer.parseInt(preferences.getString("updates_interval_fallasleep", "0")) / 60000;
        }
        //
        CalcSleepTime sleepTime = new CalcSleepTime(useFallAsleepTime, userFallAsleepTime);
        String[] calcedGoToBedTimes = sleepTime.goToSleepTime(hour, minute);
        //
        int[] cardColors = Util.getCardColors(getContext());
        //
        for (int i = 0; i < calcedGoToBedTimes.length; i++) {
            RvItem item = new RvItem();
            //
            item.setHeadLine((i + 1) + getResources().getString(R.string.wut));
            item.setTimeLine(calcedGoToBedTimes[i]);
            item.setCycleLine((i + 1) + getResources().getString(R.string.sc));
            item.setCvBackground(cardColors[i]);
            item.setCvForeground(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            //
            rvAdapterNow.addRvItem(i, item);
        }
    }

    private void removeGoToBedTimes() {
        if (rvAdapterNow != null) {
            rvAdapterNow.clear();
        }
    }

    private void refreshGoToBedTimes() {
        removeGoToBedTimes();
        getGoToBedTimes();
        refreshLayout.setRefreshing(false);
    }
}
