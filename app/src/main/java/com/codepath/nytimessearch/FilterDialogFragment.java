package com.codepath.nytimessearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.ButterKnife;

public class FilterDialogFragment extends DialogFragment implements DatePickerFragment.DateListener {

    private EditText mEditText;
    private Query query;
    private Spinner spNewsDesk;
    private Spinner spOrder;
    private Calendar begin_date;
    private Calendar end_date;


    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface FilterListener {
        void onFinishFilter(Query query);
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public static FilterDialogFragment newInstance(Query query) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("query", query);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        super.onViewCreated(view, savedInstanceState);
        query = getArguments().getParcelable("query");
        begin_date = query.getBegin_date();
        end_date = query.getEnd_date();
        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        spNewsDesk = (Spinner) view.findViewById(R.id.spNewsDesk);
        String news_desk = query.getNews_desk();
        if (news_desk != null) {
            for (int i = 0; i < spNewsDesk.getCount(); i++) {
                if (news_desk.equals(spNewsDesk.getItemAtPosition(i).toString())) {
                    spNewsDesk.setSelection(i);
                }
            }
        }

        spOrder = (Spinner) view.findViewById(R.id.spOrder);
        String sort = query.getSort();
        if (sort != null) {
            for (int i = 0; i < spOrder.getCount(); i++) {
                if (news_desk.equals(spOrder.getItemAtPosition(i).toString())) {
                    spOrder.setSelection(i);
                }
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });
        Button btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClear(v);
            }
        });

        Button btnEditBegin = (Button) view.findViewById(R.id.btnEditBegin);
        Button btnEditEnd = (Button) view.findViewById(R.id.btnEditEnd);
        btnEditBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditBeginDate(v);
            }
        });
        btnEditEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditEndDate(v);
            }
        });
    }

    public void onEditBeginDate(View view) {
        FragmentManager fm = getFragmentManager();
        DialogFragment df = new DatePickerFragment();
        df.setTargetFragment(FilterDialogFragment.this, 300);
        Bundle args = new Bundle();
        args.putSerializable("begin_date", begin_date);
        df.setArguments(args);
        df.show(fm, "begin_date");
    }

    public void onEditEndDate(View view) {
        FragmentManager fm = getFragmentManager();
        DialogFragment df = new DatePickerFragment();
        df.setTargetFragment(FilterDialogFragment.this, 300);
        Bundle args = new Bundle();
        args.putSerializable("end_date", end_date);
        df.setArguments(args);
        df.show(fm, "end_date");
    }

    public void onSubmit(View view) {
        String news_desk = spNewsDesk.getSelectedItem().toString();
        String sort = spOrder.getSelectedItem().toString();
        query = new Query(query, news_desk, begin_date, end_date, sort);
        FilterListener listener = (FilterListener) getActivity();
        listener.onFinishFilter(query);
        dismiss();
    }

    public void onClear(View view) {
        query = new Query(query, null, null, null, null);
        FilterListener listener = (FilterListener) getActivity();
        listener.onFinishFilter(query);
        dismiss();
    }

    @Override
    public void onDateSet(Calendar c, String tag) {
        if(tag.equals("begin_date")) {
            //Toast.makeText(getActivity(), "setting begin date!", Toast.LENGTH_SHORT).show();
            begin_date = c;
        } else if (tag.equals("end_date")) {
            //Toast.makeText(getActivity(), "setting end date!", Toast.LENGTH_SHORT).show();
            end_date = c;
        }
    }


}