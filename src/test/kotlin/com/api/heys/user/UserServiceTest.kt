package com.api.heys.user

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.Gender
import com.api.heys.domain.user.dto.AdminSignUpData
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.service.UserService
import com.api.heys.entity.IUserRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.ActiveProfiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class UserServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: IUserRepository
) {
    private val commonSignUpData = CommonSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
        age = 29,
        gender = Gender.Male,
        interests = mutableSetOf("교육", "자기계발"),
    )
    private val adminSignUpData = AdminSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
    )

    /**
     * Admin User 생성 후 같은 핸드폰 번호로 Common User 생성 시 role 과 데이터가 정상적으로 들어가는지 테스트
     * */
    @Test
    fun signUp_AdminUserCreate_Continue_CommonUserCreate() {
        userService.signUp(adminSignUpData, DefaultString.adminRole)
        userService.signUp(commonSignUpData, DefaultString.commonRole)

        val users = userRepository.findAll()

        assertThat(users.count()).isEqualTo(1)

        val user = users.first()

        val userDetail = user.detail

        assertThat(userDetail).isNotNull
        assertThat(userDetail!!.interestRelations.find { it.interest!!.name == "교육" }).isNotNull
        assertThat(userDetail.interestRelations.find { it.interest!!.name == "자기계발" }).isNotNull

        assertThat(user.authentications.find { it.role == DefaultString.commonRole }).isNotNull
        assertThat(user.authentications.find { it.role == DefaultString.adminRole }).isNotNull

        assertThat(userDetail.age).isEqualTo(29)
        assertThat(userDetail.gender).isEqualTo(Gender.Male)
    }

    /**
     * Common User 생성 후 같은 핸드폰 번호로 Admin User 생성 시 role 과 데이터가 정상적으로 들어가는지 테스트
     * */
    @Test
    fun signUp_CommonUserCreate_Continue_AdminUserCreate() {
        userService.signUp(commonSignUpData, DefaultString.commonRole)
        userService.signUp(adminSignUpData, DefaultString.adminRole)

        val users = userRepository.findAll()

        assertThat(users.count()).isEqualTo(1)

        val user = users.first()

        val userDetail = user.detail

        assertThat(userDetail).isNotNull
        assertThat(userDetail!!.interestRelations.find { it.interest!!.name == "교육" }).isNotNull
        assertThat(userDetail.interestRelations.find { it.interest!!.name == "자기계발" }).isNotNull

        assertThat(user.authentications.find { it.role == DefaultString.commonRole }).isNotNull
        assertThat(user.authentications.find { it.role == DefaultString.adminRole }).isNotNull

        assertThat(userDetail.age).isEqualTo(29)
        assertThat(userDetail.gender).isEqualTo(Gender.Male)
    }
}