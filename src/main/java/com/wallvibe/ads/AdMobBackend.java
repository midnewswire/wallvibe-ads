package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.wallvibe.ads.listener.AdBannerListener;

/** Google AdMob, via the official Google Mobile Ads SDK. */
final class AdMobBackend {

    private AdMobBackend() {
    }

    static void init(Context context) {
        MobileAds.initialize(context);
    }

    static void loadBanner(Activity activity, ViewGroup container, AdBannerListener listener) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(AdConfig.ad_admob_banner_unit_id);
        adView.setAdSize(AdSize.BANNER);
        container.removeAllViews();
        container.addView(adView);
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError error) {
                if (listener != null) listener.onAdFailedToLoad(error.getMessage());
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }

    static void loadInterstitial(Activity activity, AdNetwork.InterstitialReadyCallback callback) {
        InterstitialAd.load(activity, AdConfig.ad_admob_interstitial_unit_id, new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        callback.onReady(ad);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        callback.onReady(null);
                    }
                });
    }

    static boolean showInterstitial(Activity activity, Object loadedAd) {
        if (!(loadedAd instanceof InterstitialAd)) return false;
        InterstitialAd ad = (InterstitialAd) loadedAd;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                // no-op; AdNetwork already queues a fresh load after this call
            }
        });
        ad.show(activity);
        return true;
    }
}
