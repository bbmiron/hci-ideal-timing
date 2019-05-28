package com.app.android.ideatapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.ideatapp.helpers.DatabaseManager;
import com.app.android.ideatapp.helpers.Utils;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;
import com.app.android.ideatapp.home.models.ItemModel;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.android.ideatapp.WritePostActivity.FOR_FB;
import static com.app.android.ideatapp.WritePostActivity.OPEN_RECOMMENDED_SCREN_REQ_CODE;

public class UploadPhotoActivity extends AppCompatActivity {

    private static final String FOR_FB = "facebook";
    private final int SELECT_PHOTO = 3;
    private TextView profileName;
    private ImageView profileImage;
    private ImageView photo;
    private EditText photoPath;
    private ItemModel model;
    private FloatingActionButton sendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo_activity);
        init();
        getFacebookInfo();
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browsePhoto();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openRecommendedScreen();
            }
        });
    }

    private void browsePhoto() {
        if (Utils.checkPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        } else {
            ActivityCompat.requestPermissions(UploadPhotoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PHOTO);
        }

    }

    private void init() {
        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);
        photo = findViewById(R.id.photo_to_send);
        photoPath = findViewById(R.id.attachmentData);
        sendButton = findViewById(R.id.send_button);
    }

    private void getFacebookInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                MainActivity.ACCESS_TOKEN,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String profileURL = "";
                            if (Profile.getCurrentProfile() != null) {
                                profileURL = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 400, 400).toString();
                            }

                            profileName.setText(firstName + " " + lastName);
                            Picasso.get().load(profileURL).into(profileImage);

                        } catch (JSONException e) {
                            Toast.makeText(UploadPhotoActivity.this, R.string.error_occurred_try_again, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_PHOTO:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    photoPath.setText(getPathFromURI(imageUri));
                    Picasso.get().load(imageUri).into(photo);
                }
                break;
            case OPEN_RECOMMENDED_SCREN_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    String date = data.getStringExtra(RecommendedTimeScreen.DATE);
                    String time = data.getStringExtra(RecommendedTimeScreen.TIME);
                    model = new ItemModel("Upload photo", date, time, "FACEBOOK");
                    model.setId(DatabaseManager.getInstance(this).addNewTask(model));
                    setResult(RESULT_OK, new Intent());
                    this.finish();
                }
                break;
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, "", null, "");
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    private void openRecommendedScreen() {
        Intent intent = new Intent(this, RecommendedTimeScreen.class);
        Bundle bundle = new Bundle();
        bundle.putInt(FOR_FB, 1);
        bundle.putString(SendEmailActivity.DATE, "30-05-2019");
        bundle.putString(SendEmailActivity.TIME, "13:07");
        intent.putExtras(bundle);
        startActivityForResult(intent,OPEN_RECOMMENDED_SCREN_REQ_CODE);
    }

}
