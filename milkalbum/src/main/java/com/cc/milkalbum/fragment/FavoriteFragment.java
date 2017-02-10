package com.cc.milkalbum.fragment;
import com.cc.milkalbum.model.Girl;
import java.util.ArrayList;


public class FavoriteFragment extends BaseListFragment {
    /**
     * 初始化数据
     */
    protected void init() {
        girls =  new ArrayList<Girl>();
    }

    /**
     * 加载数据
     */
    public void updateGirls(){
        girls.clear();
        girls = dbHelper.getAll(null, null);
        girlAdapt.notifyDataSetChanged();
    }

}
