package com.apmato.evolveme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by hyuntae on 30/08/16.
 */

public class UserProfile {
    static private UserProfile singleton;

    private boolean isAuthenticated;
    private HashMap<String, String> userValueSet = new HashMap<String, String>();
    private Bundle userSetBundleInput = new Bundle();
    private Bundle userSetBundleOutput = new Bundle();
    StringBuilder json = new StringBuilder();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static Context context ;

    static public UserProfile sharedInstance(Context c) {
        context = c;
        if (singleton == null) singleton = new UserProfile();
        return singleton;
    }

    private UserProfile() {
        isAuthenticated = false;
    }

    public void logout(String key) {
        // clear user data
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.remove(key);

        userSetBundleInput = null;
        userSetBundleOutput = null;
        userValueSet = null;
        // set authenticated to false
        this.isAuthenticated = false;
    }

    public String getLogin() {
        if (this.isAuthenticated) {
            return loadJsonUserValues();
        } else {
            return null;
        }
    }

    public void setLogin() { //set the jsonStr in the sharedpreferece.

        String values = storeUserValuestoJsonStr();
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("ProfileLogin", values).commit();
        json.delete(0, json.toString().length());
        Log.d("Userprofile", pref.getString("ProfileLogin", "null"));
    }
    public <T> void update(String key, T value){
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        String oriShared = pref.getString("ProfileLogin", "");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject = new JSONObject(oriShared);
            jsonObject.put(key, String.valueOf(value));
            String jsonStr = jsonObject.toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("ProfileLogin");
            editor.putString("ProfileLogin", jsonStr);
            editor.commit();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setStringValue(String key, String displayNameValue) {
        userSetBundleInput.putString(key, displayNameValue);
    }

    public String getStringValue(String key) {//todo check either authentication of class or authentication of bundle
        if (this.isAuthenticated) {
            Bundle bundle = loadBundleUserValues();
            return bundle.getString(key);
        } else {
            return "not logged in";
        }
    }


    public boolean isAuthenticated() { //todo decide which authentication you are going to use
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        return pref.getBoolean("Authentication", false);
    }

    protected void setIsAuthenticated(boolean isAuthenticated) {//todo decide which authentication you are going to use
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("Authentication", isAuthenticated).commit();//or .apply();
        this.isAuthenticated = true;
    }

    private void setDateValue(String key, Date date) {
        Date dateOfMarriage = date;
        Date currentDate = new Date();
        long diffSec = (currentDate.getTime()) - (dateOfMarriage.getTime());
        long diffDay = diffSec / (60 * 60 * 24);
        long restOfTimeLongForm = DateUtils.DAY_IN_MILLIS * diffDay;
        userSetBundleInput.putLong(key, restOfTimeLongForm);
    }

    public Date getDateValue(String key) {//todo check either authentication of class or authentication of bundle
        Bundle bundle = loadBundleUserValues();
        String restOfTimeStrForm = bundle.getString(key);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return format.parse(restOfTimeStrForm);
        } catch (ParseException e) {
            Log.d("date parsing", "failed");
        }
        return null;
    }

    //getRestTime test required.
    public long getRestTime(String key) {
        Bundle bundle = loadBundleUserValues();
        String deadline = bundle.getString(key);
        long diffSec = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date deadlineDateForm = format.parse(deadline);
            Date currentDate = new Date();
            diffSec = deadlineDateForm.getTime() - currentDate.getTime();
        } catch (ParseException e) {
            Log.d("date parsing", "failed");
        }
        return diffSec;
    }

    private void setLongValue(String key, long longValue) {
        userSetBundleInput.putLong(key, longValue);
    }

    public long getLongValue(String key) {
        Bundle bundle = loadBundleUserValues();
        return bundle.getLong(key);
    }

    private void setFloatValue(String key, float floatValue) {
        userSetBundleInput.putFloat(key, floatValue);
    }

    public float getFloatValue(String key) {
        Bundle bundle = loadBundleUserValues();
        return bundle.getFloat(key);
    }



    /*
        About Context.MODE_MULTI_PROCESS
        * This was the legacy (but undocumented) behavior in and before Gingerbread (Android 2.3) and
        * this flag is implied when targetting such releases.
        * For applications targetting SDK versions greater than Android 2.3,
        * this flag must be explicitly set if desired.
        * */
    //todo make it called just once from login or whatever
    public Bundle loadBundleUserValues() {

        String jsonStr = loadJsonUserValues();
//        Log.d("loadBundle", jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyItr;
            keyItr = jsonObject.keys();
            while (keyItr.hasNext()) {
                String key = keyItr.next();
                if (jsonObject.get(key) instanceof Boolean) {
                    userSetBundleOutput.putBoolean(key, jsonObject.getBoolean(key));
                } else if (jsonObject.get(key) instanceof Long) {
                    userSetBundleOutput.putLong(key, jsonObject.getLong(key));
                } else if (jsonObject.get(key) instanceof Integer){
                    userSetBundleOutput.putInt(key, jsonObject.getInt(key));
                } else {
                    if(jsonObject.get(key).toString().contains(".") && !Pattern.compile("[a-zA-Z]").matcher(jsonObject.get(key).toString()).find()){
                        userSetBundleOutput.putFloat(key, Float.parseFloat(jsonObject.getString(key)));
                    }else {
                        userSetBundleOutput.putString(key, jsonObject.getString(key));
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("UserProfile", "JsonParsing failed");
        }
        return userSetBundleOutput;
    }

    public String loadJsonUserValues() {
        pref = context.getSharedPreferences("ProfileLogin", Context.MODE_PRIVATE);
        String jsonStr = pref.getString("ProfileLogin", null);
        return jsonStr;
    }

    private String storeUserValuestoJsonStr() { //Must call this method After all the values are set in the coupleinfodata.
        for (String str : userSetBundleInput.keySet()) {
            userValueSet.put(str, userSetBundleInput.getString(str));
        }
        JSONObject jsonObject = new JSONObject(userValueSet);
        json.append(jsonObject.toString());
        return json.toString();
    }

    public <T> void storeUserValue(String key, T value) {
        if (value instanceof Boolean) {
            setIsAuthenticated((Boolean) value);
        } else if (value instanceof Long) {
            setLongValue(key, (Long) value);
        } else if (value instanceof Date) {
            setDateValue(key, (Date) value);
        } else if (value instanceof Float) {
            setFloatValue(key, (Float) value);
        } else if (value instanceof String) {
            setStringValue(key, (String) value);
        }
        Log.d("UserProfile", userSetBundleInput.toString());
    }

}
