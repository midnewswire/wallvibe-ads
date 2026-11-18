package com.wallvibe.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.wallvibe.ads.listener.AdBannerListener;

/** Unity Ads, via the official Unity Ads SDK. */
final class UnityBackend {

    private UnityBackend() {
    }

    static void init(Context context) {
        UnityAds.initialize(context, AdConfig.ad_unity_game_id, AdConfig.debug_mode,
                new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                    }
                });
    }

    static void loadBanner(Activity activity, ViewGroup container, AdBannerListener listener) {
        BannerView bannerView = new BannerView(activity, AdConfig.ad_unity_banner_unit_id, new UnityBannerSize(320, 50));
        bannerView.setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                if (listener != null) listener.onAdLoaded();
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                if (listener != null) listener.onAdFailedToLoad(errorInfo.errorMessage);
            }

            @Override
            public void onBannerClick(BannerView bannerAdView) {
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerAdView) {
            }
        });
        container.removeAllViews();
        container.addView(bannerView);
        bannerView.load();
    }

    static void loadInterstitial(AdNetwork.InterstitialReadyCallback callback) {
        String placementId = AdConfig.ad_unity_interstitial_unit_id;
        UnityAds.load(placementId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                callback.onReady(placementId);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                callback.onReady(null);
            }
        });
    }

    static boolean showInterstitial(Activity activity, Object loadedPlacementId) {
        if (!(loadedPlacementId instanceof String)) return false;
        String placementId = (String) loadedPlacementId;
        UnityAds.show(activity, placementId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            }
        });
        return true;
    }
}
