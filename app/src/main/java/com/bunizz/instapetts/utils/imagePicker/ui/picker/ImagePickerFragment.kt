package com.bunizz.instapetts.utils.imagePicker.ui.picker

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bunizz.instapetts.R
import com.bunizz.instapetts.fragments.share_post.Picker.image.ImageListRecyclerViewAdapter
import com.bunizz.instapetts.fragments.share_post.Picker.image.ImagePickerContract
import com.bunizz.instapetts.fragments.share_post.Picker.image.ImagePickerPresenter
import com.bunizz.instapetts.utils.crop.OnCropListener
import com.bunizz.instapetts.utils.imagePicker.data.Album
import com.bunizz.instapetts.utils.imagePicker.data.Config
import com.bunizz.instapetts.utils.imagePicker.data.Image
import com.bunizz.instapetts.utils.imagePicker.helper.ExtraName
import com.bunizz.instapetts.utils.imagePicker.helper.RequestCode
import kotlinx.android.synthetic.main.fragment_imagepicker_picker.*
import java.io.Serializable

class ImagePickerFragment : Fragment(),
        ImagePickerContract.View,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        ImageListRecyclerViewAdapter.OnItemClickListener,
        ImageListRecyclerViewAdapter.OnItemLongClickListener {
    override fun hidePermissionDenied() {
        Log.e("ERROR",":)");
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPermissionDenied() {
        Log.e("ERROR",":)");
    }

    companion object {
        fun newInstance(config: Serializable) = ImagePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ExtraName.CONFIG.name, config)
            }
        }
    }

    private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING }

    private val config: Config by lazy {
        arguments?.getSerializable(ExtraName.CONFIG.name) as Config
    }

    private lateinit var albumAdapter: ArrayAdapter<String>
    private lateinit var imageAdapter: ImageListRecyclerViewAdapter
    private lateinit var presenter: ImagePickerPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_imagepicker_picker, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        albumAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        imageAdapter = ImageListRecyclerViewAdapter(context)
        presenter = ImagePickerPresenter(this, context, config)
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        spinner_album.also {
            it.adapter = albumAdapter
            it.tag = Tag.SPINNER_ALBUM
            it.onItemSelectedListener = this
        }
        recycler_view.also {
            it.layoutManager = GridLayoutManager(this.context, 3)
            it.tag = Tag.IMAGE
            it.adapter = imageAdapter
        }


        imageAdapter.also {
            it.onItemClickListener = this
            it.onItemLongClickListener = this
        }


        val permission = context?.let { ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) }
        if (permission != PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
        }


        crop_view.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                val view = layoutInflater.inflate(R.layout.dialog_result, null)
                view.findViewById<ImageView>(R.id.image).setImageBitmap(bitmap)
                context?.let { AlertDialog.Builder(it).setView(view).show() }
            }

            override fun onFailure(e: Exception) {
                //Snackbar.make(context, R.string.error_failed_to_clip_image, Snackbar.LENGTH_LONG).show()
            }
        })


        crop_now.setOnClickListener(View.OnClickListener {
            if (crop_view.isOffFrame()) {
                Log.e("FUERA_FRAME","true")
               // Snackbar.make(parent, R.string.error_image_is_off_of_frame, Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }else{
                Log.e("FUERA_FRAME","false")
            }
            crop_view.crop()
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finish -> {
                presenter.saveSelected(imageAdapter.getSelectedImages())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ImagePickerContract.View

    override fun scrollToTop() {
        recycler_view.smoothScrollToPosition(0)
    }

    override fun clearAlbums() {
        albumAdapter.clear()
    }

    override fun addAlbums(items: List<Album>) {
        albumAdapter.addAll(items.map { it.folderName })
        albumAdapter.notifyDataSetChanged()
    }

    override fun clearImages() {
        imageAdapter.clear()
    }

    override fun addImages(items: List<Image>) {
        crop_view.setUri(Uri.parse(items.get(0).path))
        imageAdapter.addAll(items)
    }


    override fun requestPermissions() {
        activity?.let {
            if (ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
        }
    }

    override fun finishPickImages(items: List<Image>) {
        Intent()
                .apply { putExtra(ExtraName.PICKED_IMAGE.name, items.map { it.path }.toTypedArray()) }
                .let { activity?.setResult(RESULT_OK, it) }
    }

    override fun finish() {
        activity?.finish()
    }

    override fun onClick(view: View?) {
        when (view?.tag) {
            Tag.BUTTON_SETTING -> {
                config.packageName
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { name ->
                            Intent().apply {
                                action = ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.parse("package:" + name)
                            }.let { activity?.startActivity(it) }
                        }
            }
        }
    }

    // AdapterView.OnItemSelectedListener

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (adapterView?.tag) {
            Tag.SPINNER_ALBUM -> presenter.albumSelected(position)

        }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    // ImageListRecyclerViewAdapter.OnItemLongClickListener

    override fun onItemClick(parent: ViewGroup, view: View, position: Int, item: Image, selectable: Boolean) {
        when (parent.tag) {
            Tag.IMAGE -> {
                if (!selectable) return
                crop_view.setUri(Uri.parse(imageAdapter.get_uri(position)))
            }
        }
    }

    // ImageListRecyclerViewAdapter.OnItemClickListener

    override fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int, item: Image, selectable: Boolean): Boolean {
        when (parent.tag) {
            Tag.IMAGE -> {
                if (!selectable) return false
                imageAdapter.updateItemView(position, config.maxCount)
                crop_view.setUri(Uri.parse(imageAdapter.get_uri(position)))
               /* Intent(context, DetailActivity::class.java)
                        .apply { putExtra(ExtraName.IMAGE_PATH.name, item.path) }
                        .let { startActivity(it) }*/
                return true
            }
        }
        return false
    }
}
