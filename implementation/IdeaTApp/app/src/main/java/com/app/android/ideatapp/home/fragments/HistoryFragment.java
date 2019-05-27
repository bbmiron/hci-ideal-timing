package com.app.android.ideatapp.home.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.ideatapp.R;
import com.app.android.ideatapp.helpers.DatabaseManager;
import com.app.android.ideatapp.home.adapters.QueueItemsAdapter;
import com.app.android.ideatapp.home.models.ItemModel;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private QueueItemsAdapter adapter;
    private View layoutInflater;

    BroadcastReceiver updateUIReceiver;


    public HistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();

        filter.addAction("com.hello.action");

        updateUIReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                adapter.setModelsList(initItems());
            }
        };
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUIReceiver, filter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.history_fragment, container, false);
        return layoutInflater;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = layoutInflater.findViewById(R.id.recycler_view);
        adapter = new QueueItemsAdapter(initItems());
        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setModelsList(initItems());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUIReceiver);
        }
    }

    private List<ItemModel> initItems() {
        return DatabaseManager.getInstance(getActivity()).getTasks();
    }
}
