package com.example.bruno.testing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> example ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            example = new ArrayList<>();
            example.add(0,"billy");
            example.add(1,"jason");
            example.add(2,"tommy");
            example.add(3,"kimberly");
            example.add(4,"trinny");
            example.add(5,"adam");

            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(example);
            mRecyclerView.setAdapter(mAdapter);
        }


    }

