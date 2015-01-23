package ru.lizaalert.hotline.lib;

import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import java.util.Map;
import java.util.HashMap;

/**
 * Wrapper of Yandex Metrica
 */
public class YM {

    private static final String TAG = "8800";

    // Events name
    private static final String YM_EVENT_SEARCH = "Search";
    private static final String YM_EVENT_REGION = "Region";

    // Event attributes name
    private static final String YM_ATTR_SEARCH_QUERY = "Query";
    private static final String YM_ATTR_REGION_NAME = "Name";

    // Register search event
    public static void reportSearchEvent(String query) {
        if (query == null || query.isEmpty()) {
            Log.w(TAG, "Empty search query");
            return;
        }
        // event attributes
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(YM_ATTR_SEARCH_QUERY, query);

        YandexMetrica.reportEvent(YM_EVENT_SEARCH, attr);

        Log.d(TAG, String.format("reportSearchEvent: %s / %s", YM_EVENT_SEARCH, attr.toString()));
    }

    // Register region choosing event
    public static void reportRegionEvent(String regionName) {
        if (regionName == null || regionName.isEmpty()) {
            Log.w(TAG, "Empty region name");
            return;
        }
        // event attributes
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(YM_ATTR_REGION_NAME, regionName);

        YandexMetrica.reportEvent(YM_EVENT_REGION, attr);

        Log.d(TAG, String.format("reportRegionEvent: %s / %s", YM_EVENT_REGION, attr.toString()));
    }
}
