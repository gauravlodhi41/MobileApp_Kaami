package com.company.kaami.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.kaami.R;
import com.company.kaami.fragments.LoginFragment;
import com.company.kaami.fragments.MainFragment;
import com.company.kaami.fragments.RegisterUserFragment;
import com.company.kaami.fragments.UserFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager manager = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) manager.findFragmentById(R.id.main_container);

        if(fragment==null){
            fragment = MainFragment.newInstance("donkey" , " main fragmnryLoaded succesfully");
            manager.beginTransaction()
                    .add(R.id.main_container, fragment).addToBackStack("main frag").commit();
        }


    }

    //////////////////////////////////////////////////////////////////////////////////

    public  void loadRegisterForm(){
        Log.d("donkey", "loading page for.. registration");

        RegisterUserFragment registerFragment = new RegisterUserFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_container, registerFragment)
                .addToBackStack(null).commit();

    }

    public  void loadLoginForm(){
        Log.d("donkey", "loading page for.. login");
        LoginFragment loginFragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, loginFragment).addToBackStack("second frag").commit();

    }

    public void loadUserForm(){

        Log.d("donkey", "loading page for.. login");

        UserFragment userFragment = new UserFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, userFragment)
                .addToBackStack(null).commit();
    }

}
