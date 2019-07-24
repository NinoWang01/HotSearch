package com.example.hotsearch;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;

    public MyAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder.tv = view.findViewById(R.id.tv);
            viewHolder.iv = view.findViewById(R.id.iv);
            viewHolder.ll = view.findViewById(R.id.ll);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText((i+1)+"、"+list.get(i).get("word").toString());
        if (i>4)
            viewHolder.iv.setVisibility(View.GONE);
        else
            viewHolder.iv.setVisibility(View.VISIBLE);
//        viewHolder.ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,WebViewActivity.class);
//                intent.putExtra("param",list.get(i).get("word").toString());
//            context.startActivity(intent);
//            }
//        });
        return view;
    }

    class ViewHolder {
        private TextView tv;
        private ImageView iv;
        private LinearLayout ll;
    }
}
