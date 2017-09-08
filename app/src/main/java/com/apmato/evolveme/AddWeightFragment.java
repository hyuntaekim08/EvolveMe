package com.apmato.evolveme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by hyuntae on 31/08/16.
 */
public class AddWeightFragment extends Fragment{
    private static final String DIALOG_DATE ="date";
    private static final String DIALOG_TIME ="time";
    private static final String DIALOG_WEIGHT ="weight";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_READ = 1;
    private static final int REQUEST_WRITE = 2;
    private static final int REQUEST_CAMERA = 3;
    private static final String TAG ="AddWeightFragment";
    String weight;
    TextView introText;
    TextView dateText;
    TextView timeText;
    EditText weightText;
    ImageButton cameraButton;
    private static CircularImageView cameraImageView;
    DailyData dailyData;
    Bundle extras = new Bundle();
    public List<BasicNameValuePair> weightValuePairs;
    UserProfile profile;
    static File pictureFile;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dailyData = new DailyData();
        weightValuePairs = new ArrayList<BasicNameValuePair>();
        profile = UserProfile.sharedInstance(getActivity().getApplicationContext());
        context = getActivity().getApplicationContext();
        locationpermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_addweight, parent, false);

        introText = (TextView)v.findViewById(R.id.titletextview);

        cameraImageView = (CircularImageView)v.findViewById(R.id.cameraImageView);
        cameraImageView.setBorderColor(getResources().getColor(R.color.GrayLight));
        cameraImageView.setBorderWidth(10);
        cameraImageView.addShadow();
        cameraImageView.setVisibility(View.INVISIBLE);

        dateText = (TextView)v.findViewById(R.id.TextViewDate);
        /*dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(dailyData.getDate());
                dateDialog.setTargetFragment(AddWeightFragment.this, REQUEST_DATE);
                dateDialog.show(fm, DIALOG_DATE);
            }
        });*/

        timeText = (TextView)v.findViewById(R.id.TextViewTime);
        /*timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(dailyData.getHour(), dailyData.getMin());
                timeDialog.setTargetFragment(AddWeightFragment.this, REQUEST_TIME);
                timeDialog.show(fm, DIALOG_TIME);
            }
        });*/

        weightText = (EditText)v.findViewById(R.id.TextViewWeight);
        weightText.setFocusableInTouchMode(false);
        weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightText.setFocusableInTouchMode(true);
            }
        });
        weightText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    weightText.setFocusable(false);
                    dateText.setText(new Date().toString());
                    long time = System.currentTimeMillis();
                    SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
                    String str = dayTime.format(new Date(time));
                    timeText.setText(str);
                    if (cameraImageView.getVisibility() == View.INVISIBLE) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Foto aufnehmen");
                        alertDialog.setMessage("Bitte mache zuerst ein Foto und trage dann das Gewicht ein.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        weightText.getText().clear();
                    } else {
                        weight = weightText.getText().toString();
                        new AsyncHttpGetTask("http://evolveme.apmato.com:3000/api/1.0/addEntry?").execute();
                        Toast.makeText(getActivity().getApplicationContext(), " Danke, dass du das Foto gemacht hast.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        weightText.setText("", TextView.BufferType.EDITABLE);
        weightText.getText().delete(0, weightText.getText().length());
        weightText.getText().clear();
        Log.d(TAG, weightText.getText().toString());
        /*weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                WeightPickerFragment weightDialog = WeightPickerFragment.newInstance(dailyData.getWeight());
                weightDialog.setTargetFragment(AddWeightFragment.this, REQUEST_WEIGHT);
                weightDialog.show(fm, DIALOG_WEIGHT);

            }
        });*/


        cameraButton = (ImageButton)v.findViewById(R.id.imageButtonCamera);
        cameraButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                cameraImageView.setVisibility(View.VISIBLE);


            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Log.d(TAG, "onActivityResult executed");
        /*
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            dailyData.setDate(date);
            String dateStr = dailyData.getDate().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
            SimpleDateFormat f1 = new SimpleDateFormat("dd.MMM.yyyy");
            try {
                Date simpleDate = formatter.parse(dateStr);
                dateText.setText(f1.format(simpleDate));
            }catch(ParseException e) {
                Log.d(DIALOG_DATE, "parsing failed");
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_TIME){
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE, 0);
            dailyData.setHour(hour);
            dailyData.setMin(minute);
            timeText.setText(dailyData.getHour() + ":" + dailyData.getMin());
        }else if(requestCode == REQUEST_WEIGHT){
            float weight = data.getFloatExtra(WeightPickerFragment.EXTRA_WEIGHT, 0);
            dailyData.setWeight(weight);
            String weightStr = String.valueOf(dailyData.getWeight());
            weightText.setText(weightStr);
        }else */
        if(requestCode == REQUEST_CAMERA){
            Log.d(TAG, "onActivityResult Camera executed");
            PictureUtils pictureUtils = new PictureUtils();
            if(data.hasExtra("data")) {
                extras = data.getExtras();
                pictureUtils = new PictureUtils((Bitmap) extras.get("data"));
                cameraImageView.setImageBitmap(pictureUtils.getBitmap());
                introText.setText("Glückwunsch! Vielen Dank, dass Du Dein Gewicht eingetragen hast.");
            }else{
                Uri imageUri = data.getData();
                try {
                    introText.setText("Glückwunsch! Vielen Dank, dass Du Dein Gewicht eingetragen hast.");
                    Bitmap vignette = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    vignette =Bitmap.createScaledBitmap(vignette ,100,100, true);
                    pictureUtils = new PictureUtils(vignette);
                    cameraImageView.setImageBitmap(pictureUtils.getBitmap());
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            pictureFile = getOutputMediaFile(getActivity().getApplicationContext()); //create empty File

            if(pictureFile == null){
                Toast.makeText(getActivity(), "Error saving", Toast.LENGTH_LONG).show();
                return;
            }
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(pictureFile); //put file at output stream
                pictureUtils.getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fos); //save image to file through file output stream
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally{
                try{
                    if(fos != null){
                        fos.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static File getOutputMediaFile(Context context) {
        // Environment.getExternalStorageState() check if sd card is mounted
        Log.d(TAG, "getOutputMediaFile executed");
        File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); //storage path is in the evolveme application dir
        // create the path which save the pics


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        Log.i("MyCamera", "Saved at" + context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));

        return mediaFile;
    }


    public class AsyncHttpGetTask extends AsyncTask<Void, Void, String> {

        private final String TAG = AsyncHttpGetTask.class.getSimpleName();
        private String server;

        public AsyncHttpGetTask(final String server) {
            this.server = server;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            String parameters = "username="+profile.getStringValue("email");
            parameters += "&password="+profile.getStringValue("password")+"&";
            weightValuePairs.add(new BasicNameValuePair("weight", weight));
            weightValuePairs.add(new BasicNameValuePair("picture", pictureFile.getName()));
            parameters += URLEncodedUtils.format(weightValuePairs, "utf-8");
            server += parameters;
            weightValuePairs.clear();
            Log.d(TAG, server);
        }
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
            String responseString = "";
            //method.setEntity(new FileEntity(params[0], "text/plain"));
            try {
                URI url = new URI(server);
                httpGet.setURI(url);
                HttpResponse response = httpClient.execute(httpGet);
                responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                Log.d(TAG, responseString);
            }catch (URISyntaxException e){
                e.printStackTrace();
            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return responseString;
        }
    }
    private void locationpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                Toast.makeText(context, "Camera permisson needed", Toast.LENGTH_SHORT);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "Camera permisson needed", Toast.LENGTH_SHORT);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "Camera permisson needed", Toast.LENGTH_SHORT);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)     {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    cameraButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Camera permisson needed", Toast.LENGTH_SHORT);
                        }
                    });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    cameraButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Read storage permisson needed", Toast.LENGTH_SHORT);
                        }
                    });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    cameraButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Write storage permisson needed", Toast.LENGTH_SHORT);
                        }
                    });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
