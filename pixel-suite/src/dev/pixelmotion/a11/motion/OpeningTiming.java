package dev.pixelmotion.a11.motion;

import android.view.animation.Interpolator;

/** Per-property clocks of the Pixel Launcher 701 icon opening animation. */
enum OpeningTiming {
    LOWER_OR_NEAR_CENTER(306, 550, 61),
    UPPER(440, 244, 49);

    // Keep the existing per-property motion curves, stretched proportionally
    // from 450 ms to the requested 550 ms total launch duration.
    static final int TOTAL_MS = 550;
    static final Interpolator SCALE = MotionCurve.a10Exaggerated(550, TOTAL_MS);
    static final Interpolator CROP_AND_RADIUS = MotionCurve.a10Exaggerated(458, TOTAL_MS);
    final Interpolator x, y, fade;

    OpeningTiming(int xMs, int yMs, int fadeMs) {
        x = MotionCurve.a11Aggressive(xMs, TOTAL_MS);
        y = MotionCurve.a11Aggressive(yMs, TOTAL_MS);
        fade = fraction -> Math.max(0f, Math.min(1f, (fraction * TOTAL_MS - 31f) / fadeMs));
    }

    static OpeningTiming select(float iconTop, float iconCenterY, float targetCenterY, int cellHeight) {
        return iconTop > targetCenterY || Math.abs(targetCenterY - iconCenterY) < cellHeight
                ? LOWER_OR_NEAR_CENTER : UPPER;
    }
}
