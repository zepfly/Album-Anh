package com.example.jack_rambo.doanalbumanh;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AlbumActivity extends AppCompatActivity{
    GridView gvAlbum;
    ArrayList<HashMap<String, String>> imgList = new ArrayList<HashMap<String, String>>();
    String albumName = "";
    LoadAlbumImages loadAlbumTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Intent intent = getIntent();
        albumName = intent.getStringExtra("name");
        setTitle(albumName);

        gvAlbum = findViewById(R.id.gvAlbum);

        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();
    }

    class LoadAlbumImages extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            String xml = "";

            String path = null;
            String album = null;
            String timestamp = null;

            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_MODIFIED};
            Cursor cursorExternal = getContentResolver().query(uriExternal, projection,
                    "bucket_display_name = \""+albumName+"\"", null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection,
                    "bucket_display_name = \""+albumName+"\"",null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});

            while (cursor.moveToNext())
            {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));

                imgList.add(Function.mappingInbox(album, path, timestamp, Function.convertToTime(timestamp), null));
            }
            cursor.close();
            Collections.sort(imgList, new MapComparator(Function.KEY_TIMESTAMP, "dsc"));

            return xml;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SingleAlbumAdapter adapter = new SingleAlbumAdapter(AlbumActivity.this, imgList);
            gvAlbum.setAdapter(adapter);
            gvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AlbumActivity.this, PhotoPreview.class);
                    intent.putExtra("path", imgList.get(+position).get(Function.KEY_PATH));

                    startActivity(intent);
                }
            });
        }
    }
}
