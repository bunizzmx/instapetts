package com.bunizz.instapetts.utils.imagePicker

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import com.bunizz.instapetts.utils.imagePicker.data.Config
import com.bunizz.instapetts.utils.imagePicker.helper.ExtraName
import com.bunizz.instapetts.utils.imagePicker.helper.RequestCode
import com.bunizz.instapetts.utils.imagePicker.ui.picker.ImagePickerActivity
import java.io.Serializable

class ImagePicker {
    class Builder(private var activity: Activity) {
        private val config = Config()

        fun pickerAllItemTitle(title: String): Builder {
            config.pickerAllItemTitle = title
            return this
        }

        fun maxCount(count: Int): Builder {
            config.maxCount = count
            return this
        }

        fun noImage(image: Drawable?): Builder {
            config.noImage = image
            return this
        }

        fun noPermissionImage(image: Drawable?): Builder {
            config.noPermission = image
            return this
        }

        fun noPermissionText(text: String): Builder {
            config.noPermissionText = text
            return this
        }

        fun packageName(name: String): Builder {
            config.packageName = name
            return this
        }

        fun start() {
            Intent(activity, ImagePickerActivity::class.java)
                    .apply { putExtra(ExtraName.CONFIG.name, config as Serializable) }
                    .let { activity.startActivityForResult(it, RequestCode.PICK_IMAGE.rawValue) }
        }
    }
}
