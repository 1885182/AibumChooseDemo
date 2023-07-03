package com.ren.newalbumchoose.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/23
 *     desc  : utils about bar
 * </pre>
 */
public final class BarUtils {

    ///////////////////////////////////////////////////////////////////////////
    // status bar
    ///////////////////////////////////////////////////////////////////////////

    private static final String TAG_STATUS_BAR = "TAG_STATUS_BAR";
    private static final String TAG_OFFSET     = "TAG_OFFSET";
    private static final int    KEY_OFFSET     = -123;

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the status bar's height.
     *
     * @return the status bar's height
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }







    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     */
    public static View setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color) {
        return setStatusBarColor(activity, color, false);
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param isDecor  True to add fake status bar in DecorView,
     *                 false to add fake status bar in ContentView.
     */
    public static View setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null;
        transparentStatusBar(activity);
        return applyStatusBarColor(activity, color, isDecor);
    }



    /**
     * Set the status bar's color.
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     */
    public static void setStatusBarColor(Activity activity,@NonNull final View fakeStatusBar,
                                         @ColorInt final int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
         if (activity == null) return;
        transparentStatusBar(activity);
        fakeStatusBar.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight(activity);
        fakeStatusBar.setBackgroundColor(color);
    }

    /**
     * Set the custom status bar.
     *
     * @param fakeStatusBar The fake status bar view.
     */
    public static void setStatusBarCustom(Activity activity,@NonNull final View fakeStatusBar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
         if (activity == null) return;
        transparentStatusBar(activity);
        fakeStatusBar.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity)
            );
            fakeStatusBar.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = getStatusBarHeight(activity);
        }
    }


    private static View applyStatusBarColor(@NonNull final Activity activity,
                                            final int color,
                                            boolean isDecor) {
        return applyStatusBarColor(activity.getWindow(), color, isDecor);
    }

    private static View applyStatusBarColor(@NonNull final Window window,
                                            final int color,
                                            boolean isDecor) {
        ViewGroup parent = isDecor ?
                (ViewGroup) window.getDecorView() :
                (ViewGroup) window.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            fakeStatusBarView = createStatusBarView(window.getContext(), color);
            parent.addView(fakeStatusBarView);
        }
        return fakeStatusBarView;
    }

    private static void hideStatusBarView(@NonNull final Activity activity) {
        hideStatusBarView(activity.getWindow());
    }

    private static void hideStatusBarView(@NonNull final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void showStatusBarView(@NonNull final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static View createStatusBarView(@NonNull final Context context,
                                            final int color) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context)));
        statusBarView.setBackgroundColor(color);
        statusBarView.setTag(TAG_STATUS_BAR);
        return statusBarView;
    }

    public static void transparentStatusBar(@NonNull final Activity activity) {
        transparentStatusBar(activity.getWindow());
    }

    public static void transparentStatusBar(@NonNull final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int vis = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(option | vis);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // action bar
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // notification bar
    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////
    // navigation bar
    ///////////////////////////////////////////////////////////////////////////






    /**
     * Set the navigation bar's color.
     *
     * @param activity The activity.
     * @param color    The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Activity activity, @ColorInt final int color) {
        setNavBarColor(activity.getWindow(), color);
    }

    /**
     * Set the navigation bar's color.
     *
     * @param window The window.
     * @param color  The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Window window, @ColorInt final int color) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
    }

    /**
     * Return the color of navigation bar.
     *
     * @param activity The activity.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Activity activity) {
        return getNavBarColor(activity.getWindow());
    }

    /**
     * Return the color of navigation bar.
     *
     * @param window The window.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Window window) {
        return window.getNavigationBarColor();
    }



    /**
     * Set the nav bar's light mode.
     *
     * @param activity    The activity.
     * @param isLightMode True to set nav bar light mode, false otherwise.
     */
    public static void setNavBarLightMode(@NonNull final Activity activity,
                                          final boolean isLightMode) {
        setNavBarLightMode(activity.getWindow(), isLightMode);
    }

    /**
     * Set the nav bar's light mode.
     *
     * @param window      The window.
     * @param isLightMode True to set nav bar light mode, false otherwise.
     */
    public static void setNavBarLightMode(@NonNull final Window window,
                                          final boolean isLightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = window.getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (isLightMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    /**
     * Is the nav bar light mode.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarLightMode(@NonNull final Activity activity) {
        return isNavBarLightMode(activity.getWindow());
    }

    /**
     * Is the nav bar light mode.
     *
     * @param window The window.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarLightMode(@NonNull final Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = window.getDecorView();
            int vis = decorView.getSystemUiVisibility();
            return (vis & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0;
        }
        return false;
    }
}
