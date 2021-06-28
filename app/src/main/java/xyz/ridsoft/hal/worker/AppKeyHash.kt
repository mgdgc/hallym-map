package xyz.ridsoft.hal.worker

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

class AppKeyHash {

    public fun getAppKeyHash(context: Context) {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for(i in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(i.toByteArray())

                val something = String(Base64.encode(md.digest(), 0)!!)
                Log.e("Debug key", something)
            }
        } catch(e: Exception) {
            Log.e("Not found", e.toString())
        }
    }

}