package com.wpy.zhihu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wpy.zhihu.R;
import com.wpy.zhihu.api.ZhihuApi;
import com.wpy.zhihu.bean.StoryDetailsEntity;
import com.wpy.zhihu.http.ZhihuService;
import com.wpy.zhihu.utils.HtmlUtils;
import com.wpy.zhihu.utils.ViewUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 详情Fragment 内容展示页面
 */

public class DesFragment extends Fragment {
    public static final String PARAM_ID = "param_id";
    private RelativeLayout common_error;
    private FrameLayout common_loading;
    private WebView des_web;
    private int curId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curId = getArguments().getInt(PARAM_ID, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.des_fragment, container, false);
        des_web = (WebView) view.findViewById(R.id.story_web);
        common_error = (RelativeLayout) view.findViewById(R.id.common_error);
        common_loading = (FrameLayout) view.findViewById(R.id.common_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        des_web.setVerticalScrollBarEnabled(false);
        des_web.getSettings().setDefaultTextEncodingName("UTF-8");
        loadData();
    }
    /**
     * 加载数据方法
     */
    private void loadData() {
        ZhihuApi zhihuService = ZhihuService.createZhihuService();
        zhihuService.getNewsDetails(curId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<StoryDetailsEntity, String>() {
                    @Override
                    public String call(StoryDetailsEntity storyDetailsEntity) {
                        String s = HtmlUtils.structHtml(storyDetailsEntity.getBody(), storyDetailsEntity.getCss());
                        return s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                        handlerFailure();
                    }

                    @Override
                    public void onNext(String s) {
                        webShowData(s);
                    }
                });

    }

    private void handlerFailure() {
        ViewUtils.setViewVisibility(common_error, true);
        ViewUtils.setViewVisibility(common_loading, false);
        ViewUtils.setViewVisibility(des_web, false);
    }

    private void webShowData(String htl) {
        if (des_web != null) {
            ViewUtils.setViewVisibility(common_loading, false);
            ViewUtils.setViewVisibility(common_error, false);
            des_web.loadData(htl, "text/html; charset=UTF-8", null);
        }
    }
}
