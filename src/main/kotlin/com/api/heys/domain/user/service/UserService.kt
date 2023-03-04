package com.api.heys.domain.user.service

import com.api.heys.constants.enums.Gender
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.*
import com.api.heys.security.domain.CustomUser
import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    @Autowired private val userRepository: IUserRepository,
    @Autowired private val interestRepository: InterestRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val jwtUtil: JwtUtil,
) : IUserService {
    /**
     * 회원가입
     * User, UserDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     * 가입되어 있으면, 새로운 Role 추가. 이미 해당 Role 이 있으면, 실패 처리
     * 미가입시, User 생성 및 Role 추가
     * [admin user created -> common user role add] flow 의 경우 common user 에 필요한 데이터 업데이트
     * 로그인 가능한 토큰 반환
     * */
    @Transactional
    override fun <T: SignUpData> signUp(dto: T, role: String): String? {
        var user: Users? = userRepository.findByPhone(dto.phone)
        var detail: UserDetail? = null

        // 유저 생성
        if (user == null) {
            // Create User
            user = Users(
                isAvailable = true,
                phone = dto.phone,
                password = passwordEncoder.encode(dto.password),
            )
            // Create UserDetail
            detail = UserDetail(
                users = user,
                username = dto.username,
                birthDate = dto.birthDate,
                gender = if (dto is CommonSignUpData) dto.gender else Gender.NonBinary,
            )
        } else {
            detail = user.detail
        }

        // Admin 가입은 성별, 관심분야 항목이 필요없다. Common Role 추가 혹은 생성시 업데이트
        if (dto is CommonSignUpData) {
            val presentDetail: UserDetail = detail!!

            presentDetail.gender = dto.gender

            dto.interests.map {
                // Create Interest Categories
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(name = it)
                }

                // InterestRelation Linking
                val rel = InterestRelations()
                rel.interest = interest
                rel.userDetail = presentDetail
                presentDetail.interestRelations.add(rel)
                interest.interestRelations.add(rel)
            }
        }

        user.detail = detail
        userRepository.save(user)

        // 이미 해당 Role 존재하면 가입 실패
        if (user.authentications.find { it.role == role } != null) return null

        user.authentications.add(Authentication(users = user, role = role))
        userRepository.save(user)

        return jwtUtil.createJwt(user.phone, user.authentications.map { it.role })
    }

    override fun withDrawal(id: Number, role: String): ResponseEntity<Boolean> {
        // TODO: role 제거, 유저 비활성화 처리 (removedAt) flow 설계 필요
        return ResponseEntity.status(403).body(false)
    }

    override fun checkMember(dto: CheckMemberData): Boolean {
        val user: Users? = userRepository.findByPhone(dto.phone)
        return user != null
    }

    override fun findByPhone(phone: String): Users? {
        return userRepository.findByPhone(phone)
    }

    /**
     * Require Spring Security User Service
     * */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? {
        val phone: String? = username // username 을 phone 으로 체크합니다.

        if (phone != null) {
            val usersEntity: Users? = userRepository.findByPhone(phone)
            if (usersEntity != null) {
                return CustomUser(
                    usersEntity.phone,
                    usersEntity.password,
                    usersEntity.isAvailable,
                    usersEntity.authentications.map { SimpleGrantedAuthority(it.role) }
                )
            }
        }
        return null
    }
}