package com.wpy.zhihu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wpy.zhihu.R;
import com.wpy.zhihu.adapter.DesAdapter;
import com.wpy.zhihu.bean.StoriesEntity;

import java.util.ArrayList;

/**
 *  详情界面
 */

public class HomeDesActivity extends AppCompatActivity {
    public static final String PARAM_POS = "param_pos";
    public static final String PARAM_DATA_LIST = "param_data_list";
    private int curPos = -1;
    private ArrayList<StoriesEntity> arrayList;
    private Toolbar toolbar;
    private ViewPager viewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_des_activity);
        Intent intent = getIntent();
        curPos = intent.getIntExtra(PARAM_POS, -1);
        arrayList = intent.getParcelableArrayListExtra(PARAM_DATA_LIST);
        Log.i("TAG", "onCreate: 集合"+arrayList.size());
        intiView();
    }
    /**
     *   初始化
     */
    private void intiView(){
        viewpager = (ViewPager) findViewById(R.id.home_des_viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar();
        //进行适配传值
        viewpager.setAdapter(new DesAdapter(getSupportFragmentManager(), arrayList));
        viewpager.setCurrentItem(curPos);
    }
    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
