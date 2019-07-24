package com.example.hotsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MainActivity extends Activity {
    private ListView lv;
    private String url = "https://www.toolnb.com/ext/topnowdata.json";
    private String token = "0a7365f58a8585a805862d23875f0bc2";
    private MyAdapter myAdapter;
    private List<Map<String, Object>> list;
    private AVLoadingIndicatorView avi;
    private FloatingActionButton fb;
    private SharedPreferences sharedPreferences;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        avi.show();
        sharedPreferences = getSharedPreferences("search", Context.MODE_PRIVATE);
        search=sharedPreferences.getString("name","google");
        fb.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, search.equals("google")?R.mipmap.google:R.mipmap.baidu));

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = search.equals("google")?"baidu":"google";
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("search", search);
                editor.commit();
                fb.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, search.equals("google")?R.mipmap.google:R.mipmap.baidu));
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                getData();
            }
        }.start();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("param",list.get(i).get("word").toString());
                intent.putExtra("search",search);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        lv = findViewById(R.id.lv);
        avi = findViewById(R.id.avi);
        fb = findViewById(R.id.fb);
    }


    public void getData() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            // 设置请求方式,请求超时信息
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            // 设置运行输入,输出:
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Post方式不能缓存,需手动设置为false
            conn.setUseCaches(false);
            // 我们请求的数据:
            String data = "token=" + token;
            // 这里可以写一些请求头的东东...
            // 获取输出流
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                jsonStringToList(new String(message.toByteArray()));
                Log.d("--------json", new String(message.toByteArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("--------htttp", e.toString());

        }
    }

    public void jsonStringToList(String jsonString) {
        // 先创建集合
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // 先获取jsonObject 和jsonArray对象
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            // 循环遍历jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                // 在获取一次JSONObject的对象
                JSONObject jsonObject_item = jsonArray.getJSONObject(i);
                // 先创建map集合
                Map<String, Object> map = new HashMap<String, Object>();
                // 往map中添加值
                map.put("word", jsonObject_item.getString("word"));
                map.put("sort", jsonObject_item.getString("sort"));
                // 把map添加到list中去
                list.add(map);
                Log.d("--------json", jsonObject_item.getString("word"));

            }
            Message message = new Message();
            message.what = 0;
            this.list = list;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.d("--------e", e.toString());
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            avi.hide();
            switch (msg.what) {
                case 0:
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    } else {
                        myAdapter = new MyAdapter(MainActivity.this, list);
                        lv.setAdapter(myAdapter);
                    }
                    break;
                case 1:
                    Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
