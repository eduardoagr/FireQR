package com.example.ed.fireqr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ed.fireqr.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanSurface extends AppCompatActivity {

    String intentData;
    Button mGoHome;
    SurfaceView mCameraSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_surface);

        viewInit();

        createCameraSource();

    }

    private void viewInit() {
        mCameraSurfaceView = findViewById(R.id.scanSurface_scan);
        mGoHome = findViewById(R.id.scanSurface_goHome);

        mGoHome.setEnabled(false);
    }

    private void createCameraSource() {

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920, 1024)
                .build();

        mCameraSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(ScanSurface.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(mCameraSurfaceView.getHolder());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            private static final String TAG = "eee" ;

            @Override
            public void release() { }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                SparseArray<Barcode> barcodeArray = detections.getDetectedItems();

                if (barcodeArray.size() != 0){

                    if (barcodeArray.valueAt(0) != null) {
                        mGoHome.setBackgroundResource(R.color.green);
                        mGoHome.setEnabled(true);
                        intentData = barcodeArray.valueAt(0).rawValue;
                    }
                }
            }
        });
    }

    public void goHome(View view) {
        startActivity(new Intent(ScanSurface.this, Home.class).putExtra("data", intentData));
        finish();
    }
}
