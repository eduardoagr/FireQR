package com.example.ed.fireqr.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ed.fireqr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail;
    Button mForgotPass;
    FirebaseAuth mAuth;
    String mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        viewInit();

        mForgotPass.setOnClickListener(this);
    }

    private void viewInit() {
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.forgot_email);
        mForgotPass = findViewById(R.id.forgot_password_button);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.forgot_password_button:

                mUserEmail = mEmail.getText().toString().trim();

                if (mUserEmail.isEmpty()){
                    mEmail.setError("Provide an email");
                    return;
                }

                mAuth.sendPasswordResetEmail(mUserEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(ForgotPassword.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });
        }
    }
}
