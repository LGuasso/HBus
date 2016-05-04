# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files (x86)\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#depura��o
-verbose
#desabilita o op��o de otimiza��o
-dontoptimize

-repackageclasses ''

#desabilita o op��o para metodos onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep class com.google.inject.** { *; }

-keep public class br.com.expressobits.hbus.ui.MainActivity

-keep public class br.com.expressobits.hbus.ui.fragments.AddFavoriteFragment
-keep public class br.com.expressobits.hbus.ui.fragments.OnibusFragment
-keep public class br.com.expressobits.hbus.ui.fragments.FavoriteItineraryFragment
-keep public class br.com.expressobits.hbus.ui.splash.SplashActivty
-keep public class br.com.expressobits.hbus.ui.tour.TourActivity
-keep public class br.com.expressobits.hbus.ui.tour.PagerFragment
-keep public class br.com.expressobits.hbus.ui.tour.ContentPagerAdapter
-keep public class br.com.expressobits.hbus.ui.settings.SettingsActivity2
-keep public class br.com.expressobits.hbus.ui.settings.AboutPreferenceFragment
-keep public class br.com.expressobits.hbus.ui.settings.SelectCityFragment
-keep public class br.com.expressobits.hbus.ui.settings.DataSyncPreferenceFragment
-keep public class br.com.expressobits.hbus.ui.settings.GeneralPreferenceFragment
-keep public class br.com.expressobits.hbus.ui.settings.NotificationPreferenceFragment


-keep class com.mikepenz.materialdrawer.** { *; }

-dontwarn android.support.**

-keep public class com.google.android.gms.* { public *; }

-dontwarn com.google.android.gms.**

-keep class org.slf4j.**