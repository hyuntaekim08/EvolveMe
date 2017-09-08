package com.apmato.evolveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hyuntae on 01/09/16.
 */
public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME_HOUR = "com.apmato.evolveme.timehour";
    public static final String EXTRA_TIME_MINUTE = "com.apmato.evolveme.timeminutes";
    private int mTimeHour = Calendar.getInstance().get(Calendar.HOUR);
    private int mTimeMinute = Calendar.getInstance().get(Calendar.MINUTE);

    public static TimePickerFragment newInstance(int hour, int minute){
        Bundle args = new Bundle();
        args.putInt(EXTRA_TIME_HOUR, hour);
        args.putInt(EXTRA_TIME_MINUTE, minute);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME_HOUR, mTimeHour);
        intent.putExtra(EXTRA_TIME_MINUTE, mTimeMinute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle bundle =getArguments();

        mTimeHour = bundle.getInt(EXTRA_TIME_HOUR);
        mTimeMinute = bundle.getInt(EXTRA_TIME_MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                mTimeHour = hourOfDay;
                mTimeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(EXTRA_TIME_HOUR, mTimeHour);
                b.putInt(EXTRA_TIME_MINUTE, mTimeMinute);
                sendResult(Activity.RESULT_OK);
            }
        });

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                mTimeHour = hourOfDay;
                mTimeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(EXTRA_TIME_HOUR, mTimeHour);
                b.putInt(EXTRA_TIME_MINUTE, mTimeMinute);
                sendResult(Activity.RESULT_OK);
            }
        };
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Upload Time")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();
    }
}
