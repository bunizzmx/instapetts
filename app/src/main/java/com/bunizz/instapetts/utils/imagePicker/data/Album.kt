package com.bunizz.instapetts.utils.imagePicker.data

import org.parceler.Parcel

@Parcel
data class Album(
        val folderName: String = "",
        val images: ArrayList<Image> = ArrayList()
)
