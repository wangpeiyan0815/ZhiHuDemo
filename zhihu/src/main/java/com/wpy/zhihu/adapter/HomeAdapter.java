package com.wpy.zhihu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wpy.zhihu.R;
import com.wpy.zhihu.bean.StoriesEntity;
import com.wpy.zhihu.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页适配器
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<StoriesEntity> storiesList;

    public HomeAdapter(Context context) {
        this.context = context;
    }
    private long lastPos = -1;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_adapter_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final StoriesEntity sto = storiesList.get(position);
        if (sto == null) {
            return;
        }
        //进行赋值
        ((ViewHolder) holder).txt_title.setText(sto.getTitle());
        List<String> imgs = sto.getImages();
        //加载图片
        if (imgs != null && imgs.size() > 0) {
            ((ViewHolder) holder).item_img.setImageURI(Uri.parse(imgs.get(0)));
        }
        //点击进行跳转
        ((ViewHolder) holder).item_main_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行页面的跳转
                IntentUtils.toHomeDesActivity(context, position, storiesList);
            }
        });
        //给cardView 添加动画
        startAnimator(((ViewHolder) holder).item_main_card, position);
    }

    /**
     *   加载动画
     * @param view
     * @param position
     */
    private void startAnimator(View view, long position) {
        if (position > lastPos) {
            //AnimationUtils定义了使用动画常见的实用程序。
            view.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.item_bottom_in));
            lastPos = position;
        }
    }

    /**
     * （当适配器创建的view（即列表项view）被窗口分离（即滑动离开了当前窗口界面）就会被调用）
     *    重写该动画  保证动画 不卡顿 错乱
     *
     * @param holder
     *
     *http://www.myexception.cn/android/2065627.html还有其对应方法
     *
     * 当条目离开可视化界面时 清除以前的动画
     */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //该方法clearAnimation  取消任何动画
        ((ViewHolder) holder).item_main_card.clearAnimation();
    }
    @Override
    public int getItemCount() {
        return storiesList == null ? 0 : storiesList.size();
    }

    //改变
    public void changeData(ArrayList<StoriesEntity> list) {
        storiesList = list;
        notifyDataSetChanged();
    }

    //添加
    public void addData(ArrayList<StoriesEntity> list) {
        if (storiesList == null) {
            changeData(list);
        } else {
            storiesList.addAll(list);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        SimpleDraweeView item_img;
        RelativeLayout item_main_rl;
        CardView item_main_card;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.item_main_txt_title);
            item_img = (SimpleDraweeView) itemView.findViewById(R.id.item_main_img);
            item_main_rl = (RelativeLayout) itemView.findViewById(R.id.item_main_rl);
            item_main_card = (CardView) itemView.findViewById(R.id.item_main_card);
        }
    }
}
