package com.apmato.evolveme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    UserProfile profile;
    private ListView mListView;
    private CustomListAdapter mAdapter;
    private TextView mTitle;
    static ListData listData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = UserProfile.sharedInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        mAdapter = new CustomListAdapter(getActivity().getApplicationContext());
        mTitle = (TextView)v.findViewById(R.id.title_home);
        mListView = (ListView) v.findViewById(R.id.listView);

        mTitle.setText(profile.getStringValue("statusText"));
        // connect adapter to ListView
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //todo connect ranking and weight fragments to here
                listData = (ListData) mAdapter.getItem(i);
                switch (listData.centerTextView) {
                    case "Gewicht eintragen":
                        Intent intent = new Intent(getActivity(), AddWeightActivity.class);
                        startActivity(intent);
                        break;
                    case "Ranking":
                        FragmentManager fm = getFragmentManager();
                        Fragment fragmentRanking = new RankingFragment();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentRanking).addToBackStack(null).commit();
                        break;
                    default:
                        //
                        break;
                }
            }
        });

        // add items at ListView
        mAdapter.addItem(R.drawable.icon_star, "Ranking");
        mAdapter.addItem(R.drawable.icon_plus, "Gewicht eintragen");

        return v;
    }
}

