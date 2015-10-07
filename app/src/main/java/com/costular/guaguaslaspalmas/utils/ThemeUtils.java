package com.costular.guaguaslaspalmas.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by diego on 3/06/15.
 */
public class ThemeUtils {

    private Context mContext;

    public static final int DEFAULT = R.style.AppTheme;
    public static final int PINK = R.style.AppTheme_Pink;
    public static final int PURPLE = R.style.AppTheme_Purple;
    public static final int BLUE = R.style.AppTheme_Blue;
    public static final int GREEN = R.style.AppTheme_Green;
    public static final int ORANGE = R.style.AppTheme_Orange;
    public static final int GRAY = R.style.AppTheme_Gray;
    private String themeSaved;
    private int theme;
    private int mainColor;
    private int mainDarkColor;

    private static ThemeUtils instance = null;
    private TypedValue value = null;

    public static ThemeUtils getInstance(Context context) {
        if(instance == null) {
            instance = new ThemeUtils(context);
        }

        return instance;
    }

    public ThemeUtils(Context context) {
        this.mContext = context;
        themeSaved = PrefUtils.getTheme(context);

        int[] colors = getTheme(themeSaved);

        theme = colors[0];
        mainDarkColor = colors[1];
    }

    public int getTheme() {
        return theme;
    }

    public int getPrimaryDarkColor() {
        return mainDarkColor;
    }

    public void onThemeChanged(String themeName) {
        themeSaved = themeName;

        int[] colors = getTheme(themeName);
        theme = colors[0];
        mainDarkColor = colors[1];
    }

    public int[] getTheme(String str) {
        String theme = str;

        if("indigo".equalsIgnoreCase(theme)) {
            return new int[] {DEFAULT, mContext.getResources().getColor(R.color.main_indigo_dark)};
        }
        else if("pink".equalsIgnoreCase(theme)) {
            return new int[] {PINK, mContext.getResources().getColor(R.color.main_pink_dark)};
        }
        else if("purple".equalsIgnoreCase(theme)) {
            return new int[] {PURPLE, mContext.getResources().getColor(R.color.main_purple_dark)};
        }
        else if("blue".equalsIgnoreCase(theme)) {
            return new int[]{BLUE, mContext.getResources().getColor(R.color.main_blue_dark)};
        }
        else if("green".equalsIgnoreCase(theme)) {
            return new int[] {GREEN, mContext.getResources().getColor(R.color.main_green_dark)};
        }
        else if("orange".equalsIgnoreCase(theme)) {
            return new int[] {ORANGE, mContext.getResources().getColor(R.color.main_orange_dark)};
        }
        else if("gray".equalsIgnoreCase(theme)){
            return new int[] {GRAY, mContext.getResources().getColor(R.color.main_gray_dark)};
        }

        return new int[] {DEFAULT, mContext.getResources().getColor(R.color.main_indigo_dark)};
    }

    private int getColor(Context context, int id, int defaultValue){
        if(value == null)
            value = new TypedValue();

        try{
            Resources.Theme theme = context.getTheme();
            if(theme != null && theme.resolveAttribute(id, value, true)){
                if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
                    return value.data;
                else if (value.type == TypedValue.TYPE_STRING)
                    return context.getResources().getColor(value.resourceId);
            }
        }
        catch(Exception ex){}

        return defaultValue;
    }

    public void onStop() {
        PrefUtils.setTheme(mContext, themeSaved);
    }
}
