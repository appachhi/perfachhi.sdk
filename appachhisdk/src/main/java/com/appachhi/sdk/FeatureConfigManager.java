package com.appachhi.sdk;

import com.appachhi.sdk.instrument.transition.ScreenTransitionFeatureModule;
import com.appachhi.sdk.monitor.cpu.CpuUsageInfoFeatureModule;
import com.appachhi.sdk.monitor.fps.FpsFeatureModule;
import com.appachhi.sdk.monitor.memory.MemoryInfoFeatureModule;
import com.appachhi.sdk.monitor.network.NetworkFeatureModule;

import java.util.List;

public class FeatureConfigManager {

    private List<? extends FeatureModule> featureModules;

    public FeatureConfigManager(List<? extends FeatureModule> featureModules) {
        this.featureModules = featureModules;
    }

    public boolean isScreenTransitionOverlayEnabled() {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof ScreenTransitionFeatureModule) {
                ScreenTransitionFeatureModule transitionFeatureModule = (ScreenTransitionFeatureModule) featureModule;
                return transitionFeatureModule.isOverlayEnabled();
            }
        }
        return false;
    }

    public boolean setScreenTransitionOverlay(boolean enabled) {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof ScreenTransitionFeatureModule) {
                ScreenTransitionFeatureModule transitionFeatureModule = (ScreenTransitionFeatureModule) featureModule;
                transitionFeatureModule.setOverlayEnabled(enabled);
                return true;
            }
        }
        return false;
    }

    public boolean isMemoryInfoOverlayEnabled() {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof MemoryInfoFeatureModule) {
                MemoryInfoFeatureModule memoryInfoFeatureModule = (MemoryInfoFeatureModule) featureModule;
                return memoryInfoFeatureModule.isOverlayEnabled();
            }
        }
        return false;
    }

    public boolean setMemoryInfoOverlayEnabled(boolean enabled) {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof MemoryInfoFeatureModule) {
                MemoryInfoFeatureModule memoryInfoFeatureModule = (MemoryInfoFeatureModule) featureModule;
                memoryInfoFeatureModule.setOverlayEnabled(enabled);
                return true;
            }
        }
        return false;
    }

    public boolean isNetworkOverlayEnabled() {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof NetworkFeatureModule) {
                NetworkFeatureModule networkFeatureModule = (NetworkFeatureModule) featureModule;
                return networkFeatureModule.isOverlayEnabled();
            }
        }
        return false;
    }

    public boolean setNetworkOverlayEnabled(boolean enabled) {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof NetworkFeatureModule) {
                NetworkFeatureModule networkFeatureModule = (NetworkFeatureModule) featureModule;
                networkFeatureModule.setOverlayEnabled(enabled);
                return true;
            }
        }
        return false;
    }

    public boolean isCpuUsageOverlayEnabled() {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof CpuUsageInfoFeatureModule) {
                CpuUsageInfoFeatureModule cpuUsageInfoFeatureModule = (CpuUsageInfoFeatureModule) featureModule;
                return cpuUsageInfoFeatureModule.isOverlayEnabled();
            }
        }
        return false;
    }

    public boolean setCpuUsageOverlayEnabled(boolean enabled) {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof CpuUsageInfoFeatureModule) {
                CpuUsageInfoFeatureModule cpuUsageInfoFeatureModule = (CpuUsageInfoFeatureModule) featureModule;
                cpuUsageInfoFeatureModule.setOverlayEnabled(enabled);
                return true;
            }
        }
        return false;
    }

    public boolean isFpsOverlayEnabled() {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof FpsFeatureModule) {
                FpsFeatureModule fpsFeatureModule = (FpsFeatureModule) featureModule;
                return fpsFeatureModule.isOverlayEnabled();
            }
        }
        return false;
    }

    public boolean setFpsOverlayEnabled(boolean enabled) {
        for (FeatureModule featureModule : this.featureModules) {
            if (featureModule instanceof FpsFeatureModule) {
                FpsFeatureModule fpsFeatureModule = (FpsFeatureModule) featureModule;
                fpsFeatureModule.setOverlayEnabled(enabled);
                return true;
            }
        }
        return false;
    }

    public boolean isMemoryLeakOverlayEnabled() {
//        for (FeatureModule featureModule : this.featureModules) {
//            if (featureModule instanceof MemoryLeakFeatureModule) {
//                MemoryLeakFeatureModule memoryLeakFeatureModule = (MemoryLeakFeatureModule) featureModule;
//                return memoryLeakFeatureModule.isOverlayEnabled();
//            }
//        }
        return false;
    }

    public boolean setMemoryLeakOverlayEnabled(boolean enabled) {
//        for (FeatureModule featureModule : this.featureModules) {
//            if (featureModule instanceof MemoryLeakFeatureModule) {
//                MemoryLeakFeatureModule memoryLeakFeatureModule = (MemoryLeakFeatureModule) featureModule;
//                memoryLeakFeatureModule.setOverlayEnabled(enabled);
//                return true;
//            }
//        }
        return false;
    }

}
