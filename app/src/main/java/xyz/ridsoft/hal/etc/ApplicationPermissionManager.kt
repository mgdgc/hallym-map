package xyz.ridsoft.hal.etc

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ApplicationPermissionManager(val context: Context) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 111
    }

    var onShouldShowRequestPermissionRationale: (() -> Unit)? = null

    fun checkPermissionGranted(permission: String): Int {
        return ContextCompat.checkSelfPermission(context, permission)
    }

    fun requestPermission(permission: String) {
        if (checkPermissionGranted(permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    permission
                )
            ) {
                onShouldShowRequestPermissionRationale?.let {
                    it()
                }
            } else {
                request(permission)
            }
        }
    }

    private fun request(permission: String) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(permission),
            PERMISSION_REQUEST_CODE
        )
    }


}
