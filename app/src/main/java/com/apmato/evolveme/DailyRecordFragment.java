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
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by hyuntae on 12/09/16.
 */
public class DailyRecordFragment extends Fragment {
    public String TAG = "DailyRecordFragment";
    private ListView mListView;
    public DailyData dailyData;
    private CustomDailyAdapter mAdapter;
    private ArrayList<DailyData> dailyDataList;
    public List<BasicNameValuePair> dailyNamePair;
    UserProfile profile;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        profile = UserProfile.sharedInstance(getActivity().getApplicationContext());
        dailyData = new DailyData();
        dailyDataList = new ArrayList<>();
        new getUrlAsyncTask(getActivity().getApplicationContext()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_dailyrecord_listview, parent, false);

        mAdapter = new CustomDailyAdapter(parent.getContext());
        mListView = (ListView)v.findViewById(R.id.listView);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private class getUrlAsyncTask extends AsyncTask<Void, Void, String>{
        String urlString = "http://evolveme.apmato.com:3000/api/1.0/diary?";
        private Context context;

        private getUrlAsyncTask(Context context){this.context = context;}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            String parameters = "username="+profile.getStringValue("email");
            parameters += "&password="+profile.getStringValue("password");
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
                Log.d(TAG, "doInBackground:" +reponseString);

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
        final String DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss";
        String weight = "test weight";
        String dateStr = "test entryDate";
        String pictureName = "test pic";
        String whoVerified = "who is tester";
        Date newDate = null;

            try {
                JSONObject jsonOri = new JSONObject(jsonStr);
                jsonStr = jsonOri.get("diary").toString();
                JSONArray jsonArray = new JSONArray(jsonStr);//everthing is in array 1... ??
                Log.d(TAG, String.valueOf(jsonArray.length()));

                for(int i = 0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d(TAG, String.valueOf(i));
                    weight = jsonObject.getString("data");
                    dateStr = jsonObject.getString("entryDate");
                    pictureName = jsonObject.getString("picture");
                    whoVerified = jsonObject.getString("verifiedByReferee");

                    if(dateStr.contains(" GMT+0000 (UTC)")){
                        dateStr = dateStr.replace(" GMT+0000 (UTC)", "");
                    }
                    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH); //when deivce select diffrent language with date language it crush. Locale.English is important
                    try {
                        newDate = format.parse(dateStr);
                    }catch (ParseException e){
                        //newDate = new Date();
                        e.printStackTrace();
                        Log.d(TAG, "date parsing failed.");
                    }

                    dailyDataList.add(i, new DailyData());
                    pictureName = "/storage/emulated/0/Android/data/com.apmato.evolveme/files/Pictures/"+pictureName;
                    File imgFile = new File(pictureName);

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    }
                    dailyDataList.get(i).setPhoto(imgFile);

                    try{
                        float weightFloat = Float.parseFloat(weight);
                        dailyDataList.get(i).setWeight(weightFloat);
                        dailyDataList.get(i).setDate(newDate);
                        dailyDataList.get(i).setVerifiedByReferee(Boolean.valueOf(whoVerified));
                    }catch (NumberFormatException e){
                        dailyDataList.get(i).setWeight(0.0f);
                        dailyDataList.get(i).setDate(newDate);
                        dailyDataList.get(i).setVerifiedByReferee(Boolean.valueOf(whoVerified));
                        e.printStackTrace();
                    }
                }
                for(int i=0; i< dailyDataList.size(); i++){
                    Log.d("DailyRecordFor", String.valueOf(dailyDataList.get(i).getWeight()));
                }
                mAdapter.addItem(dailyDataList);
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
