package com.ingluise.ProyectoMinTICCiclo4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class RecyclerViewBasicActivity extends AppCompatActivity {
    private final String TAG = RecyclerViewBasicActivity.class.getSimpleName();
    private final int DATASET_LENGHT = 100;

    private RecyclerView rv;
    private CustomAdapter ca;

    private LinearLayoutManager llm;
    private GridLayoutManager glm;

    private String[] mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_basic);

        rv = findViewById(R.id.rv_list);
        initDataset();
        ca = new CustomAdapter(mDataset);
        Log.i(TAG, ""+ca.getItemCount());
        rv.setAdapter(ca);
        Log.i(TAG, mDataset[2]);

        setRecyclerViewLayoutManager();
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        glm = new GridLayoutManager(this, 3);

        rv.setLayoutManager(glm);
        rv.scrollToPosition(scrollPosition);
    }

    public String[] initDataset() {
        mDataset = new String[DATASET_LENGHT];
        for (int i = 0; i < DATASET_LENGHT; i++) {
            mDataset[i] = "This is element # " + i;
        }
        return mDataset;
    }
}