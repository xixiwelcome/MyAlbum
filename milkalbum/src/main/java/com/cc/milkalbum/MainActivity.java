package com.cc.milkalbum;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.milkalbum.model.Girl;
import com.handmark.pulltorefresh.library.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Girl> girls;//要显示的数据集合
    private PullToRefreshListView lvGirls;//ListView对象
    private BaseAdapter girlAdapt;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();//初始化要显示的数据集合、ListView对象、以及适配器
        setListener();//设置按item事件

    }

    private void setListener() {
        lvGirls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, girls.get(position).getName()+":被短按 ", Toast.LENGTH_LONG).show();
            }

        });

        lvGirls.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }
        });
    }

    /**
     * 下拉加载执行de任务
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        // 获取新的数据
        @Override
        protected String[] doInBackground(Void... params) {
            Random r = new Random();
            Girl general = new Girl(r.nextInt(), r.nextInt()+"");
            girls.add(0, general);
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            lvGirls.onRefreshComplete();
            girlAdapt.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        girls = new ArrayList<Girl>();
        int[] resImags = {1, 2, 3, 4, 5, 6, 7, 8};
        String [] names = {"1", "2","3", "4","5", "6","7", "8"};
        for (int i =0;i<resImags.length;i++){
            Girl general = new Girl(resImags[i],names[i]);
            girls.add(general);
        }

        lvGirls = (PullToRefreshListView) findViewById(R.id.lvGirls);
        girlAdapt = new GeneralAdapter();
        lvGirls.setAdapter(girlAdapt);
    }

    class GeneralAdapter extends BaseAdapter {

        //得到listView中item的总数
        @Override
        public int getCount() {
            return girls.size();
        }

        @Override
        public Girl getItem(int position) {
            return girls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(MainActivity.this, R.layout.activity_item_girls_, null);
            //找到显示军事家头像的ImageView
            ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
            //找到显示名字的TextView
            TextView tvName = (TextView) layout.findViewById(R.id.tvName);
            Girl general =  girls.get(position);
            //显示头像
            // ivThumb.setImageResource(general.getId());
            //显示姓名
            tvName.setText(general.getName());

            return layout;
        }

    }
}
