package com.apmato.evolveme;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by hyuntae on 17/10/16.
 */
public class StatisticsFagment extends Fragment {
    private static final String TAG = "StatisticsFragment";
    private ListView mListView;
    private TextView dateTitle;
    private StatisticsData mData;
    private CustomStatisticsAdapter mAdapter;
    private ArrayList<StatisticsData> statisticsDataList;
    static String initialWeight = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mData = new StatisticsData();
        statisticsDataList = new ArrayList<>();
        new getUrlAsyncTask(getActivity().getApplicationContext()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_statistics, parent, false);
        dateTitle = (TextView)v.findViewById(R.id.statistic_textview);
        mAdapter = new CustomStatisticsAdapter(parent.getContext());
        mListView = (ListView)v.findViewById(R.id.statistic_listview);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceSate){
        super.onViewCreated(view, savedInstanceSate);
        SimpleDateFormat format = new SimpleDateFormat("dd, MMM, yy", Locale.ENGLISH);
        String date = format.format(new Date());
        dateTitle.setText("Heute, " + date);
        dateTitle.setTextColor(Color.WHITE);
        mListView = (ListView)view.findViewById(R.id.statistic_listview);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private class getUrlAsyncTask extends AsyncTask<Void, Void, String> {
        String urlString = "http://evolveme.apmato.com:3000/api/1.0/statistics?";
        private Context context;

        private getUrlAsyncTask(Context context){this.context = context;}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            String parameters = URLEncodedUtils.format(LoginActivity.nameValuePairs, "utf-8");
            urlString += parameters;
            Log.d(TAG, "urlString:"+urlString);
        }

        @Override
        protected String doInBackground(Void... params){
            HttpClient httpClient = new DefaultHttpClient();
            String reponseString = "";

            try{
                HttpGet httpGet = new HttpGet();
                URI url = new URI(urlString);
                httpGet.setURI(url);
                HttpResponse response = httpClient.execute(httpGet);
                reponseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                Log.d(TAG, "doInBackground:" + reponseString);

            }catch (URISyntaxException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }catch (ClientProtocolException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }catch (IOException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }
            return reponseString;
        }

        @Override
        protected void onPostExecute(String response){
            try{
                parseItem(response);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
    void parseItem(String jsonStr) throws JSONException {
        String daysRemaining = "";
        String weight = "";
        String percent = "";
        String achivements = "";

        try {
            JSONObject jsonOri = new JSONObject(jsonStr);

            daysRemaining = jsonOri.getString("daysRemaining");
            initialWeight = jsonOri.getString("initialWeight");
            weight = jsonOri.getString("weight");
            percent = jsonOri.getString("percent");
            achivements = jsonOri.getString("achievements");

            for(int i = 0; i<jsonOri.length()-1; i++) {
                statisticsDataList.add(i, new StatisticsData());
            }

            statisticsDataList.get(0).setWeight(Float.valueOf(weight));
            statisticsDataList.get(1).setDaysRemaining(Integer.valueOf(daysRemaining));
            statisticsDataList.get(2).setPercentage((int)Math.round(Float.valueOf(percent)));
            statisticsDataList.get(3).setAchievements(Integer.valueOf(achivements));

            mAdapter.addItem(statisticsDataList);
            mListView.setAdapter(mAdapter);
        } catch (JSONException e) {
            Log.d(TAG, "failed to parsing");
        }

    }

    public boolean hasJsonErr(String jsonStr){
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            if(jsonObject.has("error")){
                return true;
            }else{
                return false;
            }
        }catch (JSONException e){
            Log.d(TAG, "fail to know boolean value");
            e.printStackTrace();
        }
        return true;
    }

}
