package com.bunizz.instapetts.utils.view.listener

import com.bunizz.instapetts.utils.view.entity.Effect


interface OnEffectTouchListener {

  fun onEffectTouchEvent(event: Int, effect: Effect)
}