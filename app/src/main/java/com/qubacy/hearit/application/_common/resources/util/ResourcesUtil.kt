package com.qubacy.hearit.application._common.resources.util

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri

fun Resources.getUriFromResource(
    resourceId: Int
): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}