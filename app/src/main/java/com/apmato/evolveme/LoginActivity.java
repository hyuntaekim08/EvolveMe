package com.apmato.evolveme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public String TAG = "LoginActivity";
    private static UserProfile userProfile;
    public static EditText mEmailText;
    public static EditText mPasswordText;
    private Button mLoginButton;
    public static List<BasicNameValuePair> nameValuePairs;
    static String email;
    static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userProfile = UserProfile.sharedInstance(this.getApplicationContext());
        nameValuePairs = new ArrayList<BasicNameValuePair>(0);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0 && mEmailText.getText().length()>0){
                    mLoginButton.setEnabled(true);
                }else{
                    mLoginButton.setEnabled(false);
                }
            }
        };
        int color = Color.parseColor("#1D747E");
        mEmailText = (EditText)findViewById(R.id.emailText);
        mEmailText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mEmailText.addTextChangedListener(textWatcher);

        mPasswordText = (EditText)findViewById(R.id.passwdText);
        mPasswordText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mPasswordText.addTextChangedListener(textWatcher);

        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    public void attemptLogin(){
        mEmailText.setError(null);
        mPasswordText.setError(null);
        boolean cancel = false;
        View focuseView = null;

        email = mEmailText.getText().toString();
        mEmailText.getText().clear();
        password = mPasswordText.getText().toString();
        mPasswordText.getText().clear();

        if(!TextUtils.isEmpty(password) && !isPasswordValid(password)){
            mPasswordText.setError("This password is too short");
            focuseView = mPasswordText;
            cancel = true;
        }

        if(TextUtils.isEmpty(email)){
            mEmailText.setError("This field is required");
            focuseView = mEmailText;
            cancel = true;
        }/*else if(!isEmailValid(email)){
            mEmailText.setError("This email is not valid");
        }*/

        if(cancel){
            focuseView.requestFocus();
        }else{
            new getUrlAsyncTask(getApplicationContext()).execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic

        return password.length() > 2;
    }

    private class getUrlAsyncTask extends AsyncTask<Void, Void, String> {
        String urlString = "http://tcmean.westeurope.cloudapp.azure.com:3000/api/1.0/login?"; ///url;
        private Context context;

        private getUrlAsyncTask(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nameValuePairs.add(new BasicNameValuePair("username", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            String parameters = URLEncodedUtils.format(nameValuePairs, "utf-8");
            Log.d(TAG, parameters);
            urlString += parameters;
        }

        @Override
        protected String doInBackground(Void... params){
            HttpClient httpClient = new DefaultHttpClient();
            String responseString = "";

            try{
                HttpGet httpGet = new HttpGet();
                URI url = new URI(urlString);
                httpGet.setURI(url);
                HttpResponse response = httpClient.execute(httpGet);
                responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                Log.d(TAG, responseString);
            }catch(URISyntaxException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }catch(ClientProtocolException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }catch(IOException e){
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String response){
            userProfile.setIsAuthenticated(true);

            if(!hasJsonErr(response)){
                userProfile.setIsAuthenticated(true);
                Intent intent = new Intent(context, IntroActivity.class);
                startActivity(intent);

                try{
                    parseItem(response);
                }catch (JSONException e){
                    Log.d(TAG, "parsing failed");
                }
            }else{
                userProfile.setIsAuthenticated(false);
                Toast.makeText(getApplicationContext(), "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                nameValuePairs.clear();
            }

        }
    }

    void parseItem(String jsonStr) throws JSONException{
        String name = "test name";
        String age = "test age";
        String gender = "test gender";
        String company = "test company";
        String initialweight;
        String statusText = "";


        try{
            JSONObject jsonObjectFull = new JSONObject(jsonStr);
            JSONObject jsonObjectRole = jsonObjectFull.getJSONObject("roles");
            JSONObject jsonObjectAccount = jsonObjectRole.getJSONObject("account");
            JSONObject jsonObjectForName = jsonObjectAccount.getJSONObject("name");
            JSONArray acheivementArr = jsonObjectAccount.getJSONArray("achievements");

            name = jsonObjectForName.getString("full");
            gender = jsonObjectAccount.getString("gender");
            company = jsonObjectAccount.getString("company");
            age = jsonObjectAccount.getString("age");
            initialweight = jsonObjectAccount.getString("initialWeight");
            statusText = jsonObjectAccount.getString("statusText");



            userProfile.storeUserValue("email", email);
            userProfile.storeUserValue("name", name);
            userProfile.storeUserValue("password", password);
            userProfile.storeUserValue("gender", gender);
            userProfile.storeUserValue("company", company);
            userProfile.storeUserValue("age", age);
            userProfile.storeUserValue("initialWeight", initialweight);
            userProfile.storeUserValue("profilePicture", "");
            userProfile.storeUserValue("statusText", statusText);
            userProfile.storeUserValue("achievement", acheivementArr.toString());
            userProfile.setIsAuthenticated(true);
            userProfile.setLogin();
            Log.d(TAG, userProfile.loadJsonUserValues());
        }catch(JSONException e){
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
        }
        return true;
    }
}
