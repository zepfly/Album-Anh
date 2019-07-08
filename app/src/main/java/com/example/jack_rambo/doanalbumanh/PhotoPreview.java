package com.example.jack_rambo.doanalbumanh;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhotoPreview extends AppCompatActivity{
    ImageView imgPhotoPreview;
    TextView tvTime, tvWxH, tvSize, tvPath, tvName;
    RelativeLayout layButton;
    String path, timestamp;
    ImageButton btnDetail, btnDelete, btnShare, btnCrop, btnRotate;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.photo_preview);

        layButton = findViewById(R.id.layButton);

        btnRotate = findViewById(R.id.btnRotate);
        btnShare = findViewById(R.id.btnShare);
        btnDetail = findViewById(R.id.btnDetail);
        btnDelete = findViewById(R.id.btnDelete);
        btnCrop = findViewById(R.id.btnCrop);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        timestamp = intent.getStringExtra("timestamp");

        imgPhotoPreview = findViewById(R.id.imgPhotoPreview);
        Glide.with(PhotoPreview.this).load(new File(path)).into(imgPhotoPreview);

        //onClickListener

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPhotoPreview.setRotation(imgPhotoPreview.getRotation() + 90);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=getLayoutInflater();
                View alertLayout =inflater.inflate(R.layout.detail_photo,null);
                final TextView tvTime = alertLayout.findViewById(R.id.tvTime);
                final TextView tvWxH = alertLayout.findViewById(R.id.tvWxH);
                final TextView tvSize = alertLayout.findViewById(R.id.tvSize);
                final TextView tvPath = alertLayout.findViewById(R.id.tvPath);
                final TextView tvName = alertLayout.findViewById(R.id.tvName);

                File file = new File(path);
                long length = file.length() / 1024;

                tvTime.setText(Function.convertToTime(file.lastModified()+""));
                tvName.setText("Tựa đề: " + file.getName());

                tvWxH.setText("Độ phân giải: " + imgPhotoPreview.getHeight() + "x"+ imgPhotoPreview.getWidth());
                tvSize.setText("Kích thước: "+ length+" KB");
                tvPath.setText("Đường dẫn: "+ path);

                AlertDialog.Builder alert = new AlertDialog.Builder(PhotoPreview.this);
                //alert.setTitle("Detail");
                alert.setView(alertLayout);
                alert.setCancelable(true);
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(path);
                CropImage.activity(Uri.fromFile(file))
                        .start(PhotoPreview.this);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpg");
                Uri uri = Uri.fromFile(new File(path));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());

                if (sharingIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("LINK :", resultUri.toString());
                imgPhotoPreview.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
