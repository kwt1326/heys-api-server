package com.api.heys.domain.user.dto

data class ModifyPasswordRequest (
     val phoneNumber : String,
     val password : String
)