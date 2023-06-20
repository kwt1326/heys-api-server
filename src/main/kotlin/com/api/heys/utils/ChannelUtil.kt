package com.api.heys.utils

import org.springframework.beans.factory.annotation.Value
import com.api.heys.constants.ChannelConstants.channelThumbnailMapper
import com.api.heys.constants.ChannelConstants.channelThumbnailsDefault

class ChannelUtil {
    @Value("\${custom.static-file-host}")
    lateinit var staticFileHost: String

    fun getChannelImage(interests: List<String>): Map<String, String> {
        val result = mutableMapOf("host" to staticFileHost)
        var max = 0
        var map = channelThumbnailsDefault

        for (item in channelThumbnailMapper) {
            val count = item.key.count { interests.contains(it) }
            if (max < count) {
                max = count
                map = item.value
            }
        }
        result += map
        return result
    }
}