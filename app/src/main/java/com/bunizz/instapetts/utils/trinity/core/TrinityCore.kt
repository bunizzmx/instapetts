package com.bunizz.instapetts.utils.trinity.core

import android.content.Context
import com.bunizz.instapetts.utils.trinity.editor.TrinityVideoEditor
import com.bunizz.instapetts.utils.trinity.editor.TrinityVideoExport
import com.bunizz.instapetts.utils.trinity.editor.VideoEditor
import com.bunizz.instapetts.utils.trinity.editor.VideoExport

object TrinityCore {

  /**
   * 创建视频编辑实例
   * @param context Android上下文
   * @return 返回创建的视频编辑实例
   */
  fun createEditor(context: Context): TrinityVideoEditor {
    return VideoEditor(context)
  }

  fun createExport(context: Context): VideoExport {
    return TrinityVideoExport(context)
  }
}