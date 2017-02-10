package com.cc.milkalbum.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.milkalbum.MyApplication;
import com.cc.milkalbum.R;
import com.cc.milkalbum.model.Girl;
import com.cc.milkalbum.utils.ImgUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.cc.milkalbum.dao.*;
import java.util.ArrayList;
import java.util.List;

public class BaseListFragment extends Fragment {
    protected List<Girl> girls;//要显示的数据集合
    protected PullToRefreshListView lvGirls;//ListView对象
    protected BaseAdapter girlAdapt;//适配器
    private Activity mContext;
    protected View mView;
    protected ImgUtils imgUtils;
    protected static final int GetDataLimit = 1;
    protected static GirlDBHelper dbHelper = null;

    public static final int MSG_REFRESH_FINISH = 1;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_FINISH:
                    girlAdapt.notifyDataSetChanged();
                    break;
                case ImgUtils.SUCCESS:
                    //给控件设置图片
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    ImageView image = (ImageView) lvGirls.findViewWithTag(position); //根据条目的位置获取相应的控件
                    if (image != null && bitmap != null) {
                        image.setImageBitmap(bitmap);
                    }
                    Log.d("cbx", "Handler got message, findViewWithTag: " + position);
                    // girlAdapt.notifyDataSetChanged();
                    break;
                case ImgUtils.FAIL:
                    Toast.makeText(getContext(), "图片下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public Context getContext() {
        if (mContext == null) {
            return MyApplication.getInstance();
        }
        return mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        imgUtils = new ImgUtils(getContext(), handler);
        super.onCreate(savedInstanceState);
        if(dbHelper == null) {
            dbHelper = new GirlDBHelper(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_base_list, container, false);
        try {
            init();//初始化要显示的数据集合
        } catch (Exception e) {
            Log.e("cbx", "Error when init girl list!");
            e.printStackTrace();
        }

        // ListView对象、适配器
        lvGirls = (PullToRefreshListView) mView.findViewById(R.id.lvGirls);
        girlAdapt = new GeneralAdapter();
        lvGirls.setAdapter(girlAdapt);

        setListener();//设置按item事件
        // Inflate the layout for this fragment
        return mView;
    }


    private void setListener() {
        lvGirls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.insert(girls.get(position - 1));
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
            try {
                updateGirls();
            } catch (Exception e) {
                Log.e("cbx", "Error when update girl list!");
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            lvGirls.onRefreshComplete();
            // girlAdapt.notifyDataSetChanged();
            Message message = Message.obtain();
            message.what = MSG_REFRESH_FINISH;
            handler.sendMessage(message);
            super.onPostExecute(result);
        }
    }

    /**
     * 初始化数据
     */
    protected void init() throws Exception {
        girls = new ArrayList<Girl>();
        new GetDataTask().execute();
    }

    /**
     * 加载数据
     */
    protected void updateGirls() throws Exception {
        Log.e("cbx", "base list update called!!!");
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

        private final class ViewHolder {
            ImageView ivThumb;
            TextView tvTitle;
            ImageView ivAuthorHead;
            TextView tvAuthorName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.activity_item_girls_, null);
                mViewHolder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
                mViewHolder.ivThumb.setTag(position*10);
                mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                mViewHolder.ivAuthorHead = (ImageView) convertView.findViewById(R.id.iv_author_head);
                mViewHolder.ivAuthorHead.setTag(position*10 + 1);
                mViewHolder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            Girl girl = girls.get(position);
            //封面
            mViewHolder.ivThumb.setImageBitmap(imgUtils.getBitmap(girl.getImgUrl(0), position*10));
            //主题
            mViewHolder.tvTitle.setText(girl.getTitle());
            //作者姓名
            mViewHolder.tvAuthorName.setText(girl.getAuthorName());
            //作者头像
            mViewHolder.ivAuthorHead.setImageBitmap(imgUtils.getBitmap(girl.getAuthorHeadImg(), position*10 + 1));

            return convertView;
        }

    }
}
