package com.wpy.zhihu.api;


import com.wpy.zhihu.bean.NewsEntity;
import com.wpy.zhihu.bean.StoryDetailsEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by diff on 2016/2/16.
 */
public interface ZhihuApi {
    /**
     *    刷新
     */
    @GET("api/4/news/latest")
    Observable<NewsEntity> getLastestNews();

    /**
     *   加载更多
     * @param id
     * @return
     */
    @GET("api/4/news/before/{id}")
    Observable<NewsEntity> getBeforeNews(@Path("id") String id);

    @GET("api/4/news/{id}")
    Observable<StoryDetailsEntity> getNewsDetails(@Path("id") int id);
}
