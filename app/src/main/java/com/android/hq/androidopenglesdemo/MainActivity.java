package com.android.hq.androidopenglesdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.hq.androidopenglesdemo.texture.ImageMultiEffectActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mListAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        setData();
    }

    private void setData() {
        ArrayList<ListAdapter.DataBean> list = new ArrayList<>();
        list.add(new ListAdapter.DataBean(getResources().getString(R.string.title_image_multi_effect_activity),
                getResources().getString(R.string.desc_image_multi_effect_activity), ImageMultiEffectActivity.class));

        mListAdapter.updateData(list);
    }
}