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

/**
 * Created by hyuntae on 24/10/16.
 */
public class InfoOverviewFragment extends Fragment {
    private TextView mTitle;
    private ListView mListView;
    private CustomListAdapter mAdapter;
    static ListData listData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        mTitle = (TextView) v.findViewById(R.id.title_home);
        mTitle.setText("");
        mAdapter = new CustomListAdapter(getActivity().getApplicationContext());
        mListView = (ListView) v.findViewById(R.id.listView);

        // connect adapter to ListView
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //todo connect ranking and weight fragments to here
                listData = (ListData) mAdapter.getItem(i);
                switch (listData.centerTextView) {
                    case "Spielregeln":
                        FragmentManager fm = getFragmentManager();
                        Fragment fragmentSpiel = new SpielregelnFragment();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentSpiel).addToBackStack(null).commit();
                        break;
                    case "Impressum":
                        Fragment fragmentImpressum = new ImpressumFragment();
                        fm = getFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentImpressum).addToBackStack(null).commit();
                        break;
                    default:
                        //
                        break;
                }
            }
        });

        // add items at ListView
        mAdapter.addItem(R.drawable.icon_hourglass, "Spielregeln");
        mAdapter.addItem(R.drawable.icon_paragraph, "Impressum");


        return v;
    }
}
