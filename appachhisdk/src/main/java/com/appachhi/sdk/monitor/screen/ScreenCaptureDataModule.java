package com.appachhi.sdk.monitor.screen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.appachhi.sdk.BaseDataModule;
import com.appachhi.sdk.database.entity.Session;
import com.appachhi.sdk.sync.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY;
import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

/**
 * Module capturing screen shot at regular interval and saving it to the app specific directory
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureDataModule extends BaseDataModule<String> {
    private static final String TAG = "ScreenCaptureDataModule";
    private static final String SCREEN_CAP = "screen_capture";
    private Context appContext;
    private MediaProjectionManager projectionManager;
    private ImageReader imageReader;
    private HandlerThread handlerThread;
    private Handler handler;
    private VirtualDisplay vDisplay;
    private MediaProjection projection;
    private SessionManager sessionManager;
    private String lastFilePath;
    private Image image;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_dd_MM_mm_ss_SSS", Locale.ENGLISH);
    private Runnable compressAndSaveLatestImage;

    private int interval;


    ScreenCaptureDataModule(Context appContext, SessionManager sessionManager, final int interval) {
        this.appContext = appContext;
        projectionManager = (MediaProjectionManager) appContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        this.sessionManager = sessionManager;
        this.interval = interval;

        handlerThread = new HandlerThread(SCREEN_CAP);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        compressAndSaveLatestImage = new Runnable() {
            @Override
            public void run() {
                if (image != null) {
                    try {
                        compressCopyAndSave(image);
                    } catch (IllegalStateException e) {
                        Log.e(TAG, "Error when saving the image");
                    } catch (RuntimeException e) {
                        // This exception should be ignored as the image is closed when bitmap is being
                        // copied
                    }
                }
                if (handler != null) {
                    handler.postDelayed(this, ScreenCaptureDataModule.this.interval);
                }
            }
        };
    }


    @Override
    protected String getData() {
        return lastFilePath;
    }

    @Override
    public void start() {
        handler.removeCallbacks(compressAndSaveLatestImage);
        handler.post(compressAndSaveLatestImage);
    }

    @Override
    public void stop() {
        // Will not the projection
    }

    void stopProjection() {
        if (projection != null) {
            projection.stop();
            projection = null;
        }
        if (image != null) {
            image.close();
            image = null;
            lastFilePath = null;
        }

        if (vDisplay != null) {
            vDisplay.release();
            vDisplay = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    void createMediaProjection(Activity activity) {
        Log.d(TAG, "createMediaProjection: Entered");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (vDisplay == null) {
                Log.d(TAG, "createMediaProjection: Intent state");

                Intent intent = projectionManager.createScreenCaptureIntent();
                activity.startActivityForResult(intent, 34);
            }
        }
    }


    void handleMediaProjectionResult(int resultCode, Intent result) {
        projection = projectionManager.getMediaProjection(resultCode, result);
        if (projection != null) {
            final Display display = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            final Point size = new Point();

            display.getRealSize(size);
            final int width = size.x, height = size.y;
            final int density = appContext.getResources().getDisplayMetrics().densityDpi;
            imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
            vDisplay = projection.createVirtualDisplay(SCREEN_CAP, width, height, density,
                    VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | VIRTUAL_DISPLAY_FLAG_PUBLIC,
                    imageReader.getSurface(), virtualDisplayCallback, handler);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {

                    ScreenCaptureDataModule.this.onImageAvailable(imageReader);

                }
            }, handler);

        }
    }

    private void onImageAvailable(ImageReader reader) {
        if (image != null) {
            image.close();
            image = null;
        }
        image = reader.acquireLatestImage();
    }

    private void compressCopyAndSave(Image image) {
        final int width = image.getWidth(), height = imageReader.getHeight();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride(), rowStride = planes[0].getRowStride(), rowPadding = rowStride - pixelStride * width;
        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_4444);
        bitmap.copyPixelsFromBuffer(buffer);

        Session session = sessionManager.getCurrentSession();
        if (session != null) {
            // Create File
            File folderPath = appContext.getExternalFilesDir(String.format("screens/%s", session.getId()));
            File file = new File(folderPath, String.format("IMG_%s.jpeg", DATE_FORMAT.format(new Date())));
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                // Compress
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, fileOutputStream);
                lastFilePath = file.getAbsolutePath();
                notifyObservers();
                bitmap.recycle();
            } catch (FileNotFoundException e) {
             //
            }
        }
    }

    private VirtualDisplay.Callback virtualDisplayCallback = createVirtualDisplayCallback();

    private VirtualDisplay.Callback createVirtualDisplayCallback() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return new VirtualDisplay.Callback() {
                @Override
                public void onStopped() {
                    super.onStopped();
                    if (vDisplay != null) {
                        vDisplay.release();
                        vDisplay = null;
                    }
                    if (imageReader != null) {
                        imageReader.close();
                        imageReader = null;
                    }
                }
            };

        }
        return null;
    }

}
