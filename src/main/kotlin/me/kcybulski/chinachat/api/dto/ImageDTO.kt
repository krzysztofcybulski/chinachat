package me.kcybulski.chinachat.api.dto

import me.kcybulski.chinachat.domain.File

data class ImageResponse(val url: String) {

    companion object {

        fun fromFile(file: File) = ImageResponse(file.url)

    }

}
