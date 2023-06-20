package com.api.heys.domain.aws.push.vo

data class TargetPushMessageVo(
    override val title : String? = null,
    override val content : String,
    val endPoints : Set<String>,
) : PushMessageVo(title = title, content = content)