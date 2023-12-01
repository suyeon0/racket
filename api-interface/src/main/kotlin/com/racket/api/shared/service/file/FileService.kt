package com.racket.api.shared.service.file

import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException

@Component
class FileService {

    fun uploadFile(fileName:String, uploadPath: String, fileData: ByteArray?): String {
        this.createDirectoryIfNotExists(uploadPath)
        val fileUploadUrl = "$uploadPath/$fileName"

        try {
            this.saveFile(fileData = fileData, filePath = fileUploadUrl)
            return fileName
        } catch (e: IOException) {
            throw RuntimeException("Failed to upload file: ${e.message}", e)
        }
    }

    private fun createDirectoryIfNotExists(directoryPath: String) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                println("Directory created: $directoryPath")
            } else {
                throw IOException("Failed to create directory: $directoryPath")
            }
        }
    }

    fun deleteFile(filePath: String) {
        val deleteFile = File(filePath)
        if (deleteFile.exists()) {
            deleteFile.delete()
        } else {
            throw RuntimeException("File does not exist: $filePath")
        }
    }

    private fun generateFileName() = ObjectId().toHexString()

    private fun saveFile(fileData: ByteArray?, filePath: String) {
        FileOutputStream(filePath).use { fos ->
            fos.write(fileData!!)
        }
    }
}