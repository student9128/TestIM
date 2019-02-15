package com.kevin.testim.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by <a href="http://blog.csdn.net/student9128">Kevin</a> for Project Mellow on 2017/9/12.
 * <h3>Description:</h3>
 * <div>
 * <div/>
 */


public class DisplayUtils {
    /**
     *
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * convert dp to its equivalent px
     */
    public static int dpTpx(Context context,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,context.getResources().getDisplayMetrics());
    }

    /**
     * convert sp to its equivalent px
     */
    public static int spTpx(Context context,int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,context.getResources().getDisplayMetrics());
    }

}
