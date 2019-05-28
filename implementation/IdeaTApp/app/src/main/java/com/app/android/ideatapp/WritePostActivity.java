package com.app.android.ideatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.ideatapp.helpers.DatabaseManager;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;
import com.app.android.ideatapp.home.models.ItemModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class WritePostActivity extends AppCompatActivity {

    public static final String MODEL = "model";
    public static final String FOR_FB = "facebook";
    public static final int OPEN_RECOMMENDED_SCREN_REQ_CODE = 1003;
    private CallbackManager callbackManager;

    private TextView profileName;
    private ImageView profileImage;
    private EditText sendMessage;
    private ItemModel model;
    private FloatingActionButton sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post_activity);
        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);
        sendMessage = findViewById(R.id.send_messange);
        sendButton = findViewById(R.id.send_button);
        getFacebookInfo();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecommendedScreen();
            }
        });

    }

    private void openRecommendedScreen() {
        Intent intent = new Intent(this, RecommendedTimeScreen.class);
        Bundle bundle = new Bundle();
        bundle.putInt(FOR_FB, 0);
        bundle.putString(SendEmailActivity.DATE, "30-05-2019");
        bundle.putString(SendEmailActivity.TIME, "13:07");
        intent.putExtras(bundle);
        startActivityForResult(intent,OPEN_RECOMMENDED_SCREN_REQ_CODE);
    }

    private void postMessageOnWall() {
        ShareDialog shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new
                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(WritePostActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(WritePostActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(WritePostActivity.this, "onError" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_RECOMMENDED_SCREN_REQ_CODE && resultCode == RESULT_OK) {
            String date = data.getStringExtra(RecommendedTimeScreen.DATE);
            String time = data.getStringExtra(RecommendedTimeScreen.TIME);
            model = new ItemModel(sendMessage.getText().toString(), date, time, "FACEBOOK");
            model.setId(DatabaseManager.getInstance(this).addNewTask(model));
            setResult(RESULT_OK, new Intent());
            this.finish();
        }
//        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                            Toast.makeText(WritePostActivity.this, R.string.error_occurred_try_again, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
