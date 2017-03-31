package com.wpy.zhihu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wpy.zhihu.R;
import com.wpy.zhihu.adapter.HomeAdapter;
import com.wpy.zhihu.api.ZhihuApi;
import com.wpy.zhihu.bean.NewsEntity;
import com.wpy.zhihu.bean.StoriesEntity;
import com.wpy.zhihu.http.ZhihuService;
import com.wpy.zhihu.utils.ViewUtils;
import com.wpy.zhihu.view.AutoLoadRecylerView;

import java.util.ArrayList;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 内容首页
 */

public class HomeFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener,AutoLoadRecylerView.loadMoreListener {

    private AutoLoadRecylerView recycler;
    private WaveSwipeRefreshLayout mainWsrefresh;
    private HomeAdapter adapter;
    private FrameLayout loading;
    private String curDate;
    private RelativeLayout commonError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.home_fragment,container,false);
        mainWsrefresh = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_wsrefresh);
        loading = (FrameLayout) view.findViewById(R.id.common_loading);
        commonError = (RelativeLayout) view.findViewById(R.id.common_error);

        recycler = (AutoLoadRecylerView) view.findViewById(R.id.recycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置刷新视图颜色
        mainWsrefresh.setColorSchemeColors(Color.WHITE);
        mainWsrefresh.setWaveColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mainWsrefresh.setOnRefreshListener(this);
        setAdapter();
        getData();

    }
    /**
     *  适配方法
     */
    private void setAdapter(){
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        adapter = new HomeAdapter(getActivity());
        recycler.setAdapter(adapter);
        //加载更多监听
        recycler.setLoadMoreListener(this);
    }
    /**
     *  请求数据
     */
    private void getData(){
        ViewUtils.setViewVisibility(loading, true);
        ZhihuApi zhihuService = ZhihuService.createZhihuService();
        zhihuService.getLastestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerFailure(true);
                    }

                    @Override
                    public void onNext(NewsEntity newsEntity) {
                        handlerSuccess(newsEntity,true);
                    }
                });
    }
    /**
     *   上拉加载数据方法
     */
    private void loadBeforeData(String curDate){
        ZhihuApi service = ZhihuService.createZhihuService();
        service.getBeforeNews(curDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerFailure(true);
                    }

                    @Override
                    public void onNext(NewsEntity newsEntity) {
                        handlerSuccess(newsEntity,false);
                    }
                });
    }
    /**
     * 改变list数据
     *
     * @param isRefresh
     * @param stories
     */
    private void chageListDatas(boolean isRefresh, ArrayList<StoriesEntity> stories) {
        if (adapter == null) {
            return;
        }
        if (isRefresh) {
            //刷新操作
            adapter.changeData(stories);
        } else {
            //加载更多走这个
            adapter.addData(stories);
        }
    }

    /**
     *   刷新操作
     */
    @Override
    public void onRefresh() {
        getData();
    }

    /**
     *  加载geng多
     */
    @Override
    public void onLoadMore() {
        loadBeforeData(curDate);
    }
    /**
     * 停止加载状态
     *
     * @param isRefresh 是否刷新
     */
    private void stopLoadStatus(boolean isRefresh) {
        if (isRefresh && mainWsrefresh != null) {
            mainWsrefresh.setRefreshing(false);
        } else if (recycler != null) {
            recycler.setLoading(false);
        }
    }

    /**
     *    成功显示效果
     * @param entity
     * @param isRefresh
     */
    private void handlerSuccess(NewsEntity entity, boolean isRefresh) {
        ViewUtils.setViewVisibility(loading, false);
        ViewUtils.setViewVisibility(commonError, false);
        ViewUtils.setViewVisibility(mainWsrefresh, true);
        //获取加载更多需要的 请求参数
        curDate = entity.getDate();
        stopLoadStatus(isRefresh);
        chageListDatas(isRefresh, entity.getStories());
    }
    /**
     *  失败显示效果
     */
    private void handlerFailure(boolean isRefresh) {
        stopLoadStatus(isRefresh);
        ViewUtils.setViewVisibility(commonError, true);
        ViewUtils.setViewVisibility(loading, false);
        ViewUtils.setViewVisibility(mainWsrefresh, false);
    }
}
