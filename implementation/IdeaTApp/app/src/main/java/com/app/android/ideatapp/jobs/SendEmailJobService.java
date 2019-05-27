package com.app.android.ideatapp.jobs;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.android.ideatapp.MainActivity;
import com.app.android.ideatapp.R;
import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.helpers.DatabaseManager;
import com.app.android.ideatapp.home.activities.HomeScreenActivity;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;
import com.app.android.ideatapp.home.fragments.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SendEmailJobService extends JobService {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private boolean jobCancelled = false;
    private long id;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("tag", "onStartJob JobId: " + params.getJobId());
        String currentDate = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(new Date());


        String time = params.getExtras().getString(RecommendedTimeScreen.TIME);
        String date = params.getExtras().getString(RecommendedTimeScreen.DATE);
        this.id = params.getExtras().getLong(RecommendedTimeScreen.ID);

        if (currentDate.equals(date) && currentTime.equals(time)) {
            sendEmail();
            jobFinished(params, false);
        }
        return false;
    }

    private void sendEmail() {
        Log.d("tag", "running...");
        SendEmailActivity sendEmailActivity = SendEmailActivity.instance;
        if (sendEmailActivity != null) {
            sendEmailActivity.getResultsFromApi();
            DatabaseManager.getInstance(getApplicationContext()).updateTaskStatus(id, "SENT");
            sendNotification();
            sendIntent();
        }
        if (jobCancelled) {
            return;
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                HomeFragment.SEND_EMAIL_REQ_CODE, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Task completed")
                        .setContentText("Your email has been sent!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) id, builder.build());
    }

    private void sendIntent() {
        Intent local = new Intent();
        local.setAction("com.hello.action");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(local);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("tag", "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
