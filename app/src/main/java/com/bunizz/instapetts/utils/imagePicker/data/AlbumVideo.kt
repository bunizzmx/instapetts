package com.bunizz.instapetts.utils.imagePicker.data

import org.parceler.Parcel

@Parcel
data class AlbumVideo(
        val folderName: String = "",
        val videos: ArrayList<Video> = ArrayList()
)
