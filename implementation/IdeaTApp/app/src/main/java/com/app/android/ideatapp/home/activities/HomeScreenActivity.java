package com.app.android.ideatapp.home.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.android.ideatapp.MainActivity;
import com.app.android.ideatapp.R;
import com.app.android.ideatapp.SendEmailActivity;
import com.app.android.ideatapp.home.adapters.TabsAdapter;
import com.app.android.ideatapp.home.fragments.HistoryFragment;
import com.app.android.ideatapp.home.fragments.HomeFragment;
import com.app.android.ideatapp.home.models.ItemModel;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String TIME = "time";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String name;
    private String email;
    private HistoryFragment historyFragment;
    private TabsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        name = getIntent().getStringExtra(MainActivity.NAME_TAG);
        email = getIntent().getStringExtra(MainActivity.EMAIL_TAG);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.NAME_TAG, name);
        bundle.putString(MainActivity.EMAIL_TAG, email);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        historyFragment = new HistoryFragment();
        adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "Home");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ItemModel model = (ItemModel) data.getExtras().getParcelable(SendEmailActivity.MODEL);
            Bundle bundle = new Bundle();
            bundle.putString(TITLE, model.getTitle());
            bundle.putString(DATE, model.getDate());
            historyFragment.setArguments(bundle);
            adapter.addFragment(historyFragment, "History");
            viewPager.setAdapter(adapter);
            viewPager.arrowScroll(View.FOCUS_RIGHT);
        }
    }
}
