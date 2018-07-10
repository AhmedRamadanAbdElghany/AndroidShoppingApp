package com.ub.akshay.nitkart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SupermarketAdapter extends BaseAdapter {
    public String title[];
    public String place[];
    public int shopPic[];
    public int ratePic[];
    public Activity context;
    public LayoutInflater inflater;
    public SupermarketAdapter(Activity context, String[] title, String[] description, int[] flag, int[] flag2) {
        super();
        this.shopPic = flag;
        this.ratePic = flag2;
        this.context = context;
        this.title = title;
        this.place = description;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    public static class ViewHolder {
        ImageView imgViewLogo;
        ImageView rateLogo;
        TextView Title;
        TextView place;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.supermarketlist, null);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.supermarketpic);
            holder.rateLogo = (ImageView) convertView.findViewById(R.id.rating_image);
            holder.Title = (TextView) convertView.findViewById(R.id.name);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.imgViewLogo.setImageResource(shopPic[position]);
        holder.rateLogo.setImageResource(ratePic[position]);
        holder.Title.setText(title[position]);
        holder.place.setText(place[position]);
        return convertView;
    }
}