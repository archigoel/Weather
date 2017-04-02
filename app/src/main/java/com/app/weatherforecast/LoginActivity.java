package com.app.weatherforecast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {

    private static final String PREFS_NAME = "UserAutheticationFile";
    Button login_button,register_button;
    EditText ed1,ed2;
    int counter = 3;
    public SignupFragment signupFragment;
    private LoginActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = (Button) findViewById(R.id.login_button);
        ed1 = (EditText) findViewById(R.id.editEmail);
        ed2 = (EditText) findViewById(R.id.editPassword);

        register_button = (Button) findViewById(R.id.register_button);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);


//        SharedPreferences.Editor editor = settings.edit();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ed1.getText().toString().equals("admin") &&
//                        ed2.getText().toString().equals("admin")) {
//                     progressDialog.setIndeterminate(true);
//                    progressDialog.setMessage("Authenticating...");
//                    progressDialog.show();
//
////                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
//                }
                attemptLogin();
//                new android.os.Handler().postDelayed(
//                        new Runnable() {
//                            public void run() {
//                                // On complete call either onLoginSuccess or onLoginFailed
////                                onLoginSuccess();
//                                // onLoginFailed();
//                                progressDialog.dismiss();
//                            }
//                        }, 3000);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupFragment = new SignupFragment();
                signupFragment.show(getFragmentManager(), "fragment");
            }
        });
    }


        private void attemptLogin() {

            // Reset errors.
            ed1.setError(null);
            ed2.setError(null);

            // Store values at the time of the login attempt.
            String email = ed1.getText().toString();
            String password = ed2.getText().toString();

            boolean cancel = false;
            View focusView = null;

            SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

            String user_email = preferences.getString("user_email", null);
            String user_password = preferences.getString("user_password", null);

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                ed2.setError(getString(R.string.error_invalid_password));
                focusView = ed2;
                cancel = true;
            }

            // Check for a valid email address.
            else if (TextUtils.isEmpty(email)) {
                ed1.setError(getString(R.string.error_field_required));
                focusView = ed1;
                cancel = true;
            }

            else if (!isEmailValid(email)) {
                ed1.setError(getString(R.string.error_invalid_email));
                focusView = ed1;
                cancel = true;
            }


            else if (email.equals(user_email) && (password.equals(user_password))){
                saveLoginDetails(email,password);
                startHomeActivity();
            }
            else{
                Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
            }



        }

        private void startHomeActivity() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        private void saveLoginDetails(String email, String password) {
            new PrefManager(this).saveLoginDetails(email, password);
        }


        private boolean isEmailValid(String email) {
            //TODO: Replace this with your own logic
            return email.contains("@");


        }


    }
