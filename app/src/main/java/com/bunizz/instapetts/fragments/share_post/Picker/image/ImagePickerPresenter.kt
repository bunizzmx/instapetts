package com.bunizz.instapetts.fragments.share_post.Picker.image

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.bunizz.instapetts.R
import com.bunizz.instapetts.utils.imagePicker.data.Album
import com.bunizz.instapetts.utils.imagePicker.data.Config
import com.bunizz.instapetts.utils.imagePicker.data.Image
import java.io.File
import java.lang.Exception

class ImagePickerPresenter(
        override val view: ImagePickerContract.View?,
        private val context: Context?,
        private val config: Config
) : ImagePickerContract.Presenter<ImagePickerContract.View> {
    private val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    private var albums = LinkedHashMap<String, Album>()

    override fun resume() {
        when (context?.let { ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) }) {
            PERMISSION_GRANTED -> {
               // view?.hidePermissionDenied()
                load()
            }
            else -> view?.showPermissionDenied()
        }
    }

    override fun albums(): List<Album> {
        return albums.values.toList()
    }

    override fun albumSelected(position: Int) {
        view?.clearImages()
        view?.scrollToTop()
        view?.addImages(albums.values.toList()[position].images)
    }

    override fun saveSelected(items: List<Image>) {
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
        view?.addImages(albums.values.toList()[0].images)
    }

    private fun loadAlbums() {
        val cursor = context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED
        )

        context?.getString(R.string.text_imagepicker_album_all_key)?.let { albums.put(it, Album(config.pickerAllItemTitle)) }

        cursor?.takeIf { it.count > 0 }?.use {
            it.moveToLast()
            do {
                try {
                    val id = it.getLong(it.getColumnIndex(projection[0]))
                    val name = it.getString(it.getColumnIndex(projection[1]))
                    val path = it.getString(it.getColumnIndex(projection[2]))
                    val bucket = it.getString(it.getColumnIndex(projection[3]))

                    val file = File(path)
                    if (file.exists()) {
                        if (albums[bucket] == null) {
                            albums[bucket] = Album(bucket)
                        }
                        albums[context?.getString(R.string.text_imagepicker_album_all_key)]?.images?.add(Image(id, name, path))
                        albums[bucket]?.images?.add(Image(id, name, path))
                    }
                }catch (ex : Exception){
                    Log.e("ERROR_CARGAR_ALBUM",":(")
                }
            } while (it.moveToPrevious())
        }
    }
}
