package com.bunizz.instapetts.fragments.share_post.Picker.video


import com.bunizz.instapetts.utils.imagePicker.data.AlbumVideo
import com.bunizz.instapetts.utils.imagePicker.data.Video
import io.github.karageageta.imagepicker.ui.BaseContract

interface VideoPickerContract {
    interface View : BaseContract.View {
        fun scrollToTop()
        fun clearAlbums()
        fun addAlbums(items: List<AlbumVideo>)
        fun clearImages()
        fun addImages(items: List<Video>)
        fun showPermissionDenied()
        fun hidePermissionDenied()
        fun requestPermissions()
        fun finishPickImages(items: List<Video>)
        fun finish()
    }

    interface Presenter<out T : BaseContract.View> : BaseContract.Presenter<T> {
        fun resume()
        fun albums(): List<AlbumVideo>
        fun albumSelected(position: Int)
        fun saveSelected(items: List<Video>)
    }
}
