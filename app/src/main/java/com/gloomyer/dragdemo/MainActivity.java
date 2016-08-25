package com.gloomyer.dragdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private SwipeRefreshLayout swr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        swr = (SwipeRefreshLayout) findViewById(R.id.mswr);

        swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swr.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new RecyclerView.Adapter<MyHolder>() {

            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                MyHolder holder = new MyHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(MyHolder holder, int position) {
                holder.tv.setText("" + position);
            }

            @Override
            public int getItemCount() {
                return 50;
            }
        });
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        public final View contentView;
        public TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            this.contentView = itemView;
            tv = (TextView) itemView.findViewById(android.R.id.text1);
            tv.setTextSize(16);
            tv.setTextColor(Color.BLACK);
        }
    }
}
