package com.app.android.ideatapp.jobs;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SendEmailJobService extends JobService {

    private static final String DATE_FORMAT = "dd-M-yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("tag", "onStartJob JobId: " + params.getJobId());
        String currentDate = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(new Date());


        String time = params.getExtras().getString(RecommendedTimeScreen.TIME);
        String date = params.getExtras().getString(RecommendedTimeScreen.DATE);

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
        }
        if (jobCancelled) {
            return;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("tag", "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
