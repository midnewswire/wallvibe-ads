package com.wallvibe.ads;

import com.wallvibe.ads.data.AdNetworkType;

/**
 * Static config holder populated once at startup by the host app
 * (e.g. {@code AdNetworkHelper#init}). Field names mirror the old dreamspace-ads-sdk's
 * AdConfig so a consuming app's existing call site needed no changes.
 */
public class AdConfig {

    public static boolean ad_enable = true;
    public static boolean debug_mode = false;
    public static boolean enable_gdpr = true;
    public static AdNetworkType ad_network = AdNetworkType.ADMOB;
    public static int ad_inters_interval = 5;

    public static String ad_admob_publisher_id;
    public static String ad_admob_banner_unit_id;
    public static String ad_admob_interstitial_unit_id;

    public static String ad_fan_banner_unit_id;
    public static String ad_fan_interstitial_unit_id;

    public static String ad_ironsource_app_key;
    public static String ad_ironsource_banner_unit_id;
    public static String ad_ironsource_interstitial_unit_id;

    public static String ad_unity_game_id;
    public static String ad_unity_banner_unit_id;
    public static String ad_unity_interstitial_unit_id;

    public static String ad_applovin_banner_unit_id;
    public static String ad_applovin_interstitial_unit_id;

    private AdConfig() {
    }
}
