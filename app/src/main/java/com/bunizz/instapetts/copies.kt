package com.bunizz.instapetts

import android.content.Context
import android.text.TextUtils
import android.util.Log
import java.io.*



class copies(context : Context) {

    private companion object {
        fun copiesss(source: String, targetPath: String,context: Context) {
            copyAssets(source,targetPath,context)
        }

        private fun copy(source: String, targetPath: String,context :Context) {
            if (TextUtils.isEmpty(source) || TextUtils.isEmpty(targetPath)) {
                return
            }
            val dest = File(targetPath)
            dest.parentFile?.mkdirs()
            try {
                val inputStream = BufferedInputStream(context.assets.open(source))
                val out = BufferedOutputStream(FileOutputStream(dest))
                val buffer = ByteArray(2048)
                var length: Int
                while (true) {
                    length = inputStream.read(buffer)
                    if (length < 0) {
                        break
                    }
                    out.write(buffer, 0, length)
                }
                out.close()
                inputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /**
         * 拷贝assets文件下文件到指定路径
         * @param assetDir  源文件/文件夹
         * @param dir  目标文件夹
         */
        private fun copyAssets(assetDir: String, targetDir: String,context :Context) {
            Log.e("LOCALIZACION_FILES","->" + assetDir + "/" +targetDir )
            if (TextUtils.isEmpty(assetDir) || TextUtils.isEmpty(targetDir)) {
                return
            }
            val separator = File.separator
            try {
                // 获取assets目录assetDir下一级所有文件以及文件夹
                val fileNames = context.assets.list(assetDir) ?: return
                // 如果是文件夹(目录),则继续递归遍历
                if (fileNames.isNotEmpty()) {
                    val targetFile = File(targetDir)
                    if (!targetFile.exists() && !targetFile.mkdirs()) {
                        return
                    }
                    for (fileName in fileNames) {
                        copyAssets(assetDir + separator + fileName, targetDir + separator + fileName,context)
                    }
                } else { // 文件,则执行拷贝
                    copy(assetDir, targetDir,context)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

   

    
}