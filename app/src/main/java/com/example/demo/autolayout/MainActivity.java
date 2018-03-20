package com.example.demo.autolayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

public class MainActivity extends AutoLayoutActivity implements SwipeRefreshLayout.OnRefreshListener {

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //显示方面
            initAdapter();

        }
    };

    private ListView mListView;
    private List<Bean.ResultBean.DataBean> data;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        mSwipeRefreshLayout.setBackgroundColor(Color.YELLOW);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setDistanceToTriggerSync(100);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setProgressViewEndTarget(false, 200);
        mListView = findViewById(R.id.mListView);
        //请求数据
        initData();
    }

    private void initData() {
        //创建队列
        final RequestQueue requestQueue = NoHttp.newRequestQueue();
        //消息请求
        Request<String> stringRequest = NoHttp.createStringRequest("http://v.juhe.cn/toutiao/index?type=top&key=b0c1a57febbe49da8940dc820c2d8e43", RequestMethod.GET);
        requestQueue.add(0, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                Gson gson = new Gson();
                Bean bean = gson.fromJson(response.get(), Bean.class);
                data = bean.getResult().getData();
                handler.sendEmptyMessage(0);

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void initAdapter() {
        mListView.setAdapter(new BaseAdapter() {

            private ViewHolder viewHolder;

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
                    viewHolder.img = convertView.findViewById(R.id.img);
                    viewHolder.title = convertView.findViewById(R.id.title);
                    viewHolder.auto_name = convertView.findViewById(R.id.auto_name);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.auto_name.setText(data.get(position).getAuthor_name());
                viewHolder.title.setText(data.get(position).getTitle());
                Glide.with(MainActivity.this).load(data.get(position).getThumbnail_pic_s()).into(viewHolder.img);
                return convertView;
            }

            class ViewHolder {
                ImageView img;
                TextView title;
                TextView auto_name;
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        initAdapter();
    }


}
