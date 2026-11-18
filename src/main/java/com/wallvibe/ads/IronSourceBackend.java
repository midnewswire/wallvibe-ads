package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;
import com.wallvibe.ads.listener.AdBannerListener;

/**
 * ironSource, via the official ironSource/LevelPlay mediation SDK.
 * <p>
 * Unlike the other four backends, ironSource's banner/interstitial API is a global static
 * facade ({@code IronSource.*}) rather than per-ad objects, so interstitial readiness is
 * checked directly with {@code IronSource.isInterstitialReady()} instead of going through
 * {@link AdNetwork}'s generic "loaded ad object" caching.
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
        IronSourceBannerLayout banner = IronSource.createBanner(activity, com.ironsource.mediationsdk.model.ISBannerSize.BANNER);
        banner.setBannerListener(new com.ironsource.mediationsdk.sdk.BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError error) {
                if (listener != null) listener.onAdFailedToLoad(error.getErrorMessage());
            }

            @Override
            public void onBannerAdClicked() {
            }

            @Override
            public void onBannerAdScreenPresented() {
            }

            @Override
            public void onBannerAdScreenDismissed() {
            }

            @Override
            public void onBannerAdLeftApplication() {
            }
        });
        container.removeAllViews();
        container.addView(banner);
        IronSource.loadBanner(banner);
    }

    static void loadInterstitial() {
        IronSource.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onInterstitialAdReady() {
            }

            @Override
            public void onInterstitialAdLoadFailed(IronSourceError error) {
            }

            @Override
            public void onInterstitialAdShowSucceeded() {
            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError error) {
            }

            @Override
            public void onInterstitialAdClicked() {
            }

            @Override
            public void onInterstitialAdOpened() {
            }

            @Override
            public void onInterstitialAdClosed() {
            }
        });
        IronSource.loadInterstitial();
    }

    static boolean showInterstitial() {
        if (!IronSource.isInterstitialReady()) return false;
        IronSource.showInterstitial();
        return true;
    }

    @SuppressWarnings("unused")
    private static String sdkVersion() {
        return IronSourceUtils.getSDKVersion();
    }
}
