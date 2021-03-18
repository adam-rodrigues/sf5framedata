package com.example.sf5characterframedata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private String[] mImageNames;
    private TypedArray mImagePhotos;
    private int[][] mAttributes;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public CustomAdapter(String[] imageNames, TypedArray imagePhotos, Context context) {
        // Making all names uppercase for main menu
        for(int i = 0; i < imageNames.length; i++)
            imageNames[i] = imageNames[i].toUpperCase();

        this.mImageNames = imageNames;
        this.mImagePhotos = imagePhotos;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImagePhotos.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_items, parent, false);
        }

        String tempLowerCaseName = mImageNames[position].toLowerCase() + "_attributes"; // grabbing all resource id names
        tempLowerCaseName = tempLowerCaseName.replace(" ", ""); // removing all spaces
        tempLowerCaseName = tempLowerCaseName.replace("-", ""); // removing all hyphens
        int tempResourceId = mContext.getResources().getIdentifier(tempLowerCaseName, "array", mContext.getPackageName()); // storing resId
        TypedArray barImages = mContext.getResources().obtainTypedArray(tempResourceId); // retrieve all attribute bars used for a character

        // Assigning all layout resources to views
        TextView tvName = convertView.findViewById(R.id.character_label);
        ImageView imageView = convertView.findViewById(R.id.character_card_image);
        ImageView powerBarImage = convertView.findViewById(R.id.power_bars);
        ImageView healthBarImage = convertView.findViewById(R.id.health_bars);
        ImageView mobilityBarImage = convertView.findViewById(R.id.mobility_bars);
        ImageView techniqueBarImage = convertView.findViewById(R.id.technique_bars);
        ImageView rangeBarImage = convertView.findViewById(R.id.range_bars);

        // Assigning images to views in layout
        tvName.setText(mImageNames[position]);
        imageView.setImageResource(mImagePhotos.getResourceId(position, -1));
        powerBarImage.setImageResource(barImages.getResourceId(0, -1));
        healthBarImage.setImageResource(barImages.getResourceId(1, -1));
        mobilityBarImage.setImageResource(barImages.getResourceId(2, -1));
        techniqueBarImage.setImageResource(barImages.getResourceId(3, -1));
        rangeBarImage.setImageResource(barImages.getResourceId(4, -1));

        return convertView;
    }
}
