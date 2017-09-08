package com.apmato.evolveme;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hyuntae on 24/10/16.
 */
public class ImpressumFragment extends Fragment {
    TextView infoTextView;
    TextView versionInfo;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState){
        View v = inflater.inflate(R.layout.fragment_information, parent, false);
        infoTextView = (TextView)v.findViewById(R.id.info_textView);
        infoTextView.setText(R.string.impressum);
        infoTextView.setTextColor(Color.WHITE);

        PackageInfo pi = null;
        String dateStr = "";
        try{
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long lastUpdateTime = pi.lastUpdateTime;
            Date updateTiemDate = new Date(lastUpdateTime);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            dateStr = format.format(updateTiemDate);
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        versionInfo = (TextView)v.findViewById(R.id.version_info);
        versionInfo.setText("Version: "+String.valueOf(pi.versionName)+" - "+dateStr);
        versionInfo.setTextColor(Color.WHITE);

        return v;
    }
}
