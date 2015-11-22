package com.yeahdev.yeahsleeptimerfree.fragments;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.adapter.RvAdapter;
import com.yeahdev.yeahsleeptimerfree.helper.CalcSleepTime;
import com.yeahdev.yeahsleeptimerfree.helper.Util;
import com.yeahdev.yeahsleeptimerfree.model.RvItem;

import java.util.Calendar;


public class GoToBedFragment extends Fragment {
    private TextView tvChoosenTime;
    private Button btnGoGTB;
    private RecyclerView rvGoToBed;
    private RvAdapter rvAdapter;
    private boolean isTimeChoosen = false;
    private int hour;
    private int minute;

    public GoToBedFragment() {}
    public static GoToBedFragment newInstance() { return new GoToBedFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        rvAdapter = new RvAdapter(getActivity());
        //
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("hour") && savedInstanceState.containsKey("minute")) {
                calculateGoToBedTimes(savedInstanceState.getInt("hour"), savedInstanceState.getInt("minute"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_to_bed, container, false);
        //
        initComponents(view);
        setupListener();
        //
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("hour") && savedInstanceState.containsKey("minute")) {
                String h = Util.convertValueToDoubleZeroString(savedInstanceState.getInt("hour"));
                String m = Util.convertValueToDoubleZeroString(savedInstanceState.getInt("minute"));
                tvChoosenTime.setText(String.format("%s:%s", h, m));
            }
        }
        //
        return view;
    }

    /**
     *
     * @param view
     */
    private void initComponents(View view) {
        tvChoosenTime = (TextView) view.findViewById(R.id.tvGoToBedChoosenTime);
        btnGoGTB = (Button) view.findViewById(R.id.btnGoGTB);
        //
        rvGoToBed = (RecyclerView) view.findViewById(R.id.rvGoToBed);
        rvGoToBed.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGoToBed.setItemAnimator(new DefaultItemAnimator());
        //
        rvGoToBed.setAdapter(rvAdapter);
    }

    /**
     *
     */
    private void setupListener() {
        btnGoGTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                //
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //
                        tvChoosenTime.setText(String.format("%s:%s", Util.convertValueToDoubleZeroString(hourOfDay), Util.convertValueToDoubleZeroString(minute)));
                        calculateGoToBedTimes(hourOfDay, minute);
                    }
                }, hour, minute, true);
                //
                timePickerDialog.setTitle(getResources().getString(R.string.btnSelectTime));
                timePickerDialog.show();
            }
        });
    }

    /**
     *
     * @param hourOfDay
     * @param minute
     */
    private void calculateGoToBedTimes(int hourOfDay, int minute) {
        removeGoToBedTimes();
        //
        this.isTimeChoosen = true;
        this.hour = hourOfDay;
        this.minute = minute;
        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean useFallAsleepTime = preferences.getBoolean("perform_updates_fallasleep", false);
        int userFallAsleepTime = 0;
        if (useFallAsleepTime) {
            userFallAsleepTime = Integer.parseInt(preferences.getString("updates_interval_fallasleep", "0")) / 60000;
        }
        //
        CalcSleepTime sleepTime = new CalcSleepTime(useFallAsleepTime, userFallAsleepTime);
        String[] calcedGoToBedTimes = sleepTime.goToSleepTime(hourOfDay, minute);
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
            rvAdapter.addRvItem(i, item);
        }
    }

    /**
     *
     */
    private void removeGoToBedTimes() {
        if (rvAdapter != null) {
            rvAdapter.clear();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //
        if (isTimeChoosen) {
            outState.putInt("hour", hour);
            outState.putInt("minute", minute);
        }
    }
}
