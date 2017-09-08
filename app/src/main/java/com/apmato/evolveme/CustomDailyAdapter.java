package com.apmato.evolveme;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hyuntae on 12/09/16.
 */
public class CustomDailyAdapter extends BaseAdapter {
    Context context;

    static RecordHolder holder;

    public ArrayList<DailyData> mList = new ArrayList<>();

    public CustomDailyAdapter(Context context){
        this.context = context;
    }

    public static void setHolder(RecordHolder holder){
        CustomDailyAdapter.holder = holder;
    }

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

        ImageView mImageView;
        TextView mWeightTextView;
        TextView mDateTextView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_daily_item, parent, false);

            mImageView = (ImageView)convertView.findViewById(R.id.cameraImageView);
            mWeightTextView = (TextView)convertView.findViewById(R.id.weightView);
            mDateTextView = (TextView)convertView.findViewById(R.id.dateView);

            setHolder(new RecordHolder());
            holder.mImageView = mImageView;
            holder.mWeightTextView = mWeightTextView;
            holder.mDateTextView = mDateTextView;
            convertView.setTag(holder);
        }else{
            holder = (RecordHolder)convertView.getTag();
            mImageView = holder.mImageView;
            mWeightTextView = holder.mWeightTextView;
            mDateTextView = holder.mDateTextView;
        }

        DailyData mData = mList.get(position);

        holder.mImageView.setVisibility(View.VISIBLE);
        holder.mWeightTextView.setVisibility(View.VISIBLE);
        holder.mDateTextView.setVisibility(View.VISIBLE);

        if(mData.getPhoto() == null){
            holder.mImageView.setImageResource(R.drawable.defaultweightimage);
        }else {
            holder.mImageView.setImageBitmap(mData.getPhoto());
        }
        if(mData.isVerifiedByReferee()){
            holder.mWeightTextView.setTextColor(Color.RED);
            holder.mDateTextView.setTextColor(Color.RED);
        }else{
            holder.mWeightTextView.setTextColor(Color.YELLOW);
            holder.mDateTextView.setTextColor(Color.YELLOW);
        }
        holder.mWeightTextView.setText(String.valueOf(mData.getWeight()));
        Date date = mData.getDate();
        SimpleDateFormat format = new SimpleDateFormat("dd. MMM. yyyy", Locale.ENGLISH);
        String dateStr = format.format(date);
        holder.mDateTextView.setText(dateStr);

        return convertView;
    }

    public void addItem(ArrayList<DailyData> list) {
        mList = list;
        for(int i =0; i<mList.size(); i++) {
            Log.d("CustomDailyAdapter", String.valueOf(mList.get(i).getWeight()));
            Log.d("CustomDailyAdapter", i+"i is finished");
        }
    }

    private class RecordHolder {
        ImageView mImageView;
        TextView mWeightTextView;
        TextView mDateTextView;
    }
}
