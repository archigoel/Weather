package com.app.weatherforecast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


public class SignupFragment extends DialogFragment {

    private static final String TAG = "SignupDialogFragment";
    private static final String PREFS_NAME = "UserAutheticationFile";
    private SignupFragment activity;
    private LoginActivity loginActivity;
    private ProgressDialog loadingDialog;
    private Context context;
    EditText name;
    EditText email;
    EditText password;
    TextView login;
    SharedPreferences settings;


    public SignupFragment (){}



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.fragment_signup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);
        builder.setCancelable(false);

        activity = this;
        context = this.getActivity();

        name = (EditText) rootView.findViewById(R.id.input_name);
        email = (EditText) rootView.findViewById(R.id.input_email);
        password = (EditText) rootView.findViewById(R.id.input_password);
        login= (TextView) rootView.findViewById(R.id.link_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        Button createAccount = (Button) rootView.findViewById(R.id.btn_signup);

        final SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCanceledOnTouchOutside(true);



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = true;

                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (userName.isEmpty() || userName.length() < 3) {
                    name.setError("at least 3 characters");
                    valid = false;
                } else if (userPassword.isEmpty() || userPassword.length() < 4) {
                    password.setError("Length is too short");
                    valid = false;
                } else if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    email.setError("enter a valid email address");
                    valid = false;
                } else if (!ifEmailExists(userEmail)) {
                    email.setError("email already exists");
                    valid = false;


                }
                else {


//                    loadingDialog = new ProgressDialog(loginActivity);
//                    loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    loadingDialog.setCancelable(false);
//                    loadingDialog.setMessage("Creating Account...");
//                    loadingDialog.show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_email", userEmail);
                    editor.putString("user_name", userName);
                    editor.putString("user_password", userPassword);
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();

                }
            }

        });


       return dialog;
    }

    private boolean ifEmailExists(String email){
        final SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
//
        final Map<String, ?> value = preferences.getAll();

        boolean valid = true;
        for (final Map.Entry<String, ?> entry : value.entrySet())
        {
//            ArrayList<String> list = new ArrayList<String>() {{
//                add(entry.getValue().toString());
//            }};

            if (email.equals(entry.getValue().toString())) {
                valid = false;
                break;

            }
            else
            {
                continue;
            }

        }
        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}






