package com.apmato.evolveme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hyuntae on 06/09/16.
 *
 * this adapter is for profile fragment listview (not for the overview)
 * if you want to edit overview go to CustomListAdapter
 */
public class CustomProfileAdapter extends BaseAdapter {

    Context context;

    static ProfileHolder holder;

    public ArrayList<ProfileData> mList = new ArrayList<>();

    public CustomProfileAdapter(Context context) {
        this.context = context;
    }

    public static ProfileHolder getHolder() {
        return holder;
    }

    public static void setHolder(ProfileHolder holder) {
        CustomProfileAdapter.holder = holder;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView textLeft    = null;
        TextView textRight   = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_profile_item, parent, false);

            textLeft    = (TextView) convertView.findViewById(R.id.textleft);
            textRight     = (TextView) convertView.findViewById(R.id.textright);

            // Tag and holder register
            setHolder(new ProfileHolder());
            holder.mLeftTextView = textLeft;
            holder.mRightTextView = textRight;
            convertView.setTag(holder);
        }
        else {
            holder  = (ProfileHolder) convertView.getTag();
            textLeft    = holder.mLeftTextView;
            textRight   = holder.mRightTextView;
        }

        // Text
        ProfileData mData = mList.get(position);

        holder.mRightTextView.setVisibility(View.VISIBLE);
        holder.mLeftTextView.setVisibility(View.VISIBLE);

        holder.mLeftTextView.setText(mData.title);
        holder.mRightTextView.setText(mData.value);

        return convertView;
    }

    private class ProfileHolder {
        TextView mLeftTextView;
        TextView mRightTextView;
    }

    private class ProfileData{
        String title;
        String value;
    }

    public void addItem(String title, String value) {
        ProfileData addInfo;
        addInfo = new ProfileData();
        addInfo.title = title;
        addInfo.value = value;
        mList.add(addInfo);
    }

    public void remove(int _position) {
        mList.remove(_position);
    }


}
