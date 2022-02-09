package com.wing.tree.n.back.training.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import com.wing.tree.n.back.training.presentation.constant.BLANK

internal val Context.versionName get() = try {
    packageManager.getPackageInfo(packageName, 0).versionName
} catch (e: PackageManager.NameNotFoundException) {
    BLANK
}