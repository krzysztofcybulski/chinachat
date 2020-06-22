package me.kcybulski.chinachat.infrastructure

import me.kcybulski.chinachat.domain.File
import me.kcybulski.chinachat.domain.FilesStorage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

class InMemoryFilesStorage : FilesStorage {

    override fun upload(bytes: ByteArray): CompletableFuture<File> = completedFuture(File(bytes.toString()))
}
