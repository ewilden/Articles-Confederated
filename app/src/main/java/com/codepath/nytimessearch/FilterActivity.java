package com.codepath.nytimessearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.spNewsDesk)  Spinner spNewsDesk;
    @BindView(R.id.spOrder)     Spinner spOrder;
    @BindView(R.id.dpBegin)     DatePicker dpBegin;
    @BindView(R.id.dpEnd)       DatePicker dpEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
    }

    public void onSubmit(View view) {
        String news_desk = spNewsDesk.getSelectedItem().toString();
        String sort = spOrder.getSelectedItem().toString();
        Calendar begin_date = new GregorianCalendar(dpBegin.getYear(), dpBegin.getMonth(), dpBegin.getDayOfMonth());
        Calendar end_date = new GregorianCalendar(dpEnd.getYear(), dpEnd.getMonth(), dpEnd.getDayOfMonth());
        Query query = getIntent().getParcelableExtra("query");
        if (news_desk.equals("All")) {
            news_desk = null;
        }
        Query q = new Query(query, news_desk, begin_date, end_date, sort);

        Intent i = new Intent();
        i.putExtra("query", q);

        setResult(RESULT_OK, i);
        finish();
    }
}
