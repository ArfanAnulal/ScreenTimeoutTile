# Screen Timeout Tile

A lightweight Android Quick Settings Tile that allows you to toggle your screen timeout between **30 seconds** and **Always On** (Infinite) with a single tap from your notification shade.

Designed specifically to handle newer Android versions (like Funtouch OS 14) where standard "Never" timeout options are missing or hidden. 

Born from a problem I've had while running Flutter apps on my phone, which always required deep diving into the settings for a simple change

## 🚀 Features



* **Quick Toggle:** Switch modes directly from the notification shade (Quick Settings).
* **Visual Feedback:** * 🕒 **Clock Icon:** Normal mode (30 seconds).
    * ♾️ **Infinite Icon:** Always On mode (Tile lights up).
* **Battery Safe:** Uses system settings directly; no background services draining battery.


## ⚠️ Important: First Time Setup

After installing the app, you **must** manually grant permission for it to modify system settings. The app cannot do this automatically due to Android security restrictions.



1. **Add the Tile:**
    * Swipe down to open your Notification Shade.
    * Tap the **Edit (Pencil)** icon.
    * Drag the **"Screen Timeout"** tile into your active tiles area.
2. **Grant Permission (Critical Step):**
    * **Long-press** the new "Screen Timeout" tile.
    * This will open the App Info page.
    * Scroll down and tap on **"Modify system settings"** (sometimes called "Allow modify system settings").
    * Toggle the switch to **ON**.

**Note:** The tile will not work until this permission is granted. If you tap it without permission, it may attempt to redirect you to the settings page, but the long-press method is the most reliable.


## 🛠️ How to Build

This project is built with **Kotlin** and standard Android Views (no Jetpack Compose).


### Prerequisites



* Android Studio
* JDK 17 (recommended)
* Android Device with Developer Options enabled


### Compile & Install

If you have your phone connected via USB:
```
./gradlew installDebug
```

To just build the APK:
```
./gradlew assembleDebug
```

The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`


## 🧩 How it Works



* **Normal Mode:** Sets system screen off timeout to `30000` ms (30 seconds).
* **Infinite Mode:** Sets timeout to `Int.MAX_VALUE` (~24 days).
    * *Why?* Many modern Android skins (like Funtouch OS) ignore the traditional `-1` value for "Never". Using `Int.MAX_VALUE` forces the system to stay awake effectively forever.


## 📝 Note

If you need more timer options, features or face any bugs do raise an issue


## 📄 License

This project is open-source and available under the MIT License.
