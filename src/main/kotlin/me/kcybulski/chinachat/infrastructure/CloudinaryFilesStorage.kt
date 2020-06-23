package me.kcybulski.chinachat.infrastructure

import com.cloudinary.Cloudinary
import me.kcybulski.chinachat.domain.model.File
import me.kcybulski.chinachat.domain.ports.FilesStorage
import ratpack.exec.Blocking
import java.lang.System.getenv
import java.util.concurrent.CompletableFuture

class CloudinaryFilesStorage : FilesStorage {

    private val cloudinary = Cloudinary(getenv("CLOUDINARY_URL"))

    override fun upload(bytes: ByteArray): CompletableFuture<File> = Blocking.get {
        cloudinary.uploader().upload(bytes, mutableMapOf<String, String>())
    }
        .map { File(it["secure_url"] as String) }.toCompletableFuture()

}
