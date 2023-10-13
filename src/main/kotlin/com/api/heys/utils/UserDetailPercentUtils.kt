package com.api.heys.utils

import com.api.heys.constants.enums.Percentage
import com.api.heys.entity.UserDetail
import org.springframework.util.CollectionUtils

class UserDetailPercentUtils {

    companion object {
        fun calculateUserDetailPercentage(userDetail: UserDetail?) : Int {

            if (userDetail == null) {
                throw IllegalArgumentException("#### 유저의 정보가 없습니다.")
            }

            if (StringUtils.hasNotText(userDetail.introduceText)) {
                return Percentage.ZERO.getValue()
            }

            if (CollectionUtils.isEmpty(userDetail.profileLink) ||
                StringUtils.hasNotText(userDetail.job) ||
                StringUtils.hasNotText(userDetail.capability)) {
                return Percentage.FIFTY.getValue()
            }
            return Percentage.HUNDRED.getValue()
        }
    }
}

