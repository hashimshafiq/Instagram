package com.hashim.instagram.utils.common

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import com.mindorks.paracamera.Camera
import com.mindorks.paracamera.Utils
import java.io.*


object FileUtils {

    private val fileList: ArrayList<String> = ArrayList()

    // /storage/emulated/0
    var ROOT_DIR: String = Environment.getExternalStorageDirectory().path
    var PICTURES = "$ROOT_DIR/Pictures"
    var CAMERA = "$ROOT_DIR/DCIM/camera"

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
        return try {
            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(file), null, o)
            Pair(o.outWidth, o.outHeight)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getDirectoryPaths(dir: File): ArrayList<String> {
//        val pathArray: ArrayList<String> = ArrayList()
//        val file = File(directory)
//        val listfiles = file.listFiles()
//        for (i in listfiles.indices) {
//            if (listfiles[i].isDirectory) {
//                pathArray.add(listfiles[i].absolutePath)
//            }
//        }
//        return pathArray

        val listFile: Array<File> = dir.listFiles()
        if (listFile.isNotEmpty()) {
            for (file in listFile) {
                if (file.isDirectory) {
                    getDirectoryPaths(file)
                } else {
                    if (file.name.endsWith(".png")
                        || file.name.endsWith(".jpg")
                        || file.name.endsWith(".jpeg")
                        || file.name.endsWith(".gif")
                        || file.name.endsWith(".bmp")
                        || file.name.endsWith(".webp")
                    ) {
                        val temp = file.path.substring(0, file.path.lastIndexOf('/'))
                        if (!fileList.contains(temp) && !temp.contains(".")) fileList.add(temp)
                    }
                }
            }
        }
        return fileList
    }

    fun getFilePaths(directory: String?): ArrayList<String>? {
        val pathArray: ArrayList<String> = ArrayList()
        val file = File(directory)
        val listfiles = file.listFiles()
        for (i in listfiles.indices) {
            if (listfiles[i].isFile) {
                pathArray.add(listfiles[i].absolutePath)
            }
        }
        return pathArray
    }
}