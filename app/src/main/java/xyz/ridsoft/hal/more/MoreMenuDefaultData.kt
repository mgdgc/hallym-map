package xyz.ridsoft.hal.more

import android.content.Context
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.model.TableData
import xyz.ridsoft.hal.value.SharedPreferencesKeys

class MoreMenuDefaultData(val context: Context) {

    public fun getDefaultData(): Array<TableData> {
        val data = ArrayList<TableData>()

        // User
        val user = TableData("user", "", null, R.drawable.baseline_account_circle_24)
        data.add(user)

        // Application
        val sectionApp =
            TableData("section_app", context.getString(R.string.more_section_application))
        sectionApp.itemType = TableData.Companion.ItemType.DIVIDER
        data.add(sectionApp)

        val appInfo =
            TableData(
                "app_info",
                context.getString(R.string.app_name),
                content = "v0.1",
                icon = R.drawable.ic_hm_foreground,
                iconBackgroundColor = R.drawable.icon_app_icon_bg
            )
        appInfo.selectable = true
        appInfo.accessory = true
        appInfo.itemPosition = TableData.Companion.ItemPosition.TOP
        data.add(appInfo)

        // If developer menu is enabled
        if (context.getSharedPreferences(SharedPreferencesKeys.DEVELOPER_PREF, 0)
                .getBoolean(SharedPreferencesKeys.BOOL_DEV_MENU_ENABLE, false)
        ) {
            val devMenu = TableData(
                "dev_menu", context.getString(R.string.more_dev_menu), content = null,
                icon = R.drawable.baseline_bug_report_24,
                iconBackgroundColor = R.drawable.icon_green_bg
            )
            devMenu.selectable = true
            devMenu.accessory = true
            devMenu.itemPosition = TableData.Companion.ItemPosition.MIDDLE
            data.add(devMenu)
        }

        val changeLog = TableData(
            "change_log",
            context.getString(R.string.more_change_log),
            content = null,
            icon = R.drawable.baseline_new_releases_24,
            iconBackgroundColor = R.drawable.icon_blue_bg
        )
        changeLog.selectable = true
        changeLog.itemPosition = TableData.Companion.ItemPosition.MIDDLE
        data.add(changeLog)

        val licenses = TableData(
            "licenses",
            context.getString(R.string.more_licenses),
            content = null,
            icon = R.drawable.baseline_format_list_bulleted_24,
            iconBackgroundColor = R.drawable.icon_blue_bg
        )
        licenses.accessory = true
        licenses.selectable = true
        licenses.itemPosition = TableData.Companion.ItemPosition.BOTTOM
        data.add(licenses)

        // Data Management
        val sectionData =
            TableData("section_data", context.getString(R.string.more_section_data))
        sectionData.itemType = TableData.Companion.ItemType.DIVIDER
        data.add(sectionData)

        val privacyPolicy = TableData(
            "privacy",
            context.getString(R.string.more_privacy_policy),
            content = null,
            icon = R.drawable.baseline_policy_24,
            iconBackgroundColor = R.drawable.icon_yellow_bg
        )
        privacyPolicy.itemPosition = TableData.Companion.ItemPosition.TOP
        privacyPolicy.accessory = true
        privacyPolicy.selectable = true
        data.add(privacyPolicy)

        val deleteFav = TableData(
            "delete_fav",
            context.getString(R.string.more_delete_favorite),
            content = null,
            icon = R.drawable.baseline_delete_outline_24,
            iconBackgroundColor = R.drawable.icon_orange_bg
        )
        deleteFav.selectable = true
        deleteFav.itemPosition = TableData.Companion.ItemPosition.MIDDLE
        data.add(deleteFav)

        val deleteAll = TableData(
            "delete_all",
            context.getString(R.string.more_delete_all),
            content = null,
            icon = R.drawable.baseline_delete_24,
            iconBackgroundColor = R.drawable.icon_red_bg
        )
        deleteAll.selectable = true
        deleteAll.itemPosition = TableData.Companion.ItemPosition.BOTTOM
        data.add(deleteAll)

        // Report
        val sectionReport =
            TableData("section_report", context.getString(R.string.more_section_report))
        sectionReport.itemType = TableData.Companion.ItemType.DIVIDER
        data.add(sectionReport)

        val reportIssue = TableData(
            "report_issue",
            context.getString(R.string.more_report_issue),
            content = null,
            icon = R.drawable.baseline_bug_report_24,
            iconBackgroundColor = R.drawable.icon_gray_bg
        )
        reportIssue.selectable = true
        reportIssue.itemPosition = TableData.Companion.ItemPosition.TOP
        data.add(reportIssue)

        val reportPlace = TableData(
            "report_place",
            context.getString(R.string.more_report_place),
            content = null,
            icon = R.drawable.baseline_outlined_flag_24,
            iconBackgroundColor = R.drawable.icon_gray_bg
        )
        reportPlace.selectable = true
        reportPlace.itemPosition = TableData.Companion.ItemPosition.BOTTOM
        data.add(reportPlace)

        return data.toTypedArray()
    }
}