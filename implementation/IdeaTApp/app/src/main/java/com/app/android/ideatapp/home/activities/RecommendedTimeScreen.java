package com.app.android.ideatapp.home.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.Calendar.Freebusy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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

    private static final Level LOGGING_LEVEL = Level.OFF;
    private static final String PREF_ACCOUNT_NAME = "gusa.diana@gmail.com";
    GoogleAccountCredential credential;
    com.google.api.services.calendar.Calendar client;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_screen_activity);
        init();
        //getRecommendedDateTime();

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

    private void getRecommendedDateTime(){
        //Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("IdeaTAppV1.1")
                .build();

        List<FreeBusyRequestItem> itemList = new ArrayList<FreeBusyRequestItem>();
        FreeBusyRequestItem item = new FreeBusyRequestItem();
        item.setId("gusa.diana@gmail.com");
        itemList.add(item);

        FreeBusyRequest request = new FreeBusyRequest();
        request.setTimeZone("UTC");
        request.setTimeMin(new DateTime(new Date(2019,05,01,00,00,00)));
        request.setTimeMax(new DateTime(new Date(2019,05,01,00,00,00)));
        request.setItems(itemList);
        Log.d("myTag6","hei");
        try {
            //FreeBusyResponse response = client.freebusy().query(request).execute();
            Freebusy.Query calendarQuery = client.freebusy().query(request);
            FreeBusyResponse busyResponse = calendarQuery.execute();
            Log.d("myTag7","hei");
            /*for (Map.Entry<String, FreeBusyCalendar> entry : response.getCalendars().entrySet()) {
                Log.d(entry.getKey(), entry.getValue().toPrettyString());
            }*/

        } catch (IOException e) {
            Log.d("myTag8","hei");
            e.printStackTrace();
        }
    }
}
