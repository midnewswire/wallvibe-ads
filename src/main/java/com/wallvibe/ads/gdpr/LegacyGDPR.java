package com.wallvibe.ads.gdpr;

import android.app.Activity;

/**
 * Kept only for API compatibility with consumers that construct this alongside {@link GDPR}
 * but never call a method on it. {@link GDPR}'s UMP flow is the one real consent path; there
 * is no separate "legacy" path any more.
 */
@Deprecated
public class LegacyGDPR {
    public LegacyGDPR(Activity activity) {
    }
}
