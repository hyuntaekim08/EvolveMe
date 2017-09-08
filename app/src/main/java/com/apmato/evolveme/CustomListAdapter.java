package com.apmato.evolveme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hyuntae on 31/08/16.
 */
public class CustomListAdapter extends BaseAdapter {

    Context context;
    UserProfile profile;

    static CustomHolder    holder;

    public static CustomHolder getHolder() {
        return holder;
    }

    public static void setHolder(CustomHolder holder) {
        CustomListAdapter.holder = holder;
    }

    private ArrayList<ListData>   m_List;

    public CustomListAdapter(Context context) {
        m_List = new ArrayList<ListData>();
        this.context = context;
        profile = UserProfile.sharedInstance(context);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView        text    = null;
        ImageView       image   = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_item, parent, false);

            text    = (TextView) convertView.findViewById(R.id.text);
            image     = (ImageView) convertView.findViewById(R.id.image);

            // Tag and holder register
            setHolder(new CustomHolder());
            holder.m_TextView   = text;
            holder.m_ImageView  = image;
            convertView.setTag(holder);
        }
        else {
            holder  = (CustomHolder) convertView.getTag();
            text    = holder.m_TextView;
            image   = holder.m_ImageView;
        }

        // Text

        ListData mData = m_List.get(position);
        if(mData.imageView != 0){
            File imgFile = new File(ProfileFragment.pictureName);
            if(imgFile.exists() && mData.getCenterTextView() == "Profil"){ //set profile picture on profile overview listview
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.m_ImageView.setImageBitmap(getRoundedShape(myBitmap));
                }else{
                    holder.m_ImageView.setVisibility(View.VISIBLE);
                    holder.m_ImageView.setImageResource(mData.imageView);
                }
            }else{
                holder.m_ImageView.setVisibility(View.VISIBLE);
                holder.m_ImageView.setImageResource(mData.imageView);
            }
        }else{
            holder.m_ImageView.setVisibility(View.GONE);
        }
        holder.m_TextView.setText(mData.centerTextView);

        return convertView;
    }

    private class CustomHolder {
        TextView    m_TextView;
        ImageView   m_ImageView;
    }

    public void addItem(int icon, String _msg) {
        ListData addInfo = null;
        addInfo = new ListData();
        addInfo.imageView = icon;
        addInfo.centerTextView = _msg;
        m_List.add(addInfo);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 120;
        int targetHeight = 120;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}
