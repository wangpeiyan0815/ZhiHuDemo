package com.wpy.zhihu.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by diff on 2016/2/5.
 */
public class AutoLoadRecylerView extends RecyclerView {
    private loadMoreListener loadMoreListener;
    private AutoLoadScroller autoLoadScroller;
    private boolean isLoading = false;

    /**
     *   提供加载更多的接口
     */
    public interface loadMoreListener {
        void onLoadMore();
    }

    public AutoLoadRecylerView(Context context) {
        this(context, null);
    }


    public AutoLoadRecylerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoLoadScroller = new AutoLoadScroller();
        //添加一个侦听器   开始我的上拉加载判断
        addOnScrollListener(autoLoadScroller);
    }

    //加载更多的set方法
    public void setLoadMoreListener(AutoLoadRecylerView.loadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public boolean isLoading() {
        return isLoading;
    }
    //来判断是否停止加载
    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void removeAutoScroller() {
        removeOnScrollListener(autoLoadScroller);
    }

    /**
     *recyclerView 的滚动监听
     */
    private class AutoLoadScroller extends OnScrollListener {
        //滚动回调
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (getLayoutManager() instanceof LinearLayoutManager) {
                //得到最后一个条目
                int lastVisiblePos = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                //获取显示的条目数
                int itemCount = getAdapter().getItemCount();
                //dy 垂直滚动距离   dx : 水平滚动距离
                if (loadMoreListener != null && !isLoading && lastVisiblePos > itemCount - 2 && dy > 0) {
                    loadMoreListener.onLoadMore();
                    isLoading = true;
                }
            }
        }
    }
}
