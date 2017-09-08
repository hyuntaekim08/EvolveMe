package com.apmato.evolveme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by hyuntae on 06/09/16.
 */
public class ProfileFragment extends Fragment {
    private static final int REQUEST_CAMERA = 4;
    private CustomProfileAdapter mAdapter;
    private ListView mListView;
    private UserProfile userProfile;
    static private ImageView mImageView;
    public Bundle bundle = new Bundle();
    private Bundle extras = new Bundle();
    private Bundle userprofileBundle = new Bundle();
    static File pictureFile;
    public static String pictureName = "";
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);
        userProfile = UserProfile.sharedInstance(parent.getContext());
        mImageView = (ImageView)v.findViewById(R.id.cameraImageView);
        mImageView.setVisibility(View.VISIBLE);

        File imgFile = new File(pictureName);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mImageView.setImageBitmap(myBitmap);

        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });

        mAdapter = new CustomProfileAdapter(parent.getContext());
        mListView = (ListView)v.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        bundle = userProfile.loadBundleUserValues();

        if(bundle.get("initialWeight") instanceof Float){
            mAdapter.addItem("Startgewicht",String.valueOf(bundle.getFloat("initialWeight")));
        }else if(bundle.get("initialWeight") instanceof Integer){
            mAdapter.addItem("Startgewicht",String.valueOf(bundle.getInt("initialWeight")));
        }else{
            mAdapter.addItem("Startgewicht",bundle.getString("initialWeight"));
        }

        mAdapter.addItem("Name", bundle.getString("name"));
        mAdapter.addItem("Email", bundle.getString("email"));
        mAdapter.addItem("Alter", bundle.getString("age")+" Jahre");
        mAdapter.addItem("Company", bundle.getString("company"));
        mAdapter.addItem("Gender", bundle.getString("gender"));
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CAMERA){
            PictureUtils pictureUtils = new PictureUtils();
            if(data.hasExtra("data")) {
                extras = data.getExtras();
                pictureUtils = new PictureUtils((Bitmap) extras.get("data"));
                mImageView.setImageBitmap(pictureUtils.getBitmap());
            }else{
                Uri imageUri = data.getData();
                try {
                    Bitmap vignette = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    vignette = Bitmap.createScaledBitmap(vignette ,100,100, true);
                    pictureUtils = new PictureUtils(vignette);
                    mImageView.setImageBitmap(pictureUtils.getBitmap());
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            pictureFile = getOutputMediaFile(getActivity().getApplicationContext());

            if(pictureFile == null){
                Toast.makeText(getActivity(), "Error saving", Toast.LENGTH_LONG).show();
                return;
            }
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(pictureFile);
                pictureUtils.getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fos);
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

        File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); //storage path is in the evolveme application dir
        // create the path which save the pics


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }


        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "profile" + ".jpg");
        pictureName = "/storage/emulated/0/Android/data/com.apmato.evolveme/files/Pictures/" + "profile" + ".jpg";

        Log.i("MyCamera", "Saved at" + context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));

        return mediaFile;
    }
}
