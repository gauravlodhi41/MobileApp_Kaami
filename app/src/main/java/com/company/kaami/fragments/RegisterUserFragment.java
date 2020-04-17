package com.company.kaami.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.company.kaami.R;
import com.company.kaami.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterUserFragment extends Fragment {

    static final String CHAT_PREFS= "chatprefs";
    static final String DISPLAY_NAME = "username";



    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUserName;
    private EditText mConfirmPasswordView;
    private EditText mPassword;
    private Button mRegistration;

    private FirebaseAuth mAuth;

    public RegisterUserFragment() {

    }



    public static RegisterUserFragment newInstance(String param1, String param2) {
        RegisterUserFragment fragment = new RegisterUserFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);
        Log.d("donkey", "Attemptin____in register form.. registration");

        mEmailView = (AutoCompleteTextView)view.findViewById(R.id.register_email);
        mPassword = view.findViewById(R.id.register_password);
        mConfirmPasswordView = view.findViewById(R.id.register_confirm_password);
        mUserName = view.findViewById(R.id.register_username);

        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId== R.integer.register_form_finished || actionId == EditorInfo.IME_NULL){
                    attempRegistration();
                    return true;
                }
                return false;
            }
        });

        mRegistration = view.findViewById(R.id.register_sign_up_button);
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempRegistration();
            }
        });
        mAuth = FirebaseAuth.getInstance();




        return view;
    }

    private void attempRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        Log.d("donkey", "TextUtils.isEmpty(password): " + TextUtils.isEmpty(password));
        Log.d("donkey", "TextUtils.isEmpty(password) && !isPasswordValid(password): " + (TextUtils.isEmpty(password) && !isPasswordValid(password)));


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            Log.d("donkey", "Password Invalid");
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }

    private void createFirebaseUser(){
        String email = mEmailView.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("donkey", "createUser onComplete: " + task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.d("donkey" , "not Successfull"+task.getException());
                    showErrorDialoge("Registration Failed");
                } else {
                    saveDisplayName();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.loadLoginForm();

                }
            }
        });
    }

    private void saveDisplayName(){
        String displayName = mUserName.getText().toString();
        SharedPreferences prefs = this.getActivity().getSharedPreferences("CHAT_PREFS" , 0);
        prefs.edit().putString(DISPLAY_NAME, displayName).apply();
    }

    private void showErrorDialoge(String message){


        Log.d("donkey" , message);
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password
        String confirmPassword = mConfirmPasswordView.getText().toString();
        if(confirmPassword.equals(password))
            Log.d("donkey" , "confirm fail");
        if(password.length()>6)
            Log.d("donkey" , "length fail");
        return confirmPassword.equals(password) && password.length() > 6;
    }

}
