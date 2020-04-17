package com.company.kaami.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.company.kaami.R;
import com.company.kaami.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {


    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPassword;
    private Button mLogIn;


    public LoginFragment() {

    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailView = view.findViewById(R.id.login_email);
        mPassword = view.findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== R.integer.login || actionId== EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLogIn= view.findViewById(R.id.login_sign_in_button);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("donkey", "Attempting_login.. ");
                attemptLogin();
            }
        });
        return view;
    }

    private void attemptLogin(){
        String email = mEmailView.getText().toString();
        String password = mPassword.getText().toString();

        if(email.equals("") || password.equals("")) return;

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("donkey", "signInWithEmail() onComplete: " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("donkey", "Problem signing in: " + task.getException());
                    showErrorDialog(task.getException().getMessage().toString());
                } else {

                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.loadUserForm();
                }
            }
        });

    }

    private void showErrorDialog(String message) {
        Log.d("donkey", message);
        Toast.makeText(getActivity(),message , Toast.LENGTH_SHORT).show();

    }
}
