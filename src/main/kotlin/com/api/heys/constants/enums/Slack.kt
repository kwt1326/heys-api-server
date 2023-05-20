package com.api.heys.constants.enums

enum class Slack(private val url : String) {

    MESSAGE("https://slack.com/api/chat.postMessage");

    fun getUrl() : String {
        return this.url
    }
}