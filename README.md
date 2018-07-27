<p align="center">A GitHub client like how you used to</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.githubclient"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"></a></p>


## Screenshots


<img src="https://s19.postimg.cc/4634bgojn/image.png" width="218px"/> <img src="https://s19.postimg.cc/a70t8jivn/image.png" width="218px"/> <img src="https://s19.postimg.cc/3t7lb4vbn/image.png" width="218px"/> <img src="https://s19.postimg.cc/vgkap88sj/image.png" width="218px"/>

<img src="https://s19.postimg.cc/cbh1fgmer/image.png" width="218px"/> <img src="https://s19.postimg.cc/58dau19xv/image.png" width="218px"/> <img src="https://s19.postimg.cc/q59iypfoj/image.png" width="218px"/> <img src="https://s19.postimg.cc/wiym1x2k3/image.png" width="218px"/>

## Dependencies
```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation "com.android.support:design:${rootProject.ext.supportLibraryVersion}"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

    implementation 'com.android.support.constraint:constraint-layout:1.1.2'

    //Dagger
    kapt "com.google.dagger:dagger-compiler:${rootProject.ext.daggerVersion}"
    implementation "com.google.dagger:dagger:${rootProject.ext.daggerVersion}"

    //Fabric
    implementation('com.crashlytics.sdk.android:crashlytics:2.9.4@aar') {
        transitive = true
    }

    // LiveData
    implementation "android.arch.lifecycle:extensions:1.1.1"

    //Gif View
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.10'

    //Ad
    implementation 'com.google.firebase:firebase-ads:15.0.1'

    //Advanced WebView
    implementation 'com.github.delight-im:Android-AdvancedWebView:v3.0.0'

    //Quick Action
    implementation 'me.piruin:quickaction:2.4.2'

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.6.1'

    //Bottom Navigation
    implementation 'com.github.ittianyu:BottomNavigationViewEx:1.2.4'

    //Snacky
    implementation 'com.github.matecode:Snacky:1.0.3'
}
```
