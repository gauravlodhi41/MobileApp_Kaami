package com.company.kaami.activity.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.company.kaami.R;
import com.company.kaami.database.ProfileFeatures;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("profile");
    private FirebaseUser user;

    private ProfileFeatures profileFeatures;

    private ProfileViewModel profileViewModel;
    private LinearLayout textLinearView;
    private ScrollView editLinearText;
    private Button edittextbt,updatetextbt;
    private EditText adhaaredittext,nameedittext,phonenoedittext,educationedittext,skillsedittext,experienceedittext,addressedittext,emailedittext,passwordedittext;
    private TextView adhaartext,nametext,phonenotext,educationtext,skillstext,experiencetext,addresstext,emailtext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        user = FirebaseAuth.getInstance().getCurrentUser();

        textLinearView=root.findViewById(R.id.linearlayouttext);
        editLinearText=root.findViewById(R.id.linearlayoutedit);
        adhaartext=root.findViewById(R.id.adhaartext);
        nametext=root.findViewById(R.id.nameid);
        phonenotext=root.findViewById(R.id.phonenoid);
        educationtext=root.findViewById(R.id.educationid);
        skillstext=root.findViewById(R.id.skillsid);
        experiencetext=root.findViewById(R.id.experienceid);
        addresstext=root.findViewById(R.id.addressid);
        emailtext=root.findViewById(R.id.emailid);

        adhaaredittext=root.findViewById(R.id.adhaaredittext);
        nameedittext=root.findViewById(R.id.nameeditid);
        phonenoedittext=root.findViewById(R.id.phonenoeditid);
        educationedittext=root.findViewById(R.id.educationeditid);
        skillsedittext=root.findViewById(R.id.skillseditid);
        experienceedittext=root.findViewById(R.id.experienceeditid);
        addressedittext=root.findViewById(R.id.addresseditid);
        emailedittext=root.findViewById(R.id.emaileditid);
        passwordedittext=root.findViewById(R.id.passwordeditid);

        updatetextbt=root.findViewById(R.id.updatedid);
        edittextbt=root.findViewById(R.id.editprofilebtid);

        updatetextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirebase();
                textLinearView.setVisibility(View.VISIBLE);
                editLinearText.setVisibility(View.INVISIBLE);
            }
        });

        edittextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLinearView.setVisibility(View.INVISIBLE);
                editLinearText.setVisibility(View.VISIBLE);
            }
        });

        textLinearView.setVisibility(View.VISIBLE);
        editLinearText.setVisibility(View.INVISIBLE);

        getFromFirebase();
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });
        return root;
    }

    private void getFromFirebase() {
        myRef.child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileFeatures=dataSnapshot.getValue(ProfileFeatures.class);
                inflateText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateFirebase(){
        String aadhar=adhaaredittext.getText().toString();
        String name=nameedittext.getText().toString();
        String phone=phonenoedittext.getText().toString();
        String education=educationedittext.getText().toString();
        String skills=skillsedittext.getText().toString();
        String experience=experienceedittext.getText().toString();
        String address=addressedittext.getText().toString();
        String email=emailedittext.getText().toString();
        String password=passwordedittext.getText().toString();
        if(aadhar.isEmpty() || name.isEmpty() || phone.isEmpty() || education.isEmpty() || skills.isEmpty() || experience.isEmpty() ||
            address.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(getContext(),"Please Fill All Details.",Toast.LENGTH_LONG).show();
        }
        else {
            profileFeatures = new ProfileFeatures(aadhar, name, phone, education, skills, experience, address, email, password);
            myRef.child(user.getPhoneNumber()).setValue(profileFeatures);
        }
    }

    private void inflateText(){
        if(profileFeatures!=null) {
            adhaartext.setText(profileFeatures.getAadharno());
            nametext.setText(profileFeatures.getName());
            phonenotext.setText(profileFeatures.getPhoneno());
            educationtext.setText(profileFeatures.getEducation());
            skillstext.setText(profileFeatures.getSkills());
            experiencetext.setText(profileFeatures.getExperience());
            addresstext.setText(profileFeatures.getAddress());
            emailtext.setText(profileFeatures.getEmail());
        }
    }
}
