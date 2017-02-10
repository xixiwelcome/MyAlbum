package com.cc.milkalbum.fragment;
import com.cc.milkalbum.model.Girl;
import com.cc.milkalbum.utils.ResInfoUtils;

import java.util.ArrayList;
import java.util.Random;


public class LatestFragment extends BaseListFragment {

    /**
     * 加载数据
     * girls的size永远是 GetDataLimit
     * 每次随机刷新一个page显示
     */
    protected void updateGirls() throws Exception{
        Random r = new Random();
        girls.clear();
        girls = ResInfoUtils.getGirlsInfo( r.nextInt(80), GetDataLimit);
        while( girls == null ){
            girls = ResInfoUtils.getGirlsInfo( r.nextInt(80), GetDataLimit);
        }
        girlAdapt.notifyDataSetChanged();
    }
}
