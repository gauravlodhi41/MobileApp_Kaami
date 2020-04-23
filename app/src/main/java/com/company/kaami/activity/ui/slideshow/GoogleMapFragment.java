package com.company.kaami.activity.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.company.kaami.R;


public class GoogleMapFragment extends Fragment {

    private GoogleMapViewModel googleMapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        googleMapViewModel =
                ViewModelProviders.of(this).get(GoogleMapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        googleMapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
