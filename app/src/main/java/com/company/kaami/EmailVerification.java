package com.company.kaami;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EmailVerification extends AppCompatActivity {

    Button email;
    TextView emailtext, resend, login;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    FirebaseUser user;
    private static final String TAG = "Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        resend = (TextView) findViewById(R.id.resendlink);
        login = (TextView) findViewById(R.id.loginlink);

        //Firebase---------------------------------
        fstore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();
        emailtext = (TextView) findViewById(R.id.emailtext);

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                emailtext.setText("To confirm your email address, please tap on the button in the email we sent to " + documentSnapshot.getString("email"));
            }
        });

        email = (Button) findViewById(R.id.openemail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = mFirebaseAuth.getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerification.this, "Email sent successfully, please check your mail.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EmailVerification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //checking verification
        if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
            Toast.makeText(EmailVerification.this, "Verified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmailVerification.this, HomeActivity.class));
        } else {
            Toast.makeText(EmailVerification.this, "Please verify your email address.", Toast.LENGTH_SHORT).show();
        }

        login.setOnClickListener(new View.OnClickListener() {
            Intent i = getIntent();
            String em = i.getStringExtra("email");
            String pass = i.getStringExtra("password");

            @Override
            public void onClick(View view) {
                mFirebaseAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(EmailVerification.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(EmailVerification.this, "Please verify your email address.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EmailVerification.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmailVerification.this, SignUpActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
    }
}
