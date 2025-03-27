package com.example.kotlinproject.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageHandler {

    fun saveImageToStorage(context: Context,imageUri : Uri): String?{
        return try{
            val inputStream :InputStream? = context.contentResolver.openInputStream(imageUri)
            val storageDir = File(context.filesDir,"storage")
            if (!storageDir.exists()) {
                storageDir.mkdir()
            }
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val file = File(storageDir,fileName)

            inputStream?.use{input ->
                FileOutputStream(file).use{output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getImageFile(context: Context,filePath:String):File?{
        val file = File(filePath)
        return if(file.exists()) file else null
    }

    fun deleteImageFile(context: Context,filePath: String):Boolean{
        val file = File(filePath)
        return file.exists() && file.delete()
    }
}