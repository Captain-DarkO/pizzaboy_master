package com.anurag.pizzaboy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaptureActivity extends AppCompatActivity {
    VisualRecognition service;
    Pizza mpizza;
    List<String> base, toppings, ingredients;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private Button captureButton, doneButton, btnTop, btnbase;
    private SharedPreferences sortpref;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            VisualClassification result = null;
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
            try {
                ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                        .images(pictureFile)
                        .build();
                result = service.classify(options).execute();
            } catch (Exception e) {
                Toast.makeText(CaptureActivity.this, "Error recognizing visual! ", Toast.LENGTH_SHORT).show();
            }
            int pref = sortpref.getInt("ingredient", 1);
            if (pref == 1) {
                //it is going to the base
                base.add(result.toString());
            } else {
                //it is going to the top
                toppings.add(result.toString());
            }
            Log.d("PizzaRec", "Added ingredient" + result.toString());
        }
    };
    private SharedPreferences.Editor sortprefEdit;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "PizzaBoy");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("PizzaBoy:", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        sortpref = getSharedPreferences("Ingredient", MODE_PRIVATE);
        sortprefEdit = sortpref.edit();
        sortprefEdit.putInt("ingredient", 1);
        captureButton = (Button) findViewById(R.id.button_capture);
        doneButton = (Button) findViewById(R.id.button_done);
        btnbase = (Button) findViewById(R.id.btn_base);
        btnTop = (Button) findViewById(R.id.btn_top);

        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("<api-key>");
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortprefEdit.putInt("ingredient", 1);
                sortprefEdit.apply();
                Toast.makeText(CaptureActivity.this, "Put the toppings.. ", Toast.LENGTH_SHORT).show();
            }
        });
        btnbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortprefEdit.putInt("ingredient", 2);
                sortprefEdit.apply();
                Toast.makeText(CaptureActivity.this, "Get the base ingredients ", Toast.LENGTH_SHORT).show();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaptureActivity.this, OrderActivity.class);
                CaptureActivity.this.startActivity(intent);
            }
        });

    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
        }
        return camera;
    }
}
