package com.example.jack_rambo.doanalbumanh;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public SingleAlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        SingleAlbumViewHolder holder = null;
        if (convertView == null){
            holder = new SingleAlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.single_album_row, parent, false);
            holder.imgPhoto = convertView.findViewById(R.id.imgAlbum);

            convertView.setTag(holder);
        }
        else
        {
            holder = (SingleAlbumViewHolder) convertView.getTag();
        }
        holder.imgPhoto.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();

        song = data.get(position);
        try {
            Glide.with(activity).load(new File(song.get(Function.KEY_PATH))).into(holder.imgPhoto);
        }catch (Exception e){}

        return convertView;
    }
}

class SingleAlbumViewHolder
{
    ImageView imgPhoto;
}
