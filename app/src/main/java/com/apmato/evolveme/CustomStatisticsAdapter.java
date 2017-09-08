package com.apmato.evolveme;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hyuntae on 17/10/16.
 */
public class CustomStatisticsAdapter extends BaseAdapter {
    public static final String TAG = "CustomStaticsAdapter";
    public ArrayList<StatisticsData> mList = new ArrayList<>();
    static RecordHolder holder;
    Context context;
    StatisticsFagment fragment = new StatisticsFagment();

    public CustomStatisticsAdapter(Context context){this.context = context;}

    @Override
    public int getCount(){return mList.size();}

    @Override
    public Object getItem(int position){return mList.get(position);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        TextView titleText;
        ProgressBar progressBar;
        ImageView icon;
        TextView iconDescription;
        int progressStatus = 0;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_statistics_item, parent, false);
            
            titleText = (TextView)convertView.findViewById(R.id.statistics_textview);
            progressBar = (ProgressBar)convertView.findViewById(R.id.statistics_progressbar);
            icon = (ImageView)convertView.findViewById(R.id.statistics_imageview);
            iconDescription = (TextView)convertView.findViewById(R.id.statistics_textview_nexttoimage);

            setHolder(new RecordHolder());
            holder.titleText = titleText;
            holder.progressBar = progressBar;
            holder.icon = icon;
            holder.iconDescription = iconDescription;
            convertView.setTag(holder);
        }else{
            holder = (RecordHolder)convertView.getTag();
            titleText = holder.titleText;
            progressBar = holder.progressBar;
            icon = holder.icon;
            iconDescription = holder.iconDescription;
        }
        StatisticsData mData = mList.get(position);

        holder.titleText.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.icon.setVisibility(View.VISIBLE);
        holder.iconDescription.setVisibility(View.VISIBLE);

        titleText.setTextColor(Color.YELLOW);
        iconDescription.setTextColor(Color.WHITE);
        if(mData.getDaysRemaining() != -1){
            titleText.setText(String.valueOf(mData.getDaysRemaining())+" Tage");
            double remainDay = mData.getDaysRemaining()*1.0;
            double tempNum = remainDay/30.0 *100;
            Log.d(TAG, "tempNum "+tempNum);
            progressStatus = (int)tempNum;
            progressBar.setProgress(progressStatus);
            icon.setImageResource(R.drawable.icon_calendar);
            iconDescription.setText("   Verbleibende Tage");
        }else if(mData.getWeight() != -1){
            titleText.setText(String.valueOf(mData.getWeight()) + " Kg");
            float tempNum = mData.getWeight()/Float.parseFloat(fragment.initialWeight)*100;
            progressStatus = (int)tempNum;
            progressBar.setProgress(progressStatus);
            icon.setImageResource(R.drawable.icon_weight);
            iconDescription.setText("   Startgewicht");
        }else if(mData.getPercentage() != -1){
            titleText.setText("-" +String.valueOf((int)mData.getPercentage()) + "%");
            progressBar.setProgress((int)mData.getPercentage());
            icon.setImageResource(R.drawable.icon_fire);
            iconDescription.setText("   Verbranntes Gewicht");
        }else if(mData.getAchivements() != -1){
            titleText.setText("");
            float tempNum = mData.achievements/4*100;
            progressBar.setProgress((int)tempNum);
            icon.setImageResource(R.drawable.icon_flag);
            iconDescription.setText("  "+String.valueOf(mData.achievements)+" Erfolge");
        }

        return convertView;
    }

    public void addItem(ArrayList<StatisticsData> list){ mList = list;}

    public static void setHolder(RecordHolder holder){ CustomStatisticsAdapter.holder = holder;}

    private class RecordHolder{
        TextView titleText;
        ProgressBar progressBar;
        ImageView icon;
        TextView iconDescription;
    }
}
