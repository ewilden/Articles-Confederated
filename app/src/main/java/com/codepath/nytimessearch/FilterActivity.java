package com.codepath.nytimessearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Date;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void onSubmit(View view) {
        Intent i = new Intent();
        Spinner spNewsdesk = (Spinner) findViewById(R.id.spNewsdesk);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Spinner spSort = (Spinner) findViewById(R.id.spSort);
        String newsDesk = spNewsdesk.getSelectedItem().toString();
        String sort = spSort.getSelectedItem().toString();

        if (newsDesk != "All")
            i.putExtra("news_desk", newsDesk);
        i.putExtra("sort", sort);

        String date;
        try {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            String monthStr = Integer.toString(month);
            String dayStr = Integer.toString(day);
            if (monthStr.length() < 2) monthStr = "0"+monthStr;
            if (dayStr.length() < 2) dayStr = "0"+dayStr;
            date = Integer.toString(year) + monthStr + dayStr;

        } catch(NullPointerException e) {
            date = null;
        }

        i.putExtra("date", date);

        setResult(RESULT_OK, i);
        finish();
    }
}
