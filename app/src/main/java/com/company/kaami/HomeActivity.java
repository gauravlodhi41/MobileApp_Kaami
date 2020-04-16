package com.company.kaami;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView name, email, usermessage;
    Button logout, verify;
    FirebaseUser user;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    private static final String TAG = "Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        verify = findViewById(R.id.verifybutton);
        name = findViewById(R.id.name);
        usermessage = findViewById(R.id.userverifymessage);

        fstore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        //userID = mFirebaseAuth.getCurrentUser().getUid();
        email = (TextView) findViewById(R.id.homemailid);

        //To print the value of email fetching from user's datbase from firebase-----------------------------
        /*DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                Log.d(TAG,"code"+documentSnapshot.getString("email"));
                email.setText(documentSnapshot.getString("email"));
            }
        });*/

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //For checking Email Verification add action to verify buttton ------------------------------------------------------------
        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "SET ACTION, Check Code", Toast.LENGTH_SHORT).show();
                /*Log.d(TAG, "onSuccess: user profile is created for " + user);
                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, EmailVerification.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });

        //Checking Email Verification-------------------------------------
        /*if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
            usermessage.setText("You have verified your E-mail");
        } else {
            usermessage.setText("You have not verified your E-mail, please verify.");
        }*/

        //----------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
