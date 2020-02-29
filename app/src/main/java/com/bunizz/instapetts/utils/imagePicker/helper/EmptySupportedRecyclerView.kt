package com.bunizz.instapetts.utils.imagePicker.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptySupportedRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    var emptyView: View? = null

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (adapter == null || emptyView == null) {
                return
            }

            val isEmpty = adapter?.itemCount == 0
            emptyView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
            visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }
}