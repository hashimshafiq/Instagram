package com.mindorks.bootcamp.instagram.utils.common

import android.content.Context
import android.graphics.BitmapFactory
import com.mindorks.paracamera.Camera
import com.mindorks.paracamera.Utils
import java.io.*


object FileUtils {

    fun getDirectory(context: Context, dirName: String): File {
        val file = File(context.filesDir.path + File.separator + dirName)
        if (!file.exists()) file.mkdir()
        return file
    }

    fun saveInputStreamToFile(input: InputStream, directory: File, imageName: String, height: Int): File? {
        val temp = File(directory.path + File.separator + "temp\$file\$for\$processing")
        try {
            val final = File(directory.path + File.separator + imageName + ".${Camera.IMAGE_JPG}")
            val output = FileOutputStream(temp)
            try {
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int = input.read(buffer)
                while (read != -1) {
                    output.write(buffer, 0, read)
                    read = input.read(buffer)
                }
                output.flush()
                Utils.saveBitmap(Utils.decodeFile(temp, height), final.path, Camera.IMAGE_JPG, 80)
                return final
            } finally {
                output.close()
                temp.delete()
            }
        } finally {
            input.close()
        }
    }

    fun getImageSize(file: File): Pair<Int, Int>? {
        try {
            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(file), null, o)
            return Pair(o.outWidth, o.outHeight)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}