<p align="center">AndroCat is a GitHub client for Android phones like how you used to</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.androcat"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<p align="center"><a href='https://ko-fi.com/B0B2TZMH' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a></p>
<p align="center">Important! The old version has been removed from the Google Play Store. Download the updated AndroCat from the store icon above to get the lastest updates.</p>



## Screenshots


<img src="https://i.postimg.cc/Fr1pcM2b/1.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/Hp63rYPs/2.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/TdS9ck6z/3.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/KZGJckfH/4.png?dl=1" width="215px"/>

<img src="https://i.postimg.cc/4XbWTb6c/5.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/rqLfbYrc/6.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/6tZHMNTY/7.png?dl=1" width="215px"/> <img src="https://i.postimg.cc/92NJVf7L/8.png?dl=1" width="215px"/>

## Dependencies
```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation "com.android.support:design:${rootProject.ext.supportLibraryVersion}"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

    implementation 'com.android.support.constraint:constraint-layout:1.1.3'

    // Dagger
    kapt "com.google.dagger:dagger-compiler:${rootProject.ext.daggerVersion}"
    implementation "com.google.dagger:dagger:${rootProject.ext.daggerVersion}"

    // Crashlytics
    implementation 'com.crashlytics.sdk.android:crashlytics:2.9.9'

    // Anko
    implementation 'org.jetbrains.anko:anko-commons:0.10.5'

    // Retrofit
    implementation "com.google.code.gson:gson:${gsonVersion}"
    implementation "com.squareup.retrofit2:retrofit:${rootProject.ext.retrofitVersion}"
    implementation "com.squareup.retrofit2:converter-gson:${rootProject.ext.retrofitVersion}"
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'

    // LiveData
    implementation "android.arch.lifecycle:extensions:1.1.1"

    // Arched Image Progress Bar
    implementation 'com.github.smrtyvz:ArchedImageProgressBar:2.01'

    // Ad
    implementation 'com.google.firebase:firebase-ads:17.1.3'

    // Firebase
    implementation 'com.google.firebase:firebase-core:16.0.7'
    implementation 'com.google.firebase:firebase-config:16.3.0'

    // Advanced WebView
    implementation 'com.github.delight-im:Android-AdvancedWebView:v3.0.0'

    // Quick Action
    implementation 'me.piruin:quickaction:2.4.2'

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.6.1'

    // Bottom Navigation
    implementation 'com.github.ittianyu:BottomNavigationViewEx:1.2.4'

    // Snacky
    implementation 'com.github.matecode:Snacky:1.0.3'

    // Multidex
    implementation 'com.android.support:multidex:1.0.3'
}
```

### License
Copyright 2018 Mustafa Ozhan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
