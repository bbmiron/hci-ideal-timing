package com.app.android.ideatapp.jobs;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.home.activities.RecommendedTimeScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SendEmailJobService extends JobService {

    private boolean jobCancellend = false;
    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("tag", "onStartJob JobId: " + params.getJobId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                String currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());


                String date = params.getExtras().getString(RecommendedTimeScreen.DATE);
                String time = params.getExtras().getString(RecommendedTimeScreen.TIME);

//                if (currentDate.equals(date) && currentTime.equals(time)) {
                    sendEmail();
                    jobFinished(params, true);
//                } else {
//
//                }
            }
        }).start();

        return false;
    }

    private void sendEmail() {
        Log.d("tag", "running...");
        SendEmailActivity sendEmailActivity = SendEmailActivity.instance;
        if (sendEmailActivity != null) {
            sendEmailActivity.getResultsFromApi();
        }
        if (jobCancellend) {
            return;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("tag", "Job cancelled before completion");
        jobCancellend = true;
        return true;
    }
}
