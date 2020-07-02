package com.bunizz.instapetts.utils.imagePicker.data

import org.parceler.Parcel

@Parcel
data class Image(
        val id: Long = 0,
        val name: String = "",
        val path: String = ""
)
