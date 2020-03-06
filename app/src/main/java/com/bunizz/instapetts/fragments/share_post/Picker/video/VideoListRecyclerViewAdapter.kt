package com.bunizz.instapetts.fragments.share_post.Picker.video

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bunizz.instapetts.R
import com.bunizz.instapetts.utils.imagePicker.data.Image
import com.bunizz.instapetts.utils.imagePicker.data.Video
import kotlinx.android.synthetic.main.item_imagepicker_image.view.*
import kotlinx.android.synthetic.main.item_imagepicker_image.view.image_thumbnail
import kotlinx.android.synthetic.main.item_imagepicker_image.view.text_index
import kotlinx.android.synthetic.main.item_imagepicker_image.view.view_selected
import kotlinx.android.synthetic.main.item_videopicker_video.view.*
import java.io.File

class VideoListRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(
                parent: ViewGroup,
                view: View,
                position: Int,
                item: Video,
                selectable: Boolean
        ) {
        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(
                parent: ViewGroup,
                view: View,
                position: Int,
                item: Image,
                selectable: Boolean
        ) = false
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private lateinit var parent: ViewGroup
    private val selectedImages = ArrayList<Video>()
    private var selectable: Boolean = true

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = ArrayList<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent
        return ViewHolder(inflater.inflate(R.layout.item_videopicker_video, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView
        val item = getItem(position)
        Glide.with(context)
                .load(File(item.path))
                .into(itemView.image_thumbnail)
        itemView.text_duration.text = return_secs(items.get(position).duration)
        var isSelectedImage = false
        itemView.also {
            it.setOnClickListener { view ->
                run {
                    if (!selectable && !isSelectedImage) {
                        Toast.makeText(context, "Solo puedes seleccionar 5 imagenes", Toast.LENGTH_LONG).show()
                    }
                    onItemClickListener
                            ?.onItemClick(
                                    parent,
                                    view,
                                    position,
                                    getItem(position),
                                    selectable || isSelectedImage

                            )
                }
            }

        }
    }

    fun return_secs( duration : String ) :String{
        var duration_video : String ="";
        var duration_int :Int =0;
        duration_int = duration.toInt()
        duration_video = (duration_int / 1000).toString() + " sec"
        return  duration_video;
    }


    override fun getItemCount(): Int {
        return items.size
    }

// Public

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addAll(items: List<Video>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: Video) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun updateItemView(position: Int, maxSelectCount: Int) {
        if (selectedImages.contains(getItem(position))) {
            selectedImages.remove(getItem(position))
            selectable = selectedImages.size < maxSelectCount
            notifyDataSetChanged()
            return
        }

        selectedImages.add(getItem(position))
        if (selectedImages.size == maxSelectCount) {
            selectable = false
            notifyDataSetChanged()
            return
        }
        notifyItemChanged(position)
    }

    fun  get_uri(position: Int) :String{
        return items[position].path;
    }

    fun getSelectedImages(): List<Video> {
        return selectedImages
    }

// private

    private fun getItem(position: Int): Video {
        return items[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
