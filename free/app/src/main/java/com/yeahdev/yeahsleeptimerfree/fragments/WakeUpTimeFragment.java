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


public class WakeUpTimeFragment extends Fragment {
    private TextView tvChoosenTimeWUT;
    private Button btnWUT;
    private RecyclerView rvWUT;
    private RvAdapter rvAdapterWUT;
    private boolean isTimeChoosenWUT = false;
    private int hourWUT;
    private int minuteWUT;

    public WakeUpTimeFragment() {}
    public static WakeUpTimeFragment newInstance() { return new WakeUpTimeFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        rvAdapterWUT = new RvAdapter(getActivity());
        //
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("hourWUT") && savedInstanceState.containsKey("minuteWUT")) {
                calculateGoToBedTimes(savedInstanceState.getInt("hourWUT"), savedInstanceState.getInt("minuteWUT"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wake_up_time, container, false);
        //
        initComponents(view);
        setupListener();
        //
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("hourWUT") && savedInstanceState.containsKey("minuteWUT")) {
                String h = Util.convertValueToDoubleZeroString(savedInstanceState.getInt("hourWUT"));
                String m = Util.convertValueToDoubleZeroString(savedInstanceState.getInt("minuteWUT"));
                tvChoosenTimeWUT.setText(String.format("%s:%s", h, m));
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
        tvChoosenTimeWUT = (TextView) view.findViewById(R.id.tvWakeUpTimeChoosenTime);
        btnWUT = (Button) view.findViewById(R.id.btnWakeUpTime);
        //
        rvWUT = (RecyclerView) view.findViewById(R.id.rvWakeUpTime);
        rvWUT.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWUT.setItemAnimator(new DefaultItemAnimator());
        //
        rvWUT.setAdapter(rvAdapterWUT);
    }

    /**
     *
     */
    private void setupListener() {
        btnWUT.setOnClickListener(new View.OnClickListener() {
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
                        tvChoosenTimeWUT.setText(String.format("%s:%s", Util.convertValueToDoubleZeroString(hourOfDay), Util.convertValueToDoubleZeroString(minute)));
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
        this.isTimeChoosenWUT = true;
        this.hourWUT = hourOfDay;
        this.minuteWUT = minute;
        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean useFallAsleepTime = preferences.getBoolean("perform_updates_fallasleep", false);
        int userFallAsleepTime = 0;
        if (useFallAsleepTime) {
            userFallAsleepTime = Integer.parseInt(preferences.getString("updates_interval_fallasleep", "0")) / 60000;
        }
        //
        CalcSleepTime sleepTime = new CalcSleepTime(useFallAsleepTime, userFallAsleepTime);
        String[] calcedWakeUpTimes = sleepTime.wakeUpTime(hourOfDay, minute);
        //
        int[] cardColors = Util.getCardColors(getContext());
        //
        for (int i = 0; i < calcedWakeUpTimes.length; i++) {
            RvItem item = new RvItem();
            //
            item.setHeadLine((i + 1) + getResources().getString(R.string.gtst));
            item.setTimeLine(calcedWakeUpTimes[i]);
            item.setCycleLine((i + 1) + getResources().getString(R.string.sc));
            item.setCvBackground(cardColors[i]);
            item.setCvForeground(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            //
            rvAdapterWUT.addRvItem(i, item);
        }
    }

    /**
     *
     */
    private void removeGoToBedTimes() {
        if (rvAdapterWUT != null) {
            rvAdapterWUT.clear();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //
        if (isTimeChoosenWUT) {
            outState.putInt("hourWUT", hourWUT);
            outState.putInt("minuteWUT", minuteWUT);
        }
    }
}
