package com.wallvibe.ads.listener;

/** Optional callback for {@code AdNetwork#loadBannerAd(boolean, ViewGroup, AdBannerListener)}. */
public interface AdBannerListener {
    default void onAdLoaded() {
    }

    default void onAdFailedToLoad(String message) {
    }
}
