# Pixel Motion Suite — Android 17 QPR2 patch

Base: upstream PixelMotionSuite 2.3.15 source archive supplied in this conversation.
Target: Pixel 9 Pro / Android 17 QPR2 / SDK 37 / Pixel Launcher 907.

## Changes

1. `Motion.java`
   - Updates the `ScalingWorkspaceRevealAnim` constructor lookup from the older
     five-parameter signature to the QPR2 seven-parameter signature:
     `QuickstepLauncher, RectFSpringAnim, RectF, boolean, boolean, boolean, boolean`.
   - The existing after-constructor Hook callback only reads arguments 0 and 1,
     so the added boolean arguments do not require callback logic changes.

2. `RecentsDockHost.java`
   - Makes `OseWidgetView(Context)` lookup optional.
   - On QPR2, where this constructor is absent, logs the missing native QSB and
     continues installing the Recents Dock hooks.
   - The native search bar itself is omitted in this fallback. This patch does
     NOT yet implement the replacement Factory path.

## Important limitations

- This is a source patch, not a compiled or signed APK.
- It has not been run on-device. After building/installing, collect a fresh
  LSPosed log to find any subsequent QPR2 incompatibilities that were previously
  hidden by these first initialization failures.
- The upstream project uses `assemble.py`, not Android Studio's standard Gradle
  Build workflow. Follow the upstream README for the toolchain.
- An APK intended to update an existing installation must be signed with the
  same certificate as that installation. The repository does not contain the
  release signing key.
