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

#depuração
-verbose
#desabilita o opção de otimização
-dontoptimize

-repackageclasses ''

#desabilita o opção para metodos onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep class com.google.inject.** { *; }

-keep public class br.com.expressobits.hbus.ui.MainActivity
-keep public class br.com.expressobits.hbus.ui.fragments.LinhasFragment
-keep public class br.com.expressobits.hbus.ui.fragments.OnibusFragment

-keep class com.mikepenz.materialdrawer.** { *; }

-dontwarn android.support.**

-keep public class com.google.android.gms.* { public *; }

-dontwarn com.google.android.gms.**

-keep class org.slf4j.**