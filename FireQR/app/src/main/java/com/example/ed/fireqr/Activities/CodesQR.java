package com.example.ed.fireqr.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ed.fireqr.Adapters.MyAdapter;
import com.example.ed.fireqr.Class.QR;
import com.example.ed.fireqr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CodesQR extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter adapter;
    FirebaseUser mUser;
    DatabaseReference mReef;
    ArrayList<QR> qrArrayList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_qr);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mReef = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView = findViewById(R.id.codesQR_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReef.child(mUser.getUid()).child("QR").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qrArrayList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren() ) {

                    QR qr = snapshot.getValue(QR.class);
                    if (qrArrayList.contains(qr)) {
                        qrArrayList.add(qr);
                    }
                }

                adapter = new MyAdapter(CodesQR.this, qrArrayList);

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}