package com.example.jack_rambo.doanalbumanh;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AlbumAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        if (convertView == null){
            holder = new AlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.album_row, parent, false);
            holder.imgGallery = convertView.findViewById(R.id.imgGallery);
            holder.tvCount = convertView.findViewById(R.id.tvCount);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);

            convertView.setTag(holder);
        }
        else
        {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        holder.imgGallery.setId(position);
        holder.tvCount.setId(position);
        holder.tvTitle.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();

        song = data.get(position);
        try {
            holder.tvTitle.setText(song.get(Function.KEY_ALBUM));
            holder.tvCount.setText(song.get(Function.KEY_COUNT));

            Glide.with(activity).load(new File(song.get(Function.KEY_PATH))).into(holder.imgGallery);
        }catch (Exception e){}

        return convertView;
    }
}

class AlbumViewHolder{
    ImageView imgGallery;
    TextView tvCount, tvTitle;
}