package com.app.android.ideatapp.home.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.android.ideatapp.MainActivity;
import com.app.android.ideatapp.R;
import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.UploadPhotoActivity;
import com.app.android.ideatapp.WritePostActivity;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;

public class HomeFragment extends Fragment {
    public static final int SEND_EMAIL_REQ_CODE = 10001;
    public static final int WRITE_POST_REQ_CODE = 10005;
    public static final int UPLOAD_PHOTO_REQ_CODE = 1006;
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
                if (MainActivity.ACCESS_TOKEN != null) {
                    getActivity().startActivityForResult(new Intent(getContext(), WritePostActivity.class), WRITE_POST_REQ_CODE);
                } else {
                    createAlertDialog();
                }
            }
        });

        final String email = this.getArguments().getString(MainActivity.EMAIL_TAG);
        final String name = this.getArguments().getString(MainActivity.NAME_TAG);

        sendEmailButton = layoutInflater.findViewById(R.id.send_email_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email != null) {
                    Intent intent = new Intent(getContext(), SendEmailActivity.class);
                    intent.putExtra(MainActivity.EMAIL_TAG, email);
                    intent.putExtra(MainActivity.NAME_TAG, name);
                    getActivity().startActivityForResult(intent, SEND_EMAIL_REQ_CODE);
                } else {
                    createAlertDialogForGmail();
                }
            }
        });

        uploadPhotoButton = layoutInflater.findViewById(R.id.upload_photo_button);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ACCESS_TOKEN != null) {
                    getActivity().startActivityForResult(new Intent(getContext(), UploadPhotoActivity.class), UPLOAD_PHOTO_REQ_CODE);
                } else {
                    //createAlertDialog();
                    getActivity().startActivityForResult(new Intent(getContext(), RecommendedTimeScreen.class), UPLOAD_PHOTO_REQ_CODE);
                }
            }
        });

    }

    private void createAlertDialogForGmail() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Info")
                .setMessage("You should be logged in with a Google Account")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void createAlertDialog() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Info")
                .setMessage("You should be logged in with Facebook")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
