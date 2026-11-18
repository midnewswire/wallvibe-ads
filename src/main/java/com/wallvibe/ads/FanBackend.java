package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.wallvibe.ads.listener.AdBannerListener;

/** Meta Audience Network (FAN), via the official Audience Network SDK. */
final class FanBackend {

    private FanBackend() {
    }

    static void init(Context context) {
        AudienceNetworkAds.buildInitSettings(context).initialize();
    }

    static void loadBanner(Activity activity, ViewGroup container, AdBannerListener listener) {
        AdView adView = new AdView(activity, AdConfig.ad_fan_banner_unit_id, AdSize.BANNER_HEIGHT_50);
        container.removeAllViews();
        container.addView(adView);
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (listener != null) listener.onAdFailedToLoad(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    static void loadInterstitial(Activity activity, AdNetwork.InterstitialReadyCallback callback) {
        InterstitialAd interstitialAd = new InterstitialAd(activity, AdConfig.ad_fan_interstitial_unit_id);
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                callback.onReady(null);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                callback.onReady(interstitialAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        }).build());
    }

    static boolean showInterstitial(Object loadedAd) {
        if (!(loadedAd instanceof InterstitialAd)) return false;
        InterstitialAd ad = (InterstitialAd) loadedAd;
        if (!ad.isAdLoaded() || ad.isAdInvalidated()) return false;
        ad.show();
        return true;
    }
}
