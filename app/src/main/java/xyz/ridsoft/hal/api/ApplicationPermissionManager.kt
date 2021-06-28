package xyz.ridsoft.hal.api

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import xyz.ridsoft.hal.R

class ApplicationPermissionManager(val context: Context) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 111
    }

    var onRequestResponseListener: ((Int) -> Unit)? = null

    fun checkPermissionGranted(permission: String): Int {
        return ContextCompat.checkSelfPermission(context, permission)
    }

    fun requestPermission(permission: String) {
        if (checkPermissionGranted(permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                // 한번 거부를 눌렀다가 권한을 요청하는 경우
                val alert = AlertDialog.Builder(context)
                alert.setTitle(R.string.permission_location)
                    .setMessage(
                        context.resources.getString(R.string.permission_location_explain)
                            .replace("<app_name>", context.resources.getString(R.string.app_name)))
                    .setNegativeButton(R.string.permission_deny, DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .setPositiveButton(R.string.permission_allow, DialogInterface.OnClickListener { dialogInterface, i ->
                        request(permission)
                    })
                    .show()
            } else {
                request(permission)
            }
        }
    }

    private fun request(permission: String) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }


}
