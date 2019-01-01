package com.gmail.restujulian07.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //merupakan obj dgn tipe data ImageView
    private ImageView hasilFoto;
    //obj utk mengambil lokasi dari file
    private Uri selectedPhotoPath;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mengakses activity main
        setContentView(R.layout.activity_main);

        hasilFoto = (ImageView) findViewById(R.id.image_hasil);
    }

    @Override
    public void onClick(View view) {
        ambilFoto();
    }

    //Program intinya
    private void ambilFoto(){
        //TODO 1: Membuat Intent
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //TODO 2: Menyiapkan semua yg terkait dengan file
        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(imagePath, "default_image.jpg");
        if (newFile.exists()) {
            newFile.delete();
        } else {
            newFile.getParentFile().mkdirs();
        }
        selectedPhotoPath = getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        //TODO 3: Ketika mendapat data dari kamera
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            ClipData clip = ClipData.newUri(getContentResolver(), "A photo", selectedPhotoPath);
            captureIntent.setClipData(clip);
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            setImageViewWithImage();
        }
    }

    //TODO: Menampilkan foto yang didapatkan ke ImageView
    private void setImageViewWithImage() {
        hasilFoto.post(new Runnable() {
            @Override
            public void run() {
                Bitmap pictureBitmap = BitmapResizer.shrinkBitmap(
                        MainActivity.this,
                        selectedPhotoPath,
                        hasilFoto.getWidth(),
                        hasilFoto.getHeight()
                );
                hasilFoto.setImageBitmap(pictureBitmap);
            }
        });
    }
}
