package com.deeper.popularmovies.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    private final static String TAG = "Utility";

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Point getWindowSize(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point screen = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(screen);
        } else {
            screen.x = display.getWidth();
            screen.y = display.getHeight();
        }

        return screen;
    }

    public static Double getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);

        Log.d(TAG, "SCREENSIZE: " + screenInches + " WIDTH: " + x + " HEIGHT: " + y);

        return screenInches;
    }

    public static String getConnectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            String type = ni.getTypeName().toLowerCase(Locale.ENGLISH);
            Log.d(TAG, "CONNECTION: " + type + " CONNECTED: " + ni.isConnected());
            if (ni.isConnected()) {
                return type;
            }
        }

        return "";
    }

    public static int getOrientation(Activity activity){
        if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return Configuration.ORIENTATION_PORTRAIT;
        else
            return Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void releaseOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    public static void lockOrientationPortrait(Activity activity) {
    	activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }
    
    public static void lockOrientation(Activity activity) {
        switch (activity.getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO){
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                    if(rotation == android.view.Surface.ROTATION_90|| rotation == android.view.Surface.ROTATION_180){
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO){
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                    if(rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_90){
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                }
                break;
        }
    }

    public static int getViewHeightPixel(Activity activity, View view){
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.measure(display.getWidth(), display.getHeight());

        return view.getMeasuredHeight(); //view height
    }

    public static int getViewWidthDPixel(Activity activity, View view){
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.measure(display.getWidth(), display.getHeight());

        return view.getMeasuredWidth(); // view width
    }

    public static int getViewHeightDP(Activity activity, View view){
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.measure(display.getWidth(), display.getHeight());

        int height = view.getMeasuredHeight(); //view height

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = height / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int getViewWidthDP(Activity activity, View view){
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.measure(display.getWidth(), display.getHeight());

        int width = view.getMeasuredWidth(); // view width

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = width / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
    
    public static boolean isNormalScreen(Context context) {
        DisplayMetrics displayMetrics= context.getResources().getDisplayMetrics();
        float screenHeightInDp=displayMetrics.heightPixels/displayMetrics.density + getStatusBarHeight(context);
        float screenWidthInDp=displayMetrics.widthPixels/displayMetrics.density;

        if (screenHeightInDp < screenWidthInDp) {
            screenWidthInDp = screenHeightInDp;
        }

        return screenWidthInDp < 600;
	}
	
	public static boolean isLargeScreen(Context context) {
        DisplayMetrics displayMetrics= context.getResources().getDisplayMetrics();
        float screenHeightInDp=displayMetrics.heightPixels/displayMetrics.density + getStatusBarHeight(context);
        float screenWidthInDp=displayMetrics.widthPixels/displayMetrics.density;

        if (screenHeightInDp < screenWidthInDp) {
            screenWidthInDp = screenHeightInDp;
        }

        return screenWidthInDp >= 600 && screenWidthInDp < 720;
	}
	
	public static boolean isXLargeScreen(Context context) {
        DisplayMetrics displayMetrics= context.getResources().getDisplayMetrics();
        float screenHeightInDp=displayMetrics.heightPixels/displayMetrics.density + getStatusBarHeight(context);
        float screenWidthInDp=displayMetrics.widthPixels/displayMetrics.density;

        if (screenHeightInDp < screenWidthInDp) {
            screenWidthInDp = screenHeightInDp;
        }

        return screenWidthInDp >= 720;
	}

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            return dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
