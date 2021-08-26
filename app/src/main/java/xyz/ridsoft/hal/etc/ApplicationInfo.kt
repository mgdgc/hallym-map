package xyz.ridsoft.hal.etc

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import androidx.annotation.RequiresApi

class ApplicationInfo(val context: Context) {

    var packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    fun getVersionName(): String {
        return packageInfo.versionName
    }

    fun getVersionCode(): Int {
        return packageInfo.versionCode
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getLongVersionCode(): Long {
        return packageInfo.longVersionCode
    }

}