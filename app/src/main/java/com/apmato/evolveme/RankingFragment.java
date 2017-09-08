package com.apmato.evolveme;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.Locale;

/**
 * Created by hyuntae on 19/10/16.
 */
public class RankingFragment extends Fragment {
    public String TAG = "DailyRecordFragment";
    private ListView mListView;
    public RankingData rankingData;
    private CustomRankingAdapter mAdapter;
    private ArrayList<RankingData> rankingDataList;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rankingData = new RankingData();
        rankingDataList = new ArrayList<>();
        new getUrlAsyncTask(getActivity().getApplicationContext()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_ranking, parent, false);

        mAdapter = new CustomRankingAdapter(parent.getContext());
        mListView = (ListView)v.findViewById(R.id.ranking_listView);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.ranking_listView);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private class getUrlAsyncTask extends AsyncTask<Void, Void, String> {
        String urlString = "http://evolveme.apmato.com:3000/api/1.0/ranking?";
        private Context context;

        private getUrlAsyncTask(Context context){this.context = context;}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            String parameters = URLEncodedUtils.format(LoginActivity.nameValuePairs, "utf-8");
            urlString += parameters;
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

        String name;
        String percent;
        boolean verifiedByReferee;
        boolean isSelf;

        try {
            JSONObject jsonOri = new JSONObject(jsonStr);
            jsonStr = jsonOri.get("ranking").toString();
            JSONArray jsonArray = new JSONArray(jsonStr);//everthing is in array 1... ??
            Log.d(TAG, String.valueOf(jsonArray.length()));

            for(int i = 0; i< jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d(TAG, String.valueOf(i));
                name = jsonObject.getString("name");
                percent = jsonObject.getString("percent");
                verifiedByReferee = jsonObject.getBoolean("verifiedByReferee");
                isSelf = jsonObject.getBoolean("isSelf");

                rankingDataList.add(i, new RankingData());
                rankingDataList.get(i).setName(name);
                rankingDataList.get(i).setPercent(Float.parseFloat(percent));
                rankingDataList.get(i).setVerifiedByReferee(verifiedByReferee);
                rankingDataList.get(i).setIsSelf(isSelf);
            }
            mAdapter.addItem(rankingDataList);
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
