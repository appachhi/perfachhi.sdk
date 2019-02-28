package com.appachhi.sdk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.List;

import static android.view.WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;

class OverlayViewManager {
    private static final String TAG = "OverlayViewManager";

    private final Context context;
    private final Appachhi.Config config;
    private final WindowManager windowManager;

    private List<FeatureModule> featureModules = Collections.emptyList();
    private ViewGroup rootView;

    private boolean overlayPermissionRequested;

    OverlayViewManager(@NonNull Context context, Appachhi.Config config) {
        this.context = context;
        this.config = config;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    void setFeatureModules(@NonNull List<FeatureModule> overlayModules) {
        this.featureModules = overlayModules;
    }

    void showDebugSystemOverlay() {
        if (config.isOverlayAllowed() && rootView == null) {
            if (!canDrawOnSystemLayer(context, getWindowTypeForOverlay(true))) {
                requestDrawOnSystemLayerPermission(context);
                overlayPermissionRequested = true;
                return;
            }

            overlayPermissionRequested = false;

            rootView = createRoot();

            int layoutParamsWidth = WindowManager.LayoutParams.WRAP_CONTENT;

            for (FeatureModule overlayModule : featureModules) {
                View view = overlayModule.createView(rootView);
                if (view != null && view.getParent() == null) {
                    if (view.getLayoutParams() != null && view.getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
                        layoutParamsWidth = WindowManager.LayoutParams.MATCH_PARENT;
                    }
                    rootView.addView(view);
                }
            }

            WindowManager.LayoutParams params = createLayoutParams(config.isOverlayAllowed(), layoutParamsWidth, null);
            windowManager.addView(rootView, params);
        }
    }

    void hideDebugSystemOverlay() {
        if (config.isOverlayAllowed() && rootView != null) {
            windowManager.removeView(rootView);
            rootView = null;
        }
    }

    boolean isSystemOverlayShown() {
        return rootView != null;
    }

    boolean isOverlayPermissionRequested() {
        return overlayPermissionRequested;
    }

    OverlayViewAttachStateChangeListener createAttachStateChangeListener() {
        return new OverlayViewAttachStateChangeListener();
    }

    private WindowManager.LayoutParams createLayoutParams(boolean allowSystemLayer, int width, IBinder windowToken) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = width;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (windowToken != null) {
            layoutParams.token = windowToken;
        }
        //noinspection WrongConstant
        layoutParams.type = getWindowTypeForOverlay(allowSystemLayer);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.END;
        return layoutParams;
    }

    private ViewGroup createRoot() {
        return (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.overlay_container, null);
    }

    class OverlayViewAttachStateChangeListener implements View.OnAttachStateChangeListener {

        private ViewGroup _rootView;

        void onActivityResumed() {
            if (_rootView != null && _rootView.getChildCount() > 0) {
                _rootView.removeAllViews();
                for (FeatureModule featureModule : featureModules) {
                    View view = featureModule.createView(_rootView);
                    if (view != null && view.getParent() == null) {
                        _rootView.addView(view);
                    }
                }
                // force-update recreated views with the latest data
                for (FeatureModule featureModule : featureModules) {
                    featureModule.notifyObservers();
                }
            }
        }

        @Override
        public void onViewAttachedToWindow(View v) {
            _rootView = createRoot();
            int layoutParamsWidth = WindowManager.LayoutParams.WRAP_CONTENT;
            for (FeatureModule overlayModule : featureModules) {
                View view = overlayModule.createView(_rootView);
                if (view != null && view.getParent() == null) {
                    if (view.getLayoutParams() != null && view.getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
                        layoutParamsWidth = WindowManager.LayoutParams.MATCH_PARENT;
                    }
                    _rootView.addView(view);
                }
            }

            windowManager.addView(_rootView, createLayoutParams(config.isOverlayAllowed(),
                    layoutParamsWidth, v.getWindowToken()));
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            v.removeOnAttachStateChangeListener(this);
            windowManager.removeViewImmediate(_rootView);
        }
    }

    private static void requestDrawOnSystemLayerPermission(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // request permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    static boolean canDrawOnSystemLayer(@NonNull Context context, int systemWindowType) {
        if (isSystemLayer(systemWindowType)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                return Settings.canDrawOverlays(context);
            } else if (systemWindowType == TYPE_TOAST) {
                // since 7.1.1, TYPE_TOAST is not usable since it auto-disappears
                // otherwise, just use it since it does not require any special permission
                return true;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(context);
            } else {
                return hasSystemAlertPermission(context);
            }
        }
        return true;
    }

    private static boolean hasSystemAlertPermission(@NonNull Context context) {
        return PermissionChecker.checkSelfPermission(context, Manifest.permission.SYSTEM_ALERT_WINDOW)
                == PermissionChecker.PERMISSION_GRANTED;
    }

    private static boolean isSystemLayer(int windowType) {
        return windowType >= FIRST_SYSTEM_WINDOW;
    }

    static int getWindowTypeForOverlay(boolean allowSystemLayer) {
        if (allowSystemLayer) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return TYPE_APPLICATION_OVERLAY;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                return TYPE_SYSTEM_ALERT;
            } else {
                return TYPE_TOAST;
            }
        } else {
            // make layout of the window happens as that of a top-level window, not as a child of its container
            return TYPE_APPLICATION_ATTACHED_DIALOG;
        }
    }
}
