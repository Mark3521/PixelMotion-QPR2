# PixelMotionSuite QPR2 Splash hotfix notes

This source patch addresses two failures visible in the supplied LSPosed log dated 2026-11-21:

1. `HomeSplash` accepts both legacy `IBinder` launch cookies and Android 17 QPR2 `ActivityOptions.LaunchCookie` wrappers, extracting the wrapper's `binder` field. The supplied log still showed the old `ClassCastException` through 16:48:52, so the previous cookie patch was not present in the active module process during those events.
2. `ShellSplash` no longer loads the compiler-generated `SplashscreenWindowCreator$$ExternalSyntheticLambda4` by a hard-coded name. It discovers `makeSplashScreenContentView` from the actual `SplashscreenContentDrawer` method table by matching the leading parameter types (`Context`, `StartingWindowInfo`, `int`) and hooks the resolved method using its actual callback parameter type. Ambiguous or missing matches produce a diagnostic listing candidate signatures.

Additional `PixelSuiteSplash` diagnostics mark method resolution, hook installation, broadcast dispatch, and SystemUI acknowledgement.

## Validation status

- ZIP integrity and source-level patch assertions passed.
- This environment does not contain the project's Android build tool directory (`jre`, `ecj.jar`, `r8.jar`, `aapt2.exe`, `platform-35/android.jar`, `framework.jar`), so this is a source patch, not a compiled or device-tested APK.
- Build with the project's documented command: `python assemble.py <android-tools> unsigned.apk`. Sign with the same certificate as the installed module. Reboot after installing so old Launcher and SystemUI processes cannot retain the previous code.


## 256px foreground rendering test

- Increased the adaptive-icon foreground raster size from 144×144 to 256×256.
- The launcher payload and SystemUI bitmap reconstruction both use the shared `SplashPolicy.SIZE` value, so the dimensions remain consistent across the broadcast.
- Kept the existing splash animation and target icon sizing unchanged; this isolates the effect of source raster resolution.
- Each foreground pixel array is approximately 256 KiB before Bundle overhead. This is smaller than the 384×384 test payload that caused `TransactionTooLargeException` in the supplied LSPosed log, but still requires device-side stability testing.
