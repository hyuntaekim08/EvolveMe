package com.apmato.evolveme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by hyuntae on 06/09/16.
 */
public class ProfileOverviewFragment extends Fragment {

    private ListView mListView;
    private CustomListAdapter mAdapter;
    private ListData listData;

    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile_overview, parent, false);

        mAdapter = new CustomListAdapter(getActivity().getApplicationContext());
        mListView = (ListView)v.findViewById(R.id.listView);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listData = (ListData) mAdapter.getItem(i);
                switch (listData.centerTextView) {
                    case "Profil":
                        FragmentManager fm = getFragmentManager();
                        Fragment fragmentProfile = new ProfileFragment();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentProfile).addToBackStack(null).commit();
                        break;
                    case "Tagebuch":
                        Fragment fragmentDiray = new DailyRecordFragment();
                        fm = getFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentDiray).addToBackStack(null).commit();
                        break;
                    case "Statistik":
                        //add something
                        Fragment fragmentStatistics = new StatisticsFagment();
                        fm = getFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentStatistics).addToBackStack(null).commit();
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.addItem(R.drawable.defalut_profile_picture, "Profil");
        mAdapter.addItem(R.drawable.icon_image, "Tagebuch");
        mAdapter.addItem(R.drawable.icon_infographic, "Statistik");

        return v;
    }
}
