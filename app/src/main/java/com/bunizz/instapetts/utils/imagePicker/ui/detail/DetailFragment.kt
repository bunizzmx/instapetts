package com.bunizz.instapetts.utils.imagePicker.ui.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bunizz.instapetts.R
import com.bunizz.instapetts.utils.imagePicker.helper.ExtraName

import kotlinx.android.synthetic.main.fragment_imagepicker_detail.*
import java.io.File

class DetailFragment : Fragment() {
    companion object {
        fun newInstance(path: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ExtraName.IMAGE_PATH.name, path)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_imagepicker_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments?.getString(ExtraName.IMAGE_PATH.name)

        setHasOptionsMenu(true)

        Glide.with(this!!.context!!)
                .load(File(path))
                .into(image_detail)
    }
}
