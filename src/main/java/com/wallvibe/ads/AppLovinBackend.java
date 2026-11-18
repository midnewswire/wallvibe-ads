package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.wallvibe.ads.listener.AdBannerListener;

/** AppLovin, via the official AppLovin MAX SDK. */
final class AppLovinBackend {

    private AppLovinBackend() {
    }

    static void init(Context context) {
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.getInstance(context).initializeSdk(config -> {
        });
    }

    static void loadBanner(Activity activity, ViewGroup container, AdBannerListener listener) {
        MaxAdView adView = new MaxAdView(AdConfig.ad_applovin_banner_unit_id, activity);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                if (listener != null) listener.onAdFailedToLoad(error.getMessage());
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
            }

            @Override
            public void onAdHidden(MaxAd ad) {
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }

            @Override
            public void onAdExpanded(MaxAd ad) {
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
            }
        });
        adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        container.removeAllViews();
        container.addView(adView);
        adView.loadAd();
    }

    static void loadInterstitial(Activity activity, AdNetwork.InterstitialReadyCallback callback) {
        MaxInterstitialAd interstitialAd = new MaxInterstitialAd(AdConfig.ad_applovin_interstitial_unit_id, activity);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                callback.onReady(interstitialAd);
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                callback.onReady(null);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
            }

            @Override
            public void onAdHidden(MaxAd ad) {
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        });
        interstitialAd.loadAd();
    }

    static boolean showInterstitial(Object loadedAd) {
        if (!(loadedAd instanceof MaxInterstitialAd)) return false;
        MaxInterstitialAd ad = (MaxInterstitialAd) loadedAd;
        if (!ad.isReady()) return false;
        ad.showAd();
        return true;
    }
}
