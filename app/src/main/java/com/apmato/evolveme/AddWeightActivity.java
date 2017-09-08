package com.apmato.evolveme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by hyuntae on 31/08/16.
 */
public class AddWeightActivity extends FragmentActivity {
    private ImageButton mAddWeightUnderIcon;
    private ImageButton mSuccessUnderIcon;
    private ImageButton mCupUnderIcon;
    private ImageButton mProfileUnderIcon;
    private ImageButton mInfoUnderIcon;
    FragmentManager fm = getFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAddWeightUnderIcon = (ImageButton)findViewById(R.id.addweight_icon);
        mSuccessUnderIcon = (ImageButton)findViewById(R.id.flag_icon);
        mCupUnderIcon = (ImageButton)findViewById(R.id.trophy_icon);
        mProfileUnderIcon = (ImageButton)findViewById(R.id.human_icon);
        mInfoUnderIcon = (ImageButton)findViewById(R.id.info_icon);

        mAddWeightUnderIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mSuccessUnderIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mCupUnderIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mProfileUnderIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mInfoUnderIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        if(fragment == null){
            fragment = new AddWeightFragment();
            fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }
        mSuccessUnderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSuccessUnderIcon.setImageResource(R.drawable.icon_success_selected);
                mInfoUnderIcon.setImageResource(R.drawable.icon_information);
                mAddWeightUnderIcon.setImageResource(R.drawable.icon_plus_under);
                mCupUnderIcon.setImageResource(R.drawable.icon_cup);
                mProfileUnderIcon.setImageResource(R.drawable.icon_profile);
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new AchievementFragment();
                ft.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });

        mInfoUnderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInfoUnderIcon.setImageResource(R.drawable.icon_information_selected);
                mAddWeightUnderIcon.setImageResource(R.drawable.icon_plus_under);
                mCupUnderIcon.setImageResource(R.drawable.icon_cup);
                mProfileUnderIcon.setImageResource(R.drawable.icon_profile);
                mSuccessUnderIcon.setImageResource(R.drawable.icon_success);
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new InfoOverviewFragment();
                ft.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });

        mAddWeightUnderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddWeightUnderIcon.setImageResource(R.drawable.icon_plus_selected);
                mCupUnderIcon.setImageResource(R.drawable.icon_cup);
                mProfileUnderIcon.setImageResource(R.drawable.icon_profile);
                mSuccessUnderIcon.setImageResource(R.drawable.icon_success);
                mInfoUnderIcon.setImageResource(R.drawable.icon_information);
                FragmentTransaction ft= fm.beginTransaction();
                fragment = new AddWeightFragment();
                ft.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });
        mCupUnderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCupUnderIcon.setImageResource(R.drawable.icon_cup_selected);
                mProfileUnderIcon.setImageResource(R.drawable.icon_profile);
                mSuccessUnderIcon.setImageResource(R.drawable.icon_success);
                mInfoUnderIcon.setImageResource(R.drawable.icon_information);
                mAddWeightUnderIcon.setImageResource(R.drawable.icon_plus_under);
                FragmentTransaction ft= fm.beginTransaction();
                fragment = new HomeFragment();
                ft.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });
        mProfileUnderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileUnderIcon.setImageResource(R.drawable.icon_profile_selected);
                mSuccessUnderIcon.setImageResource(R.drawable.icon_success);
                mInfoUnderIcon.setImageResource(R.drawable.icon_information);
                mAddWeightUnderIcon.setImageResource(R.drawable.icon_plus_under);
                mCupUnderIcon.setImageResource(R.drawable.icon_cup);
                FragmentTransaction ft= fm.beginTransaction();
                fragment = new ProfileOverviewFragment();
                ft.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });
    }
}
