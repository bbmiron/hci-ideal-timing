package com.app.android.ideatapp.home.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.android.ideatapp.R;
import com.app.android.ideatapp.WritePostActivity;
import com.app.android.ideatapp.jobs.SendEmailJobService;

import java.util.Calendar;

public class RecommendedTimeScreen extends AppCompatActivity {

    public static final int JOB_ID = 100;
    public static final String DATE = "date";
    public static final String TIME = "time";
    private TextView recommendedDate;
    private TextView recommendedTime;
    private TextView recommendedSubtitle;
    private Button editDateButton;
    private Button editTimeButton;
    private Button scheduleButton;
    private int mYear, mMonth, mDay, mHour, mMinute, activityOpened;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_screen_activity);
        init();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            activityOpened = bundle.getInt(WritePostActivity.FOR_FB);
            if ( activityOpened == 0) {
                recommendedSubtitle.setText(R.string.recommended_subtitle_for_facebook);
            } else if (activityOpened == 1) {
                recommendedSubtitle.setText(R.string.recommended_subtitle_for_upload);
            }
        }

        editDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        editTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityOpened != 0 || activityOpened != 1) {
                    scheduleJob();
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(DATE, recommendedDate.getText());
                resultIntent.putExtra(TIME, recommendedTime.getText());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, SendEmailJobService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(RecommendedTimeScreen.DATE, recommendedDate.getText().toString());
        bundle.putString(RecommendedTimeScreen.TIME, recommendedTime.getText().toString());
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(10000)
                .setPersisted(true)
                .setExtras(bundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("tag", "Job scheduled!");
        } else {
            Log.d("tag", "Job not scheduled");
        }
    }

    private void openTimePicker() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute < 10) {
                            recommendedTime.setText(hourOfDay + ":0" + minute);
                        } else {
                            recommendedTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        recommendedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void init() {
        recommendedDate = findViewById(R.id.recommended_date);
        recommendedTime = findViewById(R.id.recommended_time);
        editDateButton = findViewById(R.id.edit_date);
        editTimeButton = findViewById(R.id.edit_time);
        scheduleButton = findViewById(R.id.ok_button);
        recommendedSubtitle = findViewById(R.id.recommended_subtitle);
    }
}
