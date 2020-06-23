package me.kcybulski.chinachat.infrastructure

import me.kcybulski.chinachat.domain.model.File
import me.kcybulski.chinachat.domain.ports.FilesStorage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

class InMemoryFilesStorage : FilesStorage {

    override fun upload(bytes: ByteArray): CompletableFuture<File> = completedFuture(
        File(bytes.toString())
    )
}
