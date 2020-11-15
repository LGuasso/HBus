# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in android-sdk/tools/proguard/proguard-android.txt
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
-dontoptimize

-repackageclasses ''

#disable option methods onClik View
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep class com.google.inject.** { *; }


-keep class br.com.expressobits.hbus.application.TPCDataBaseInit


-keep class br.com.expressobits.hbus.ui.*

-keep class com.mikepenz.materialdrawer.*

-keep public class com.google.android.gms.* { public *; }


-keep class com.google.android.gms.ads.*

-keep class com.google.ads.mediation.AdUrlAdapter