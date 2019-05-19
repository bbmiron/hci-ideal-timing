package com.app.android.ideatapp.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.android.ideatapp.MainActivity;
import com.app.android.ideatapp.R;
import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.WritePostActivity;

public class HomeFragment extends Fragment {

    private Button writePostButton;
    private Button uploadPhotoButton;
    private Button sendEmailButton;
    private View layoutInflater;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutInflater = inflater.inflate(R.layout.home_fragment, container, false);
        return layoutInflater;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        writePostButton = layoutInflater.findViewById(R.id.write_post_button);
        writePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WritePostActivity.class));
            }
        });

        final String email = this.getArguments().getString(MainActivity.EMAIL_TAG);
        final String name = this.getArguments().getString(MainActivity.NAME_TAG);

        sendEmailButton = layoutInflater.findViewById(R.id.send_email_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SendEmailActivity.class);
                intent.putExtra(MainActivity.EMAIL_TAG, email);
                intent.putExtra(MainActivity.NAME_TAG, name);
                startActivity(intent);
            }
        });

    }
}
