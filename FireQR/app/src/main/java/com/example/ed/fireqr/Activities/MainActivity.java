package com.example.ed.fireqr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ed.fireqr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // This is the variable that we are going to use, in the 'Intent' for receiving the variables back from the 'Register' Activity
    final int REGISTER_ACTIVITY_REQUEST_CODE = 0;

    String mMessage = "This cannot be empty";
    String mUserPassword, mUserEmail, mUserName;

    // Android widget
    EditText mEmail, mPassword;
    Button mLogin;
    TextView mForgotPassword, mCreateAccount;

    // Firebase variables
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());

        mUser = FirebaseAuth.getInstance().getCurrentUser();

   if (mUser != null){
      startActivity(new Intent(this, ScanSurface.class));
       finish();
   }

        viewInit();

        // define the click listener for each button
        mLogin.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
    }

    private void viewInit() {
        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.register_name);
        mPassword = findViewById(R.id.main_pass);
        mLogin = findViewById(R.id.main_login);
        mForgotPassword = findViewById(R.id.main_forgot_password);
        mCreateAccount = findViewById(R.id.main_create_account);
    }

    // Here we are doing a switch based on the id's of each widget, and we determine the respective action
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_login:

                // When we grabbed variables from the 'onActivityResult', we store that data in variables, to be able to check if those variables are null
                mUserEmail = mEmail.getText().toString().trim();
                mUserPassword = mPassword.getText().toString();

                if (mUserEmail.isEmpty()) {
                    mEmail.setError(mMessage);
                    return;
                }

                if (mUserPassword.isEmpty()) {
                    mPassword.setError(mMessage);
                    return;
                }

                // Firebase method to sign the user
                mAuth.signInWithEmailAndPassword(mUserEmail, mUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If everything is a success, we log the user an send him to the home of the app
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, Home.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.main_forgot_password:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;
            case R.id.main_create_account:
                startActivityForResult(new Intent(MainActivity.this, RegisterForm.class), REGISTER_ACTIVITY_REQUEST_CODE);
                break;
            default:
                Toast.makeText(this, "This should never happen", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Method for getting the values back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Here we are a couple of things:

           1 - We have to make sure that the 'requestCode' is the same as the code that we stated before above, this is usually an integer.
           In our particular case the variable is 'REGISTER_ACTIVITY_REQUEST_CODE'. Is important to name the variable that make seance. For example:
           this variable will bring variables back fro the 'Register' Activity
           2 - We have to see if the result is ok, so we can proceed
           3 - Just as precaution, We check if tha 'data' that we receive from the intent has any data.

           NOTE: the step 3, is not necessary but is a good practice

           4 - Once we receive the data, we put the data in the appropriate TextBox
         */
        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mUserName = data.getStringExtra("name");
                    mUserEmail = data.getStringExtra("email");
                    mEmail.setText(mUserEmail);
                    mUserPassword = data.getStringExtra("pass");
                    mPassword.setText(mUserPassword);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}

