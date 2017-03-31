package com.wpy.zhihu.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.wpy.zhihu.bean.StoriesEntity;
import com.wpy.zhihu.view.activity.HomeDesActivity;

import java.util.ArrayList;

/**
 *  Intent  跳转工具
 */
public class IntentUtils {

    public static void toHomeDesActivity(Context context, int pos, ArrayList<StoriesEntity> storiesEntityList) {
        Intent intent = new Intent(context, HomeDesActivity.class);
        intent.putExtra(HomeDesActivity.PARAM_POS, pos);
        intent.putParcelableArrayListExtra(HomeDesActivity.PARAM_DATA_LIST, storiesEntityList);
        context.startActivity(intent);
    }

    /*public static void toTRClientActivity(Context context) {
        context.startActivity(new Intent(context, TRClientActivity.class));
    }

    public static void toSettingActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }*/

    public static void toBrowserView(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
