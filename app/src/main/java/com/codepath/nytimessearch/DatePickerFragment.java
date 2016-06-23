package com.codepath.nytimessearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by evanwild on 6/23/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DateListener {
        void onDateSet(Calendar c, String tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar beginDate = (Calendar) getArguments().getSerializable("begin_date");
        Calendar endDate = (Calendar) getArguments().getSerializable("end_date");

        Calendar c = Calendar.getInstance();
        if (beginDate != null) {
            c = beginDate;
        } else if (endDate != null) {
            c = endDate;
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        DateListener dateListener = (DateListener) getTargetFragment();
        dateListener.onDateSet(c, getTag());
    }
}
