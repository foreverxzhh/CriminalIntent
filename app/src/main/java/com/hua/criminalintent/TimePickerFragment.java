package com.hua.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "time";
    private static final String EXTRA_TIME = "com.hua.criminalintent.time";

    private TimePicker mTimePicker;
    private Date mDate;

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        mDate = (Date) getArguments().getSerializable(ARG_DATE);
        int hour = mDate.getHours();
        int minute = mDate.getMinutes();
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity()).setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getCurrentHour();
                        int minute = mTimePicker.getCurrentMinute();
                        mDate.setHours(hour);
                        mDate.setMinutes(minute);

                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_TIME, mDate);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .setTitle(R.string.time_picker_title)
                .create();
    }
}
