package com.cc.milkalbum.fragment;
import com.cc.milkalbum.model.Girl;
import com.cc.milkalbum.utils.ResInfoUtils;
import java.util.ArrayList;


public class FavoriteFragment extends BaseListFragment {
    /**
     * 初始化数据
     */
    protected void init() {
        girls = new ArrayList<Girl>();
        Girl general = new Girl();
        girls.add(general);
    }

    /**
     * 加载数据
     */
    protected void updateGirls(){
        girls.add(0, new Girl());
    }

}
