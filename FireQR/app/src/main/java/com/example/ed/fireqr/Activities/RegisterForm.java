package com.example.ed.fireqr.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ed.fireqr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterForm extends AppCompatActivity implements View.OnClickListener {

    EditText mName, mEmail, mPassword;
    Button mRegister;
    ProgressBar mProgress;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String mUsername, mUserEmail, mUserPass;
    String mMessage = "This cannot be empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        viewInit();

        mRegister.setOnClickListener(this);
    }

    private void viewInit() {
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mProgress = findViewById(R.id.register_progress);

        mName = findViewById(R.id.register_name);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_pass);
        mRegister = findViewById(R.id.register_btn);
    }

    // Here we are doing a switch based on the id's of each widget, and we determine the respective action
    @Override
    public void onClick(View v) {
       int id = v.getId();
        switch (id) {
            case R.id.register_btn:

                //When we grabbed variables store in TextBoxes, to be able to check if those variables are null
                mUsername = mName.getText().toString().trim();
                mUserEmail = mEmail.getText().toString().trim();
                mUserPass = mPassword.getText().toString().trim();


                if (mUsername.isEmpty()) {
                    mName.setError("You need a name");
                    return;
                }


                if (mUserEmail.isEmpty()){
                    mEmail.setError(mMessage);
                    return;
                }

                if (mUserPass.isEmpty()){
                    mPassword.setError(mMessage);
                    return;
                }

                mProgress.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(mUserEmail, mUserPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mUsername).build();

                            if (user != null) {
                                user.updateProfile(profileUpdates);
                                Map<String, String> userMap = new HashMap<>();

                                userMap.put("name", mUsername);
                                userMap.put("email", mUserEmail);

                                mDatabase.child("Users").child(user.getUid()).setValue(userMap);
                            }

                            mName.setVisibility(View.GONE);
                            mEmail.setVisibility(View.GONE);
                            mPassword.setVisibility(View.GONE);
                            mRegister.setVisibility(View.GONE);
                            mProgress.setVisibility(View.GONE);

                            Intent sentData = new Intent(RegisterForm.this, MainActivity.class);

                            sentData.putExtra("name",mUsername);
                            sentData.putExtra("email", mUserEmail);
                            sentData.putExtra("pass", mUserPass);
                            setResult(RESULT_OK, sentData);
                            finish();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.setVisibility(View.GONE);
                        Toast.makeText(RegisterForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}