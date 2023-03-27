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

            var percentage = Percentage.HUNDRED;
            if (StringUtils.hasNotText(userDetail.introduceText)) {
                percentage = Percentage.ZERO;
            }

            if (CollectionUtils.isEmpty(userDetail.profileLink) ||
                StringUtils.hasNotText(userDetail.job) ||
                StringUtils.hasNotText(userDetail.capability)) {
                percentage = Percentage.FIFTY
            }
            return percentage.getValue();
        }
    }
}

