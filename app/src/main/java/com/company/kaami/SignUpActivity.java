package com.company.kaami;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, phone;
    TextView link;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID;
    private LoginButton loginbutton;
    Button signup;
    private static final String TAG = "Message";

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_sign_up);

        email = (EditText) findViewById(R.id.useremail);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phoneno);
        link = (TextView) findViewById(R.id.link);
        signup = (Button) findViewById(R.id.sign_up);

        //FACEBOOK AUTH-----------------------------------------------------------------------------
        //Checking FB Login status
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn == true) {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        }

        loginbutton = findViewById(R.id.signupfbbutton);
        callbackManager = CallbackManager.Factory.create();
        loginbutton.setReadPermissions("email");
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //FACEBOOK AUTH END-------------------------------------------------------------------------

        //Firebase--------------------------------------
        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        //Google Sign in button
        create();
        findViewById(R.id.signupsignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                final String number = phone.getText().toString();
                final String valemail = email.getText().toString().trim();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String numPattern = "^[+]?[0-9]{10,13}$";

                if (valemail.matches(emailPattern)) {
                } else {
                    email.setError("Email is Invalid");
                    email.requestFocus();
                    return;
                }

                if (number.matches(numPattern)) {
                } else {
                    phone.setError("Phone number is Invalid");
                    phone.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(em)) {
                    email.setError("This field can't be empty");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pass) && pass.length() < 6) {
                    password.setError("This field can't be empty");
                    password.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(number) || number.length() < 10) {
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }


                mFirebaseAuth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //Add this if you want to add Email Verification other than OTP after signup Activity---------------------------------------------
                            /*user = mFirebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this,"Registered successfully, please verify your email.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                            //---------------------------------------------------------------------------------------------------------------------------------

                            //Registering User to the database
                            Toast.makeText(SignUpActivity.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                            userID = mFirebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            final Map<String, Object> user = new HashMap<>();
                            user.put("email", em);
                            user.put("password", pass);
                            user.put("phoneno", number);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                }
                            });

                            //Add this if you want to start Email Verification--------------------------
                            /*Intent intent = new Intent(SignUpActivity.this, EmailVerification.class);
                            intent.putExtra("email",em);
                            intent.putExtra("password",pass);
                            startActivity(intent);*/
                            //--------------------------------------------------------------------------

                            //Calling Verify using OTP

                            String phonenumber = "+91" + number;
                            Intent intent = new Intent(SignUpActivity.this, VerifyPhone.class);
                            intent.putExtra("phonenumber", phonenumber);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void create() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "You pressed back button", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUpActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
