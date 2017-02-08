package com.cc.milkalbum;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Girl> girls;//要显示的数据集合
    private ListView lvGirls;//ListView对象
    private BaseAdapter girlAdapt;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();//初始化要显示的数据集合、ListView对象、以及适配器
        setListener();//设置按item事件

    }

    private void setListener() {
// TODO Auto-generated method stub
//短按事件监听
        lvGirls.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, girls.get(position).getName()+":被短按 ", Toast.LENGTH_LONG).show();
            }

        });

//长按事件监听
        lvGirls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
// TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, girls.get(position).getName()+":被长按 ", Toast.LENGTH_LONG).show();
                return true;//1、如果返回false，长按后，他也会触发短按事件2、如果返回true的话，长按后就不会触发短按事件
            }
        });
    }


    private void init() {
// TODO Auto-generated method stub
//初始化要显示的数据集合---start
        girls = new ArrayList<Girl>();
//图片资源集合
        int[] resImags = {1, 2, 3, 4, 5, 6, 7, 8};
//将资源中的字符串组数转换为Java数组
        String [] names = {"1", "2","1", "2","1", "2","1", "2"};
        for (int i =0;i<resImags.length;i++){
            Girl general = new Girl(resImags[i],names[i]);
            girls.add(general);
        }
//初始化要显示的数据集合---end
//初始化listView
        lvGirls = (ListView) findViewById(R.id.lvGirls);
//初始化适配器以及设置该listView的适配器
        girlAdapt = new GeneralAdapter();
        lvGirls.setAdapter(girlAdapt);
    }

    class GeneralAdapter extends BaseAdapter {

        //得到listView中item的总数
        @Override
        public int getCount() {
// TODO Auto-generated method stub
            return girls.size();
        }


        @Override
        public Girl getItem(int position) {
// TODO Auto-generated method stub
            return girls.get(position);
        }


        @Override
        public long getItemId(int position) {
// TODO Auto-generated method stub
            return position;
        }


        //简单来说就是拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
//拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(MainActivity.this, R.layout.activity_item_girls_, null);
//找到显示军事家头像的ImageView
            ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
//找到显示军事家名字的TextView
            TextView tvName = (TextView) layout.findViewById(R.id.tvName);
//获取军事中下标是position的军事家对象
            Girl general =  girls.get(position);
//显示军事家头像
            // ivThumb.setImageResource(general.getId());
//显示军事家的姓名
            tvName.setText(general.getName());

            return layout;
        }

    }
}
