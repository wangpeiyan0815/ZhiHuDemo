package com.wpy.zhihu.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wpy.zhihu.R;
import com.wpy.zhihu.fragment.HomeFragment;

/**
 *   在使用DrawerLayout侧滑时出现错误，切记在Xml中的 android:layout_gravity="left" 属性
 *   与在菜单点击时的判断： drawerLayout.closeDrawer(GravityCompat.START);/ 要保持一致
 *
 *   使用compile 'com.facebook.fresco:fresco:0.9.0+'   布局中SimpleDraweeView
 *   需要在Fresco.initialize(this);  setContentView(R.layout.activity_main); 中做一下初始化工作
 *   http://blog.csdn.net/yy1300326388/article/details/45023489
 *
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        intiView();
        text();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.frame,new HomeFragment()).commit();
    }
    /**
     *   测试String  结果为true
     */
    private void text(){
        //这种形式，一开始jvm 在字符串常量池中找不到该对象，便会创建，
        // 在次创建b的时候也会进行在常量池里查找
        //一旦发现有相同内容便会把值进行返回
        String a="a";
        String b="a";
        boolean bo = (a == b);
        Log.i("TAG", "text: +++"+bo);
        boolean equals = a.equals(b);
        Log.i("TAG", "equals: +++"+equals);
        //new来构造字符串对象的时候，不管字符串常量池中有没有相同内容的对象的引用，
        // 新的字符串对象都会创建。
        String s = new String("a");
        String s1 = new String("a");
        boolean b1 = (s == s1);
        Log.i("TAG", "equals: +++"+b1);
    }
    /**
     * 初始化
     */
    private void intiView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toold);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //让导航按钮显示出来
            //Toolbar最左侧的按钮叫做HomeAsUp
            actionBar.setDisplayHomeAsUpEnabled(true);
            //点击图片进行打开
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {//actionbar上的home icon
            //END即gravity.right 从右向左显示   START即left  从左向右弹出显示
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);//关闭抽屉
            } else {
                drawerLayout.openDrawer(GravityCompat.START);//打开抽屉
            }
            return true;
        }
        return false;
    }
}
