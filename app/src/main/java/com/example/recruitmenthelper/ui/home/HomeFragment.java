package com.example.recruitmenthelper.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.recruitmenthelper.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView homeText;
    ImageView homeImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeText = rootView.findViewById(R.id.homeText);
        homeImage = rootView.findViewById(R.id.homeImage);

        return rootView;
    }
}