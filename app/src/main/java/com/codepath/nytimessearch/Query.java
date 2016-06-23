package com.codepath.nytimessearch;

import android.app.DownloadManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by evanwild on 6/22/16.
 */
public class Query implements Parcelable {
    private String q;
    private String news_desk;
    private Calendar begin_date;
    private Calendar end_date;
    private String sort;
    private int page;

    public int getPage() {
        return page;
    }

    public Query(String q, String news_desk, Calendar begin_date, Calendar end_date, String sort, int page) {
        this.q = q;
        this.news_desk = news_desk;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.sort = sort;
        this.page = page;
    }

    public Query(String q) {
        this.q = q;
        this.news_desk = null;
        this.begin_date = null;
        this.end_date = null;
        this.sort = null;
        this.page = 0;
    }

    public Query(Query query, String q) {
        this.q = q;
        this.news_desk = query.news_desk;
        this.begin_date = query.begin_date;
        this.end_date = query.end_date;
        this.sort = query.sort;
        this.page = 0;
    }

    // Returns the same query with the given page instead of the original.
    public Query(Query query, int page) {
        this.q = query.q;
        this.news_desk = query.news_desk;
        this.begin_date = query.begin_date;
        this.end_date = query.end_date;
        this.sort = query.sort;
        this.page = page;
    }

    public Query(Query query, String news_desk, Calendar begin_date, Calendar end_date, String sort) {
        this.q = query.q;
        this.news_desk = news_desk;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.sort = sort;
    }

    public RequestParams getParams(String apiKey) {

        // initialize parameters holder
        RequestParams params = new RequestParams();
        // add API key
        params.put("api-key", apiKey);
        params.put("q", q);
        // set up date formatter
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        if (begin_date != null) {
            params.put("begin_date", format.format(begin_date.getTime()));
        }
        if (end_date != null) {
            params.put("end_date", format.format(end_date.getTime()));
        }
        if (news_desk != null) {
            params.put("fq", "news_desk:\""+news_desk+"\"");
        }
        if (sort != null) {
            params.put("sort", sort);
        }
        params.put("page", page);

        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.q);
        dest.writeString(this.news_desk);
        dest.writeSerializable(this.begin_date);
        dest.writeSerializable(this.end_date);
        dest.writeString(this.sort);
        dest.writeInt(this.page);
    }

    protected Query(Parcel in) {
        this.q = in.readString();
        this.news_desk = in.readString();
        this.begin_date = (Calendar) in.readSerializable();
        this.end_date = (Calendar) in.readSerializable();
        this.sort = in.readString();
        this.page = in.readInt();
    }

    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel source) {
            return new Query(source);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };
}
