package com.example.ed.fireqr.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ed.fireqr.Class.QR;
import com.example.ed.fireqr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    String pk, incomingData;
    ArrayList<String> data = new ArrayList<>();

    TextView mUserNameTV, mNameOfCard, mName, mOrg, mEmail, mUrl, mTel, mCell, mAddress;
    ImageView mScan;
    Button mViewAllCodes ,mSubmit;
    FirebaseUser mUser;
    DatabaseReference mReef;
    String nameOfCard, name, org, email, url, cell, tell, address;

    @SuppressLint("SetTextI18n") 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cameraPermission();
        viewInit();

        if (mUser != null) {
            mUserNameTV.setText("Welcome: " + mUser.getDisplayName());
        }
    }

    private void cameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
    }

    private void viewInit() {

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mReef = FirebaseDatabase.getInstance().getReference("Users");

        mSubmit = findViewById(R.id.home_summit);
        mUserNameTV = findViewById(R.id.home_userName);
        mScan = findViewById(R.id.home_cameraScan);
        mNameOfCard = findViewById(R.id.home_businessCard);
        mName = findViewById(R.id.home_name);
        mOrg = findViewById(R.id.home_org);
        mEmail = findViewById(R.id.home_email);
        mUrl = findViewById(R.id.home_url);
        mTel = findViewById(R.id.home_tel);
        mCell = findViewById(R.id.home_cell);
        mAddress = findViewById(R.id.home_adr);
        mViewAllCodes = findViewById(R.id.home_allCodes);

        mSubmit.setOnClickListener(this);
        mViewAllCodes.setOnClickListener(this);

        if (getIntent().getStringExtra("data") != null) {
            incomingData = getIntent().getStringExtra("data");
            String[] stringArray = incomingData.split("\n");

            data.addAll(Arrays.asList(stringArray));

            grabValues();
        }
    }

    @SuppressLint("SetTextI18n")
    private void grabValues() {
        nameOfCard = data.get(2).split(";")[0].replace("N:", "");
        name = data.get(2).split(";")[1];
        org = data.get(3).replace("ORG:", "");
        email = data.get(4).replace("EMAIL;TYPE=INTERNET:", "");
        url = data.get(5).replace("URL:", "");
        cell = data.get(6).replace("TEL;TYPE=CELL:", "");
        tell = data.get(7).replace("TEL:", "");
        address = data.get(8).replace("ADR:;;", "ADR:").replace("ADR:", "").replace(";", " ");

        mNameOfCard.setText("name of the card: " + nameOfCard);
        mName.setText("Name: " + name);
        mOrg.setText("Company: " + org);
        mEmail.setText("Email: " + email);
        mUrl.setText("Web: " + url);
        mTel.setText("Phone: " + tell);
        mCell.setText("Cell: " + cell);
        mAddress.setText("Address: " + address);

        Toast.makeText(Home.this, "The submit button has been enabled", Toast.LENGTH_SHORT).show();
        mSubmit.setEnabled(true);
    }

    public void scanQR(View view) {
        Intent scanView = new Intent(Home.this, ScanSurface.class);
        startActivity(scanView);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "We need permission to scan the QR", Toast.LENGTH_LONG).show();
            }

        }
    }//end onRequestPermissionsResult

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.home_summit:
                QR qr = new QR(nameOfCard, name, org, email, url, cell, tell, address);

                pk = mReef.push().getKey();

                mReef.child(mUser.getUid()).child("QR").child(pk).setValue(qr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){ {

                            Toast.makeText(Home.this, "Stored", Toast.LENGTH_SHORT).show();

                            mNameOfCard.setText("");
                            mName.setText("");
                            mOrg.setText("");
                            mEmail.setText("");
                            mUrl.setText("");
                            mTel.setText("");
                            mCell.setText("");
                            mAddress.setText("");

                            Toast.makeText(Home.this, "The submit button has been disabled", Toast.LENGTH_SHORT).show();
                            mSubmit.setEnabled(false);



                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.home_allCodes:
                startActivity(new Intent(Home.this, CodesQR.class));
                break;
        }
    }
}
