package com.codepath.nytimessearch;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by evanwild on 6/20/16.
 */
public class Article implements Parcelable {
    private String webUrl;
    private String headline;
    private String thumbNail;
    private static final String PREFIX = "http://www.nytimes.com/";

    public Article(JSONObject jsonObject, String urlKey, String titleKey) {
        try {
            this.webUrl = jsonObject.getString(urlKey);
            this.headline = jsonObject.getString(titleKey);

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = PREFIX + multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Article> fromJSONArray(JSONArray array, boolean topStories) {

        ArrayList<Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                if (!topStories)
                    results.add(new Article(array.getJSONObject(x)));
                else
                    results.add(new Article(array.getJSONObject(x), "url", "title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.thumbNail);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.thumbNail = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
