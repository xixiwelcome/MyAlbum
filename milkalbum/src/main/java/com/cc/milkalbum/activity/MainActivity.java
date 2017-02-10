package com.cc.milkalbum.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cc.milkalbum.R;
import com.cc.milkalbum.fragment.FavoriteFragment;
import com.cc.milkalbum.fragment.LatestFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FavoriteFragment flFavorite;
    private LatestFragment flLatest;
    private TextView tvFavorite;
    private TextView tvLatest;

    private int currentTab;
    public final static int TabLatest = 0;
    public final static int TabFavorite = 1;
    public static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.cc.milkalbum.R.layout.activity_main);

        init();
        setListener();
    }

    private void init(){
        mQueue = Volley.newRequestQueue(this);
        tvLatest = (TextView) findViewById(R.id.tv_latest);
        tvFavorite = (TextView) findViewById(R.id.tv_favorite);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        flLatest = new LatestFragment();
        transaction.replace(R.id.fl_list_latest, flLatest);
        flFavorite = new FavoriteFragment();
        transaction.replace(R.id.fl_list_favorite, flFavorite);

        transaction.hide(flFavorite);
        transaction.show(flLatest);
        currentTab = TabLatest;
        transaction.commit();
    }

    private void setListener(){
        tvLatest.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()){
            case R.id.tv_latest:
                Log.d("cbx", "点击最新！！！！");
                if (currentTab != TabLatest){
                    // 切换到latest
                    transaction.hide(flFavorite);
                    transaction.show(flLatest);
                    currentTab = TabLatest;
                    transaction.commit();
                }
                break;
            case R.id.tv_favorite:
                Log.d("cbx", "点击收藏！！！！");
                if(currentTab != TabFavorite){
                    // 切换到favorite
                    transaction.hide(flLatest);
                    transaction.show(flFavorite);
                    currentTab = TabFavorite;
                    transaction.commit();
                }
                break;
            default:
                break;
        }
    }
}
