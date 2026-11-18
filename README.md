# wallvibe-ads

First-party Android ad-serving module. Calls straight into each network's own official SDK
(AdMob, Meta Audience Network, Unity Ads, ironSource, AppLovin MAX) with GDPR/UK consent via
Google's User Messaging Platform. No third-party wrapper SDK, no license key, nothing to
generate - it replaces a closed-source vendor AAR (`dreamspace-ads-sdk`) that this project used
to depend on.

## Status

Written against each network's public docs, but **not built or run** in this repo's
environment (no Android SDK / network access available there). Build it in Android Studio and
smoke-test before shipping; per-network SDK APIs do shift between releases, so expect to adjust
a method signature or two for whichever network(s) you actually use.

## Using it in an app

As a module in the same Gradle build (this is how the WallVibe app consumes it):

```groovy
// settings.gradle
include ':wallvibe-ads'

// app/build.gradle
dependencies {
    implementation project(':wallvibe-ads')
}
```

As a standalone repo via JitPack, once pushed to its own GitHub repository:

```groovy
// settings.gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// app/build.gradle
dependencies {
    implementation 'com.github.midnewswire:wallvibe-ads:<tag-or-commit>'
}
```

## Setup

1. Set `AdConfig.ad_network` to the network you want, and fill in that network's unit/app IDs
   on the other `AdConfig.ad_*` fields (publisher/banner/interstitial IDs, app keys, game IDs).
2. Call `AdNetwork.init(context)` once at app startup (after setting `AdConfig` fields).
3. Per screen: `new AdNetwork(activity).loadBannerAd(enabled, containerViewGroup)`,
   `.loadInterstitialAd(enabled)`, `.showInterstitialAd(enabled)`.
4. For consent: `new com.wallvibe.ads.gdpr.GDPR(activity).updateGDPRConsentStatus()`.

## Dependencies

Each network's official SDK is declared as an `api` dependency in `build.gradle`, so it's
available transitively to whatever app consumes this module. Only `AdConfig.ad_network`'s
network needs real IDs to actually serve ads; the rest safely no-op on their placeholder IDs
(they fail to load, they don't crash).

| Network                  | SDK artifact                                     |
|---------------------------|---------------------------------------------------|
| AdMob                     | `com.google.android.gms:play-services-ads`         |
| Consent (all networks)    | `com.google.android.ump:user-messaging-platform`   |
| Meta Audience Network     | `com.facebook.android:audience-network-sdk`        |
| Unity Ads                 | `com.unity3d.ads:unity-ads`                        |
| ironSource                | `com.ironsource.sdk:mediationsdk` (needs ironSource's own maven repo, `https://android-sdk.is.com/`) |
| AppLovin                  | `com.applovin:applovin-sdk`                        |

## License

Your own code - choose whatever license you want for the repo. It doesn't carry over any
license terms from the removed vendor SDK; it was written from scratch against each network's
public SDK and documentation.
