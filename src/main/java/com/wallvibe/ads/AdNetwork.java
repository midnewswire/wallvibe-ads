package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.wallvibe.ads.listener.AdBannerListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loads/shows banner and interstitial ads by calling straight into whichever official ad
 * network SDK {@link AdConfig#ad_network} selects (AdMob, Meta Audience Network, Unity Ads,
 * ironSource, or AppLovin/MAX) - no third-party wrapper SDK, no license key.
 * <p>
 * This is a from-scratch, best-effort implementation written against each network's public
 * docs. It has NOT been compiled or run in this environment (no Android SDK / network access
 * here), the per-network SDK APIs do change between releases, and only the network you actually
 * configure real unit IDs for (see the host app's ad config) needs to work - build and
 * smoke-test in Android Studio before shipping, and expect to adjust a method signature or two
 * to match whatever SDK version you land on.
 * <p>
 * Public method names mirror the old dreamspace-ads-sdk's AdNetwork so a consuming app's
 * existing call site needed no changes beyond the import.
 */
public class AdNetwork {

    private static final String TAG = "WallVibeAds";

    /** App-wide "show every Nth interstitial request" counter, shared across activities. */
    private static final AtomicInteger interstitialCounter = new AtomicInteger(0);
    private static volatile boolean initialized = false;

    private final Activity activity;

    /** Holds whatever ad-network-specific "loaded interstitial" object is currently cached. */
    private Object loadedInterstitial;

    public AdNetwork(Activity activity) {
        this.activity = activity;
    }

    public static void init(Context context) {
        if (initialized || !AdConfig.ad_enable) return;
        initialized = true;
        try {
            switch (AdConfig.ad_network) {
                case ADMOB:
                    AdMobBackend.init(context);
                    break;
                case FAN:
                    FanBackend.init(context);
                    break;
                case UNITY:
                    UnityBackend.init(context);
                    break;
                case IRONSOURCE:
                    IronSourceBackend.init(context);
                    break;
                case APPLOVIN:
                    AppLovinBackend.init(context);
                    break;
            }
        } catch (Throwable t) {
            // Never let a missing/misconfigured ad network SDK take the app down.
            Log.e(TAG, "Ad network init failed", t);
        }
    }

    public void loadBannerAd(boolean enable, ViewGroup container) {
        loadBannerAd(enable, container, null);
    }

    public void loadBannerAd(boolean enable, ViewGroup container, AdBannerListener listener) {
        if (!enable || !AdConfig.ad_enable || container == null) return;
        try {
            switch (AdConfig.ad_network) {
                case ADMOB:
                    AdMobBackend.loadBanner(activity, container, listener);
                    break;
                case FAN:
                    FanBackend.loadBanner(activity, container, listener);
                    break;
                case UNITY:
                    UnityBackend.loadBanner(activity, container, listener);
                    break;
                case IRONSOURCE:
                    IronSourceBackend.loadBanner(activity, container, listener);
                    break;
                case APPLOVIN:
                    AppLovinBackend.loadBanner(activity, container, listener);
                    break;
            }
        } catch (Throwable t) {
            Log.e(TAG, "Banner load failed", t);
            if (listener != null) listener.onAdFailedToLoad(t.getMessage());
        }
    }

    public void loadInterstitialAd(boolean enable) {
        if (!enable || !AdConfig.ad_enable) return;
        try {
            switch (AdConfig.ad_network) {
                case ADMOB:
                    AdMobBackend.loadInterstitial(activity, ad -> loadedInterstitial = ad);
                    break;
                case FAN:
                    FanBackend.loadInterstitial(activity, ad -> loadedInterstitial = ad);
                    break;
                case UNITY:
                    UnityBackend.loadInterstitial(ad -> loadedInterstitial = ad);
                    break;
                case IRONSOURCE:
                    IronSourceBackend.loadInterstitial();
                    break;
                case APPLOVIN:
                    AppLovinBackend.loadInterstitial(activity, ad -> loadedInterstitial = ad);
                    break;
            }
        } catch (Throwable t) {
            Log.e(TAG, "Interstitial load failed", t);
        }
    }

    /** @return true if an interstitial was actually shown this call. */
    public boolean showInterstitialAd(boolean enable) {
        if (!enable || !AdConfig.ad_enable) return false;
        int interval = Math.max(1, AdConfig.ad_inters_interval);
        if (interstitialCounter.incrementAndGet() < interval) return false;
        interstitialCounter.set(0);

        boolean shown = false;
        try {
            switch (AdConfig.ad_network) {
                case ADMOB:
                    shown = AdMobBackend.showInterstitial(activity, loadedInterstitial);
                    break;
                case FAN:
                    shown = FanBackend.showInterstitial(loadedInterstitial);
                    break;
                case UNITY:
                    shown = UnityBackend.showInterstitial(activity, loadedInterstitial);
                    break;
                case IRONSOURCE:
                    shown = IronSourceBackend.showInterstitial();
                    break;
                case APPLOVIN:
                    shown = AppLovinBackend.showInterstitial(loadedInterstitial);
                    break;
            }
        } catch (Throwable t) {
            Log.e(TAG, "Interstitial show failed", t);
        }
        loadedInterstitial = null;
        // reload for next time regardless of whether this one showed
        loadInterstitialAd(true);
        return shown;
    }

    interface InterstitialReadyCallback {
        void onReady(Object loadedAd);
    }
}
