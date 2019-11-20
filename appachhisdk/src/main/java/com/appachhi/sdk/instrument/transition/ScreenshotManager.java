package com.appachhi.sdk.instrument.transition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ScreenshotManager is responsible for taking screenshot and saves them
 */
public class ScreenshotManager {
    private static ScreenshotManager instance;
    private static boolean isRunningFromReactNative = false;
    private Handler mainHandler = new Handler(Looper.myLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    @SuppressWarnings("FieldCanBeLocal")
    private String tag = "ScreenshotManager";

    ScreenshotManager() {
    }

    /**
     * Called by the React Module and should not used by the caller.It adds a field to check if the
     * code is running from React Native Bridge
     *
     * @param isRunningFromReactNative true if running from react native
     */
    @Keep
    public static void setIsRunningFromReactNative(boolean isRunningFromReactNative) {
        ScreenshotManager.isRunningFromReactNative = isRunningFromReactNative;
    }

    /**
     * Take the screenshot and saves them
     *
     * @param activity   {@link Activity} where screen exist
     * @param screenName Name of the screen
     */
    void takeAndSave(@NonNull Activity activity, final String screenName) {
        final View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (isRunningFromReactNative) {
            // Adding delay for react native screenshot
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    takeAndSave(view, screenName);
                }
            }, 1000);
        } else {
            // Immediate screenshot
            takeAndSave(view, screenName);
        }
    }

    /**
     * Take the screenshot and saves them if and only if the view has been created atleast
     *
     * @param view       View for which screenshot is to be taken
     * @param screenName Name of the screen
     */
    private void takeAndSave(@Nullable final View view, final String screenName) {
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap bitmap = takeScreenShot(view);
                    saveScreenshot(view.getContext().getApplicationContext(), bitmap, screenName);
                    return true;
                }
            });
        }
    }

    /**
     * Saves the {@link Bitmap} as {@link android.graphics.Bitmap.CompressFormat#PNG} in app
     * specific directory.All the operation here are done in worker thread to prevent UI thread being
     * blocked due to File Write Operation.ScreenshotEntity will be saved only if the bitmap is not null
     * <p>
     * File Naming format is ScreeName-SomeNumber.PNG
     *
     * @param applicationContext Application Context
     * @param bitmap             Bitmap to be saved which can be null
     * @param screenName         Screen Name
     */
    @WorkerThread
    private void saveScreenshot(Context applicationContext, @Nullable final Bitmap bitmap, String screenName) {
        if (bitmap != null) {
            File folder = applicationContext.getExternalFilesDir("appachhi");
            String fileName = String.format(Locale.ENGLISH, "%s-%d.png", screenName, SystemClock.elapsedRealtime());
            final File file = new File(folder, fileName);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    saveScreenshot(bitmap, file);
                }
            });
        }
    }

    private void saveScreenshot(@NonNull Bitmap bitmap, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            // Convert Image to PNG without loosing quality
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            Log.d(tag, String.format("ScreenshotManager saved at %s", file.getAbsolutePath()));
        } catch (IOException e) {
            Log.e(tag, "Failed to save the screenshot", e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Release the memory allocated to the bitmap
            bitmap.recycle();
        }
    }

    /**
     * Extract {@link Bitmap} from a given {@link View}
     *
     * @param view View
     * @return Bitmap Extracted {@link Bitmap}
     */
    private Bitmap takeScreenShot(View view) {
        View rootView = view.getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        // creates immutable clone
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
