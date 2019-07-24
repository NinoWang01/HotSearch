package com.example.hotsearch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            intent.setClass(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            views.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);
            views.setTextViewText(R.id.tv, "5");

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    /**
     * 当 Widget 被删除时调用该方法。
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * 当 Widget 第一次被添加时调用，例如用户添加了两个你的 Widget，那么只有在添加第一个 Widget 时该方法会被调用。
     * 所以该方法比较适合执行你所有 Widgets 只需进行一次的操作
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    /**
     * 与 onEnabled 恰好相反，当你的最后一个 Widget 被删除时调用该方法，所以这里用来清理之前在 onEnabled() 中进行的操作。
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当 Widget 第一次被添加或者大小发生变化时调用该方法，可以在此控制 Widget 元素的显示和隐藏。
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }




//    private ListView lv;
//    private String url = "https://www.toolnb.com/ext/topnowdata.json";
//    private String token = "0a7365f58a8585a805862d23875f0bc2";
//    private MyAdapter myAdapter;
//    private List<Map<String, Object>> list;
//    private AVLoadingIndicatorView avi;
//    private FloatingActionButton fb;
//    private SharedPreferences sharedPreferences;
//    private String search;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initView();
//        avi.show();
//        sharedPreferences = getSharedPreferences("search", Context.MODE_PRIVATE);
//        search=sharedPreferences.getString("name","google");
//        fb.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, search.equals("google")?R.mipmap.google:R.mipmap.baidu));
//
//        fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                search = search.equals("google")?"baidu":"google";
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("search", search);
//                editor.commit();
//                fb.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, search.equals("google")?R.mipmap.google:R.mipmap.baidu));
//            }
//        });
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                getData();
//            }
//        }.start();
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
//                intent.putExtra("param",list.get(i).get("word").toString());
//                intent.putExtra("search",search);
//                startActivity(intent);
//            }
//        });
//    }
//
//
//    private void initView() {
//        lv = findViewById(R.id.lv);
//        avi = findViewById(R.id.avi);
//        fb = findViewById(R.id.fb);
//    }
//
//
//    public void getData() {
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//            // 设置请求方式,请求超时信息
//            conn.setRequestMethod("POST");
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(10000);
//            // 设置运行输入,输出:
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // Post方式不能缓存,需手动设置为false
//            conn.setUseCaches(false);
//            // 我们请求的数据:
//            String data = "token=" + token;
//            // 这里可以写一些请求头的东东...
//            // 获取输出流
//            OutputStream out = conn.getOutputStream();
//            out.write(data.getBytes());
//            out.flush();
//            if (conn.getResponseCode() == 200) {
//                // 获取响应的输入流对象
//                InputStream is = conn.getInputStream();
//                // 创建字节输出流对象
//                ByteArrayOutputStream message = new ByteArrayOutputStream();
//                // 定义读取的长度
//                int len = 0;
//                // 定义缓冲区
//                byte buffer[] = new byte[1024];
//                // 按照缓冲区的大小，循环读取
//                while ((len = is.read(buffer)) != -1) {
//                    // 根据读取的长度写入到os对象中
//                    message.write(buffer, 0, len);
//                }
//                // 释放资源
//                is.close();
//                message.close();
//                // 返回字符串
//                jsonStringToList(new String(message.toByteArray()));
//                Log.d("--------json", new String(message.toByteArray()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("--------htttp", e.toString());
//
//        }
//    }
//
//    public void jsonStringToList(String jsonString) {
//        // 先创建集合
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        try {
//            // 先获取jsonObject 和jsonArray对象
//            JSONObject jsonObject = new JSONObject(jsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            // 循环遍历jsonArray
//            for (int i = 0; i < jsonArray.length(); i++) {
//                // 在获取一次JSONObject的对象
//                JSONObject jsonObject_item = jsonArray.getJSONObject(i);
//                // 先创建map集合
//                Map<String, Object> map = new HashMap<String, Object>();
//                // 往map中添加值
//                map.put("word", jsonObject_item.getString("word"));
//                map.put("sort", jsonObject_item.getString("sort"));
//                // 把map添加到list中去
//                list.add(map);
//                Log.d("--------json", jsonObject_item.getString("word"));
//
//            }
//            Message message = new Message();
//            message.what = 0;
//            this.list = list;
//            handler.sendMessage(message);
//        } catch (Exception e) {
//            Log.d("--------e", e.toString());
//            e.printStackTrace();
//        }
//
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            avi.hide();
//            switch (msg.what) {
//                case 0:
//                    if (myAdapter != null) {
//                        myAdapter.notifyDataSetChanged();
//                    } else {
//                        myAdapter = new MyAdapter(MainActivity.this, list);
//                        lv.setAdapter(myAdapter);
//                    }
//                    break;
//                case 1:
//                    Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
}

