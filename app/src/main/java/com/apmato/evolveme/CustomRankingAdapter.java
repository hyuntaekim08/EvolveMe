package com.apmato.evolveme;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by hyuntae on 19/10/16.
 */
public class CustomRankingAdapter extends BaseAdapter {
    Context context;

    static RecordHolder holder;

    public ArrayList<RankingData> mList = new ArrayList<>();

    public CustomRankingAdapter(Context context){this.context = context;}

    public static void setHolder(RecordHolder holder){CustomRankingAdapter.holder = holder;}

    @Override
    public int getCount(){return mList.size();}

    @Override
    public Object getItem(int postion){return mList.get(postion);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Context context = parent.getContext();

        TextView nameText;
        TextView percentText;
        ImageView imageView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_ranking_item, parent, false);

            nameText = (TextView)convertView.findViewById(R.id.nameText);
            percentText = (TextView)convertView.findViewById(R.id.percentText);
            imageView = (ImageView)convertView.findViewById(R.id.imageCup);

            setHolder(new RecordHolder());
            holder.nameView = nameText;
            holder.percentView = percentText;
            holder.imageView = imageView;


            convertView.setTag(holder);
        }else{
            holder = (RecordHolder)convertView.getTag();
            imageView = holder.imageView;
            nameText = holder.nameView;
            percentText = holder.percentView;
        }

        //Sorting by percentage
        Collections.sort(mList, new Comparator<RankingData>() {
            @Override
            public int compare(RankingData rankingData, RankingData t1) {
                if(rankingData.percent < t1.percent){
                    return 1;
                }else if(rankingData.percent > t1.percent){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        RankingData mData = mList.get(position);
        if(mData == mList.get(0)){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.nameView.setVisibility(View.VISIBLE);
            holder.percentView.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.nameView.setVisibility(View.VISIBLE);
            holder.percentView.setVisibility(View.VISIBLE);
        }

        holder.nameView.setText(mData.getName());
        if(mData.getPercent()<0){
            String percentStr = String.valueOf((int) mData.getPercent());
            percentStr = percentStr.replace("-", "+");
            holder.percentView.setText(percentStr + "%");
        }else if((int)mData.getPercent() == 0){
            holder.percentView.setText(String.valueOf((int) mData.getPercent() + "%"));
        }else {
            holder.percentView.setText("-" + String.valueOf((int) mData.getPercent()) + "%");
        }
        return convertView;
    }

    public void addItem(ArrayList<RankingData> list){
        mList = list;
    }

    private class RecordHolder{
        TextView nameView;
        TextView percentView;
        ImageView imageView;
    }
}
