package com.api.heys.utils

import com.api.heys.constants.enums.Gender
import com.api.heys.entity.UserDetail
import com.api.heys.entity.UserProfileLink
import com.api.heys.entity.Users
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class UserDetailPercentUtilsTest {

    var userDetail : UserDetail? = null;

    @BeforeEach
    fun createBasicMockUser () {
        val users = Users(
            phone = "01012341234",
            password = "12341234",
        )
        userDetail = UserDetail(
            users = users,
            username = "Kim",
            gender = Gender.Male,
            birthDate = LocalDate.of(2002, 2, 22)
        )
    }

    @Test
    @DisplayName("유저 상세보기가 0%인 사람")
    @Throws(Exception::class)
    fun userDetailPercentageZero () {
        // given
        // when
        val percentage = UserDetailPercentUtils.calculateUserDetailPercentage(userDetail)
        // then
        assertThat(percentage).isEqualTo(0)
    }

    @Test
    @DisplayName("유저 상세보기가 50%인 사람")
    @Throws(Exception::class)
    fun userDetailPercentageFifty () {
        // given
        userDetail?.introduceText = "안녕하세요"
        // when
        val percentage = UserDetailPercentUtils.calculateUserDetailPercentage(userDetail)
        // then
        assertThat(percentage).isEqualTo(50)
    }

    @Test
    @DisplayName("50% 유저에 1가지만 추가로 있는 경우")
    @Throws(Exception::class)
    fun userDetailPercentageFiftyAddOne () {
        // given
        userDetail?.introduceText = "안녕하세요"
        userDetail?.job = "직장인"
        // when
        val percentage = UserDetailPercentUtils.calculateUserDetailPercentage(userDetail)
        // then
        assertThat(percentage).isEqualTo(50)
    }

    @Test
    @DisplayName("50% 유저에 2가지만 추가로 있는 경우")
    @Throws(Exception::class)
    fun userDetailPercentageFiftyAddTwo () {
        // given
        userDetail?.introduceText = "안녕하세요"
        userDetail?.capability = "스프링"
        userDetail?.profileLink = setOf(
            UserProfileLink (
                userDetail = userDetail!!,
                linkUrl = "https://www.naver.com"
            ))
        // when
        val percentage = UserDetailPercentUtils.calculateUserDetailPercentage(userDetail)
        // then
        assertThat(percentage).isEqualTo(50)
    }

    @Test
    @DisplayName("유저 상세보기가 100%인 사람")
    @Throws(Exception::class)
    fun userDetailPercentageHundred () {
        // given
        userDetail?.introduceText = "안녕하세요"
        userDetail?.job = "직장인"
        userDetail?.capability = "스프링"
        userDetail?.profileLink = setOf(
            UserProfileLink (
            userDetail = userDetail!!,
            linkUrl = "https://www.naver.com"
        ))
        // when
        val percentage = UserDetailPercentUtils.calculateUserDetailPercentage(userDetail)
        // then
        assertThat(percentage).isEqualTo(100)
    }
}