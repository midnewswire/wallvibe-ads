package com.wallvibe.ads.gdpr;

import android.app.Activity;
import android.util.Log;

import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

/**
 * GDPR/UK consent via Google's User Messaging Platform (IAB TCF v2 compliant), replacing the
 * old vendor SDK's bespoke GDPR dialog. This is what every ad SDK below (AdMob, Meta, Unity,
 * ironSource, AppLovin) reads its consent signal from via the standard SharedPreferences IAB
 * TCF keys, so a single consent flow here covers all five networks.
 */
public class GDPR {

    private static final String TAG = "WallVibeAds.GDPR";

    private final Activity activity;

    public GDPR(Activity activity) {
        this.activity = activity;
    }

    public void updateGDPRConsentStatus() {
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(activity);
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();

        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity, formError -> {
                    if (formError != null) {
                        Log.w(TAG, "Consent form error: " + formError.getMessage());
                    }
                    // Consent flow finished (granted, not required, or dismissed); ad SDKs
                    // pick the result up themselves from the shared IAB TCF prefs.
                }),
                formError -> Log.w(TAG, "Consent info update failed: " + formError.getMessage())
        );
    }
}
