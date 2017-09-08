package com.apmato.evolveme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyuntae on 20/10/16.
 */
public class AchievementFragment extends Fragment {

    private ListView mListView;
    private CustomListAdapter mAdapter;
    static ListData listData;
    UserProfile profile;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        profile = UserProfile.sharedInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_achievement, parent, false);
        mAdapter = new CustomListAdapter(getActivity().getApplicationContext());
        mListView = (ListView)v.findViewById(R.id.achievement_listview);

        mListView.setAdapter(mAdapter);
        String achievementStr = profile.getStringValue("achievement");
        achievementStr = achievementStr.replace("[", "");
        achievementStr = achievementStr.replace("]", "");
        List<String> list = new ArrayList<>(Arrays.asList(achievementStr.split(",")));
        for(int i = 0; i<list.size(); i++){
            mAdapter.addItem(R.drawable.icon_cup, list.get(i));
        }

        return v;
    }
}
