/**
 * Created by luka on 20/12/17.
 */
package com.ontariolegalpool.olp;

import android.support.v4.util.LruCache;

public class MemCache {

    private static MemCache instance;
    private LruCache<Object, Object> lru;

    private MemCache() {

        lru = new LruCache<Object, Object>(1024);

    }

    public static boolean cacheExists() {
        if (instance == null) {
            return false;
        }
        return true;
    }

    public static MemCache getInstance() {

        if (instance == null) {

            instance = new MemCache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
