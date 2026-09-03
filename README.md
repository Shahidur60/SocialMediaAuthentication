# SocFakeID — Social-Media Authentication Ceremony (User-Study App)

Android research artifact for the paper **“Stress Testing Social Media Authentication
Ceremonies of End-to-End Encrypted Messaging with Ephemeral Fake Accounts”**.

This module is the **phone application used in the controlled user study** (Section 5 of the
paper). It re-implements the social-media–based authentication ceremony of
Vaziripour et al. (CHI ’19) inside a Signal-styled UI, and drives each participant through
a fixed sequence of ceremony trials in which attacker-controlled ("SocFakeID") profiles are
substituted for the peer's genuine ones. Every accept/reject decision is logged to disk so
that Benign Success Rate (BSR), Attack Success Rate (ASR), and the mismatched-profile
baseline can be computed offline.

---

## 1. Screen-by-screen flow

Launcher → `FinalActivity`, all in package `com.example.socialmediaattack`:

```
MainActivity                (registration: first + last name)
      │  saves name to SharedPreferences "myUserPrefs"
      │  writes  /sdcard/usersData/<First>_<Last>_Auth4Bob.txt
      ▼
FriendOneActivity           (familiarization: Bob's REAL X + Instagram pics, tap to open)
      ▼
exampleForBob               (trial/practice: 6 trials, layout bob_authentication_example)
      │  3 real + 3 "clear fake" pairs, Accept/Reject only increment a counter
      ▼
mainInstructions            ("the main study … 40 trials")
      ▼
bobAuthenticationCeremony   (MAIN STUDY: 40 trials, layout bob_authentication_ceremony)
      │  each Accept/Reject is appended to the cumulative log and
      │  rewritten to <First>_<Last>_Auth4Bob.txt
      ▼
FinalActivity               ("Thank you …", Exit kills the process)
```

Per trial the ceremony screen shows four circular images laid out like the Vaziripour et al.
figure:

* **top-left `imageTwitterView`** – the peer's X/Twitter profile picture (tap → opens the X URL)
* **bottom-right `imageInstagramView`** – the peer's Instagram picture (tap → opens the IG URL)
* **top-right `imageFacebookView`** / **bottom-left `imagePinteresView`** – static
  `facebook_img` / `pinteres_img` placeholders ("this contact has not authorized Signal to
  access …"), decorative and non-interactive
* a Signal-flavored description line, and **Reject** / **Next** / **Accept** buttons

`btnNext` is disabled until a decision is made; after the final trial it launches
`FinalActivity`. The `switch1` "Verified" toggle from the original design is present in the
layouts but `visibility="gone"` — the study deliberately uses two buttons instead
("focus was feasibility rather than usability").

---

## 2. Session types and how the code balances them

Each entry in the `BobLinks[]` array pairs a drawable with a social URL. On every trial the
array is shuffled (`Collections.shuffle`), the first element is drawn, its partner image +
URL are chosen by matching on the drawable id, and a ground-truth label is stored in
`currentImage`. Per-category counters cap how many of each type appear, and the shuffle is
re-rolled while a cap is hit or the same image repeats twice in a row.

### `bobAuthenticationCeremony` — 40 trials

| Session type (paper) | `currentImage` label | Drawables | Example handles | Count |
| --- | --- | --- | --- | --- |
| **Benign** (reference) | `Bob's real X and instagram accounts.` | `bob_real_img_x`, `bob_real_img_instagram` | `@Bob253784738`, `instagram.com/bob253784738` | `counterRealProfile == 10` |
| **SocFakeID** (hidden attack) | `Bob's fake X and instagram accounts. Hidden Attack Case` | `bob_fake_img_x(_2)`, `bob_fake_img_instagram(_2)` | `@Bob253874738`, `@Bob253748738` (digits transposed; picture ≈ identical) | `counterFakeProfile == 10` |
| **Baseline Attack** (clearly mismatched) | `Bob's clear fake X and instagram accounts.` / `Alice's clear fake …` | `clear_bob_fake_img_*`, `clear_bob_fake_img_*_2`, `clear_alice_fake_img_*`, `alice_fake_img_*` | `@Bob`, `tombobnyc`, `bobbyjonc`, `boboiboy`, `Bee_Bob`, `bobbyleelive`, `doekis_`, `alicelk` | `counterClearFakeProfile == 20` |

10 + 10 + 20 = 40, matching the paper: “twenty displayed Bob's reference picture and name
(half of them SocFakeID), twenty displayed unrelated identities.”

### `exampleForBob` — 6 practice trials

`counterRealProfile == 3` real reference pairs + `counterClearFakeProfile == 3` "clear fake"
pairs. Accept/Reject are not recorded — this screen only builds task familiarity.

### `aliceAuthenticationCeremony` — 30 trials (currently unreachable, see §7)

10 real (`alice_real_img_*`, `@Alice100010000`) + 10 SocFakeID (`alice_fake_img_*`,
`@Alice1000100000`, one extra digit) + 10 clear-fake, logged to `<First>_<Last>_Auth4Alice.txt`.

---

## 3. Data collection / output format

* Participant name is captured in `MainActivity`, stored in `SharedPreferences("myUserPrefs")`
  as `firstName` / `lastName`, and reused by the ceremony screens.
* Output file: `‹external-storage›/usersData/<First>_<Last>_Auth4Bob.txt`
  (`Environment.getExternalStorageDirectory()` — legacy external storage; see §8).
* On **every** Accept/Reject the ceremony appends one line to an in-memory string and
  rewrites the whole file (truncating, not appending on disk), so the final file holds the
  full ordered log:

  ```
  File starts for authenticating Bob:

  1- John Doe accepted Bob's real X and instagram accounts.

  2- John Doe rejected Bob's clear fake X and instagram accounts.

  3- John Doe accepted Bob's fake X and instagram accounts. Hidden Attack Case
  ...
  ```

* Analysis: parse `accepted` / `rejected` against the label.
  * **BSR** = accepted "real" ÷ 10
  * **ASR** = accepted "Hidden Attack Case" ÷ 10
  * **Baseline** = accepted "clear fake" ÷ 20

Demographic and post-test questionnaires (paper Appendices A–B) are collected **outside**
the app.

---

## 4. Project layout

```
.
├── build.gradle                 # AGP 7.3.0; Android app + library plugins (root)
├── settings.gradle              # rootProject "SocialMediaAttack", :app
├── gradle.properties            # AndroidX on, non-transitive R
├── gradle/wrapper/…             # Gradle 7.4
├── local.properties             # sdk.dir — machine-specific, edit for your machine
└── app/
    ├── build.gradle             # namespace com.example.socialmediaattack
    │                            # compileSdk/targetSdk 32, minSdk 21, Java 8
    │                            # deps: appcompat 1.5.1, material 1.7.0,
    │                            #       constraintlayout 2.1.4, junit, espresso
    ├── proguard-rules.pro       # defaults only (minify disabled)
    └── src/main/
        ├── AndroidManifest.xml  # ⚠ currently EMPTY — must be restored (see §8)
        ├── java/com/example/socialmediaattack/
        │   ├── MainActivity.java              # registration + file bootstrap
        │   ├── FriendOneActivity.java         # Bob familiarization
        │   ├── exampleForBob.java             # 6 practice trials
        │   ├── mainInstructions.java          # bridge screen
        │   ├── bobAuthenticationCeremony.java # 40-trial main study + logging
        │   ├── BobLinks.java                  # (image id, url) holder for Bob
        │   ├── FriendTwoActivity.java         # Alice familiarization  (unreachable)
        │   ├── aliceAuthenticationCeremony.java # 30-trial Alice study (unreachable)
        │   ├── AliceLinks.java                # (image id, url) holder for Alice
        │   └── FinalActivity.java             # thank-you + hard exit
        └── res/
            ├── layout/           # activity_main, activity_friend_one/two,
            │                     # main_instructions, bob_authentication_ceremony,
            │                     # bob_authentication_example,
            │                     # alice_authentication_ceremony, final_activity
            ├── drawable/         # profile pictures, grouped by role:
            │     bob_real_img_{x,instagram}          alice_real_img_{x,instagram}
            │     bob_fake_img_{x,instagram}[_2,_3]   alice_fake_img_{x,instagram}
            │     clear_bob_fake_img_{x,instagram}[_2]
            │     clear_alice_fake_img_{x,instagram}
            │     facebook_img, pinteres_img          # decorative placeholders
            ├── values/           # strings (app_name = "Social_Authentication_Ceremony"),
            │                     # colors (Signal ultramarine), themes
            ├── values-night/     # dark theme
            ├── mipmap-*/         # launcher icons
            └── xml/              # backup_rules, data_extraction_rules
```

`app/src/test` and `app/src/androidTest` contain only the Android Studio starter examples.

---

## 5. Build & run

### Prerequisites
* Android Studio (Giraffe-era) or a standalone Android SDK with **platform 32** + build-tools
* JDK 8+ (project compiles at Java 8)
* A device or emulator on **API 21–32**

### Fix `local.properties`
It is checked in pointing at a former author's machine:

```properties
sdk.dir=C\:\\Users\\Mashari\\AppData\\Local\\Android\\Sdk
```

Replace with your SDK path (or delete the file and let Android Studio regenerate it, or set
`ANDROID_HOME`).

### ⚠ Restore `app/src/main/AndroidManifest.xml`
The source manifest is currently a **0-byte file**, so the project will not build until it is
restored. A previously merged manifest is preserved at
`app/build/intermediates/merged_manifests/debug/AndroidManifest.xml`; the source manifest
should declare:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.socialmediaattack">

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:requestLegacyExternalStorage="true"
        android:theme="@style/Theme.SocialMediaAttack">

        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
        <activity android:name=".FriendOneActivity"/>
        <activity android:name=".FriendTwoActivity"/>
        <activity android:name=".exampleForBob"/>
        <activity android:name=".mainInstructions"/>
        <activity android:name=".bobAuthenticationCeremony"/>
        <activity android:name=".aliceAuthenticationCeremony"/>
        <activity android:name=".FinalActivity"/>
    </application>
</manifest>
```

### Build
```bash
./gradlew assembleDebug          # APK → app/build/outputs/apk/debug/
./gradlew installDebug           # build + install on a connected device
```
(On Windows use `gradlew.bat`.)

### Grant storage access
The decision log is written to shared external storage. On API 30+ the app needs
**All files access** (Settings → Apps → Social_Authentication_Ceremony → Permissions), which
is why the manifest requests `MANAGE_EXTERNAL_STORAGE` + `requestLegacyExternalStorage`.
Pull results with:
```bash
adb pull /sdcard/usersData
```

---

## 6. Configuration knobs

| Want to change | Where |
| --- | --- |
| Trial count (main study) | `bobAuthenticationCeremony`: `if (counter != 40)` and the `== 10 / == 20` caps |
| Practice trial count | `exampleForBob`: `if (counter != 6)` and the `== 3` caps |
| Which accounts / pictures are shown | `BobLinks b01…b14` array + the `if (resource == …)` partner blocks |
| Real vs look-alike handles | the `https://twitter.com/…` / `instagram.com/…` strings in those blocks |
| Ground-truth labels in the log | the `currentImage = "…"` assignments |
| Output filename / location | `path` field + `myFile` string in each ceremony activity |
| Peer name in UI | `textViewBob` / `description` text in `res/layout/bob_authentication_ceremony.xml` |
| Enable the Alice (symmetric) path | route some activity's button to `FriendTwoActivity` (nothing does today) |

---

## 7. Ethics & responsible use

The user study received **IRB approval**; participation was voluntary with opt-out, and no
identifiable data was stored. All "peer" and "attacker" accounts were **researcher-created** —
no participant's personal or pre-existing accounts were used. This artifact is published to
support reproduction of a **defensive** security result: that service-controlled identity
presentation makes social authentication ceremonies unsafe against the very adversary E2EE
authentication exists to counter. Do not repurpose it to build or link real deceptive
accounts. The paper's recommended fixes are architectural: client-verifiable key
transparency, user-controlled/decentralized identifiers, or a guaranteed out-of-band
reference.

---
