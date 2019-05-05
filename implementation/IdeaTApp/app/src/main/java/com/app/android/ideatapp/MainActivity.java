package com.app.android.ideatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.android.ideatapp.home.activities.HomeScreenActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static AccessToken ACCESS_TOKEN = null;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebookSdk();
        setContentView(R.layout.activity_main);
        customizeLogo();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList( "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                ACCESS_TOKEN = loginResult.getAccessToken();
                handleSuccessfulLogin();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login attempt canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private void customizeLogo() {
        ImageView logo = findViewById(R.id.logo);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.logo)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight())), 100, 100, paint);// Round Image Corner 100 100 100 100
        logo.setImageBitmap(imageRounded);
    }

    private void handleSuccessfulLogin() {
        startActivity(new Intent(this, HomeScreenActivity.class));
    }
}
