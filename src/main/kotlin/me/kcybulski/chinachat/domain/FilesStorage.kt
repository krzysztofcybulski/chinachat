package me.kcybulski.chinachat.domain

import java.util.concurrent.CompletableFuture

interface FilesStorage {

    fun upload(bytes: ByteArray): CompletableFuture<File>

}
