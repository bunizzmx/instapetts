package com.bunizz.instapetts.fragments.share_post.Picker.video

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.bunizz.instapetts.R
import com.bunizz.instapetts.fragments.share_post.Picker.image.ImagePickerContract
import com.bunizz.instapetts.utils.imagePicker.data.*
import java.io.File

class VideoPickerPresenter(
        override val view: VideoPickerContract.View?,
        private val context: Context?,
        private val config: Config
) : VideoPickerContract.Presenter<VideoPickerContract.View> {
    private val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
    )

    private var albums = LinkedHashMap<String, AlbumVideo>()

    override fun resume() {
        when (context?.let { ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) }) {
            PERMISSION_GRANTED -> {
               // view?.hidePermissionDenied()
                load()
            }
            else -> view?.showPermissionDenied()
        }
    }

    override fun albums(): List<AlbumVideo> {
        return albums.values.toList()
    }

    override fun albumSelected(position: Int) {
        view?.clearImages()
        view?.scrollToTop()
        view?.addImages(albums.values.toList()[position].videos)
    }

    override fun saveSelected(items: List<Video>) {
        if (items.isNotEmpty()) {
            view?.finishPickImages(items)
        }
        view?.finish()
    }

    // private

    private fun load() {
        if (albums.isNotEmpty()) {
            return
        }
        loadAlbums()
        view?.clearAlbums()
        view?.addAlbums(albums.values.toList())
        view?.clearImages()
        view?.addImages(albums.values.toList()[0].videos)
    }

    private fun loadAlbums() {
        val cursor = context?.contentResolver?.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED
        )

        context?.getString(R.string.text_imagepicker_album_all_key)?.let { albums.put(it, AlbumVideo(config.pickerAllItemTitle)) }

        cursor?.takeIf { it.count > 0 }?.use {
            it.moveToLast()
            do {
                val id = it.getLong(it.getColumnIndex(projection[0]))
                val name = it.getString(it.getColumnIndex(projection[1]))
                val path = it.getString(it.getColumnIndex(projection[2]))
                val bucket = it.getString(it.getColumnIndex(projection[3]))
                val duration = it.getString(it.getColumnIndex(projection[4]))

                val file = File(path)
                if (file.exists()) {
                    if (bucket != null)
                    {
                        if (albums[bucket] == null) {
                            albums[bucket] = AlbumVideo(bucket)
                        }
                    albums[context?.getString(R.string.text_imagepicker_album_all_key)]?.videos?.add(Video(id, name, path, duration))
                    albums[bucket]?.videos?.add(Video(id, name, path, duration))
                }
                }
            } while (it.moveToPrevious())
        }
    }

}
