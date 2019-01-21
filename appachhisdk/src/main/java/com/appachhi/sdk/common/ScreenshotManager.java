package com.appachhi.sdk.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

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
    private Executor executor = Executors.newSingleThreadExecutor();
    @SuppressWarnings("FieldCanBeLocal")
    private String tag = "ScreenshotManager";

    private ScreenshotManager() {
    }

    public static ScreenshotManager getInstance() {
        synchronized (ScreenshotManager.class) {
            if (instance == null) {
                instance = new ScreenshotManager();
            }
            return instance;
        }
    }

    /**
     * Take the screenshot and saves them
     *
     * @param activity   {@link Activity} where screen exist
     * @param screenName Name of the screen
     */
    public void takeAndSave(@NonNull Activity activity, String screenName) {
        View view = activity.getWindow().getDecorView();
        takeAndSave(view, screenName);
    }

    /**
     * Take the screenshot and saves them
     *
     * @param view       View for which screenshot is to be taken
     * @param screenName Name of the screen
     */
    private void takeAndSave(@NonNull final View view, final String screenName) {
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

    /**
     * Saves the {@link Bitmap} as {@link android.graphics.Bitmap.CompressFormat#PNG} in app
     * specific directory.All the operation here are done in worker thread to prevent UI thread being
     * blocked due to File Write Operation.
     * <p>
     * File Naming format is ScreeName-SomeNumber.PNG
     *
     * @param applicationContext Application Context
     * @param bitmap             Bitmap to be saved
     * @param screenName         Screen Name
     */
    @WorkerThread
    private void saveScreenshot(Context applicationContext, final Bitmap bitmap, String screenName) {
        File folder = applicationContext.getExternalFilesDir("appachhi");
        String fileName = String.format(Locale.ENGLISH, "%s-%d.png", screenName, SystemClock.elapsedRealtime());
        final File file = new File(folder, fileName);
        executor.execute(() -> saveScreenshot(bitmap, file));
    }

    private void saveScreenshot(Bitmap bitmap, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            // Convert Image to PNG without loosing quality
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            // Release the memory allocated to the bitmap
            bitmap.recycle();
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
