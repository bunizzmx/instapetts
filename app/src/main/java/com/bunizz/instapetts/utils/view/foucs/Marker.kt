package com.bunizz.instapetts.utils.view.foucs

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface Marker {
  fun onAttach(context: Context, container: ViewGroup): View?
}