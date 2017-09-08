package com.apmato.evolveme;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hyuntae on 24/10/16.
 */
public class SpielregelnFragment extends Fragment {
    TextView infoTextView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState){
        View v = inflater.inflate(R.layout.fragment_information, parent, false);
        infoTextView = (TextView)v.findViewById(R.id.info_textView);
        infoTextView.setText(R.string.spielrengeln);
        infoTextView.setTextColor(Color.WHITE);
        return v;
    }
}
