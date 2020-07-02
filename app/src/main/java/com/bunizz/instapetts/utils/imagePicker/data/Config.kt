package com.bunizz.instapetts.utils.imagePicker.data

import android.graphics.drawable.Drawable
import org.parceler.Parcel
import java.io.Serializable

@Parcel
data class Config(
        var pickerAllItemTitle: String = "All",
        var maxCount: Int = 5,
        var noImage: Drawable? = null,
        var noPermission: Drawable? = null,
        var noPermissionText: String? = null,
        var packageName: String? = null
) : Serializable
