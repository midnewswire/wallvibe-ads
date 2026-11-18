package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.wallvibe.ads.listener.AdBannerListener;

/**
 * ironSource/LevelPlay, via the official mediation SDK.
 * <p>
 * Unlike the other four backends, ironSource's banner/interstitial API is a global static
 * facade ({@code IronSource.*}) rather than per-ad objects, so interstitial readiness is
 * checked directly with {@code IronSource.isInterstitialReady()} instead of going through
 * {@link AdNetwork}'s generic "loaded ad object" caching.
 * <p>
 * Verified against the real mediationsdk:8.3.0 AAR (javap on its classes.jar) after the SDK's
 * "LevelPlay" rebrand renamed the classic BannerListener/InterstitialListener interfaces to
 * LevelPlayBannerListener/LevelPlayInterstitialListener and moved ISBannerSize up a package.
 */
final class IronSourceBackend {

    private IronSourceBackend() {
    }

    static void init(Context context) {
        if (context instanceof Activity) {
            IronSource.init((Activity) context, AdConfig.ad_ironsource_app_key,
                    IronSource.AD_UNIT.BANNER, IronSource.AD_UNIT.INTERSTITIAL);
        }
    }

    static void loadBanner(Activity activity, ViewGroup container, AdBannerListener listener) {
        IronSourceBannerLayout banner = IronSource.createBanner(activity, ISBannerSize.BANNER);
        banner.setLevelPlayBannerListener(new LevelPlayBannerListener() {
            @Override
            public void onAdLoaded(AdInfo adInfo) {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onAdLoadFailed(IronSourceError error) {
                if (listener != null) listener.onAdFailedToLoad(error.getErrorMessage());
            }

            @Override
            public void onAdClicked(AdInfo adInfo) {
            }

            @Override
            public void onAdLeftApplication(AdInfo adInfo) {
            }

            @Override
            public void onAdScreenPresented(AdInfo adInfo) {
            }

            @Override
            public void onAdScreenDismissed(AdInfo adInfo) {
            }
        });
        container.removeAllViews();
        container.addView(banner);
        IronSource.loadBanner(banner);
    }

    static void loadInterstitial() {
        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {
            }

            @Override
            public void onAdLoadFailed(IronSourceError error) {
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {
            }

            @Override
            public void onAdShowFailed(IronSourceError error, AdInfo adInfo) {
            }

            @Override
            public void onAdClicked(AdInfo adInfo) {
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
            }
        });
        IronSource.loadInterstitial();
    }

    static boolean showInterstitial() {
        if (!IronSource.isInterstitialReady()) return false;
        IronSource.showInterstitial();
        return true;
    }
}
