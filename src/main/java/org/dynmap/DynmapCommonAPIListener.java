package org.dynmap;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Listener class for dynmap API lifecycle - addresses issues with startup order, restarts, etc
 * Listeners should subclass, provide needed methods, and register listener using 
 * DynmapCommonAPIListener.register() method and unregister using 
 * DynmapCommonAPIListener.unregister()
 */
public abstract class DynmapCommonAPIListener {
    /**
     * Called when API becomes enabled, or during call to register listener if API is already enabled
     * 
     * @param api - API interface (note: may be platform specific subclass, such as bukkit-specific API)
     */
    public abstract void apiEnabled(DynmapCommonAPI api);
    /**
     * Called when API becomes disabled/obsolete
     * 
     * @param api - API interface being disabled (not usable immediately after call completes)
     */
    public void apiDisabled(DynmapCommonAPI api) {
    }
    
    private static DynmapCommonAPI dynmapapi = null;
    
    private static CopyOnWriteArrayList<DynmapCommonAPIListener> listeners = new CopyOnWriteArrayList<DynmapCommonAPIListener>();
    /**
     * Register listener instance
     * 
     * @param listener - listener to register
     */
    public static void register(DynmapCommonAPIListener listener) {
        listeners.add(listener);
        if(dynmapapi != null) {
            listener.apiEnabled(dynmapapi);
        }
    }
    /**
     * Unregister listener instance
     * 
     * @param listener - listener to unregister
     */
    public static void unregister(DynmapCommonAPIListener listener) {
        listeners.remove(listener);
    }
    // Internal call - MODS/PLUGINS MUST NOT USE
    public static void apiInitialized(DynmapCommonAPI api) {
        if(dynmapapi != null) {
            apiTerminated();
        }
        dynmapapi = api;
        if(dynmapapi != null) {
            for (DynmapCommonAPIListener l : listeners) {
                l.apiEnabled(api);
            }
        }
    }
    public static void apiTerminated() {
        if(dynmapapi != null) {
            for (DynmapCommonAPIListener l : listeners) {
                l.apiDisabled(dynmapapi);
            }
            dynmapapi = null;
        }
    }
}
