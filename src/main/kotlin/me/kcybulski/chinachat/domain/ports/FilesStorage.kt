package me.kcybulski.chinachat.domain.ports

import me.kcybulski.chinachat.domain.model.File
import java.util.concurrent.CompletableFuture

interface FilesStorage {

    fun upload(bytes: ByteArray): CompletableFuture<File>

}
