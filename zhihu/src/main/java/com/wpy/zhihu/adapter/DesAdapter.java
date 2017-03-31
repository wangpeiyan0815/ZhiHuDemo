package com.wpy.zhihu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wpy.zhihu.bean.StoriesEntity;
import com.wpy.zhihu.fragment.DesFragment;

import java.util.ArrayList;

/**
 *  详情ViewPager 适配
 */

public class DesAdapter extends FragmentPagerAdapter {
    private ArrayList<StoriesEntity> storiesEntities;

    public DesAdapter(FragmentManager fm, ArrayList<StoriesEntity> storiesEntities) {
        super(fm);
        this.storiesEntities = storiesEntities;
    }

    @Override
    public Fragment getItem(int position) {
        //添加Fragment
        DesFragment desFragment = new DesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DesFragment.PARAM_ID, storiesEntities.get(position).getId());
        desFragment.setArguments(bundle);
        return desFragment;
    }

    @Override
    public int getCount() {
        return storiesEntities == null ? 0 : storiesEntities.size();
    }
}
