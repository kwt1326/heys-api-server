package com.api.heys.domain.notification.vo

data class NotificationResponseVo(
    val channelId : Long?,
    val content : String,
    val createdAt : String?,
    val isRead : Boolean
){
    // 변하지 않음
    val title : String = "헤이즈 활동 알림"
}