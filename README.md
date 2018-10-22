<p align="center">AndroCat is a GitHub client for Android phones like how you used to</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.githubclient"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<!---<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.githubclient"><img src="https://www.androidpolice.com/wp-content/uploads/2016/03/nexus2cee_apkm2.gif" width="250px"></a></p>--->


## Screenshots


<img src="https://i.postimg.cc/s37V6NKR/1.png?dl=1" width="218px"/> <img src="https://s19.postimg.cc/3w0npe0v7/image.png" width="218px"/> <img src="https://s19.postimg.cc/h06823g2b/image.png" width="218px"/> <img src="https://s19.postimg.cc/qkpuoyxoj/image.png" width="218px"/>

<img src="https://s19.postimg.cc/vw4r9oc1f/image.png" width="218px"/> <img src="https://s19.postimg.cc/60l0qhxcz/image.png" width="218px"/> <img src="https://i.postimg.cc/QsWDfzfx/7.png?dl=1" width="218px"/> <img src="https://s19.postimg.cc/borbhcrer/image.png" width="218px"/>

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
