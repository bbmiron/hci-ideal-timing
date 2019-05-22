package com.app.android.ideatapp.home.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.ideatapp.R;
import com.app.android.ideatapp.home.activities.HomeScreenActivity;
import com.app.android.ideatapp.home.adapters.QueueItemsAdapter;
import com.app.android.ideatapp.home.models.ItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HistoryFragment extends Fragment {

    private List<ItemModel> itemModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private QueueItemsAdapter adapter;
    private View layoutInflater;


    public HistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.history_fragment, container, false);
        return layoutInflater;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = layoutInflater.findViewById(R.id.recycler_view);
        adapter = new QueueItemsAdapter(itemModels);
        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (this.getArguments() != null) {
            initItems();
        }
    }

    private void initItems() {
        final String title = this.getArguments().getString(HomeScreenActivity.TITLE);
        final String date = this.getArguments().getString(HomeScreenActivity.DATE);
        ItemModel model = new ItemModel(title, date);
        itemModels.add(model);
        model = new ItemModel("Java Dev", "Pending...");
        itemModels.add(model);
    }
}
