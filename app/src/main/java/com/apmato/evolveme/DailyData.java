package com.apmato.evolveme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hyuntae on 01/09/16.
 */
public class DailyData {
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    float weight;
    Date mDate = null;
    int mHour;
    int mMin;
    Bitmap mPhoto;
    boolean verifiedByReferee;

    public DailyData(){
        mHour = 0;
        mMin = 0;
        mPhoto = null;
    }
    public DailyData(JSONObject json) throws JSONException{
        mDate = new Date(json.getLong(JSON_DATE));
        /*if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }*/
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_DATE, mDate.getTime());
        /*
        if(mPhoto != null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }*/
        return json;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date date){

        this.mDate = date;
    }

    public int getHour(){
        return mHour;
    }

    public void setHour(int hour){
        this.mHour = hour;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        this.mMin = min;
    }

    public Bitmap getPhoto() {
        return mPhoto;
    }

    public void setPhoto(File photoid) {
        if(photoid == null){
            mPhoto = BitmapFactory.decodeResource(new DailyRecordFragment().getResources(), R.drawable.defaultweightimage);
        }
        mPhoto = BitmapFactory.decodeFile(photoid.getAbsolutePath());
    }

    public boolean isVerifiedByReferee() {
        return verifiedByReferee;
    }

    public void setVerifiedByReferee(boolean verifiedByReferee) {
        this.verifiedByReferee = verifiedByReferee;
    }
}
