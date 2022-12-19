package com.api.heys.domain.user.service

import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.*
import com.api.heys.security.domain.CustomUser
import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
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
): IUserService {
    /**
     * 회원가입
     * User, UserDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     * */
    @Transactional
    override fun signUp(dto: SignUpData, roles: List<String>): String? {
        val user: Users? = userRepository.findByPhone(dto.phone)

        if (user == null) {
            // Create User
            val newUsers = Users(
                    isAvailable = true,
                    phone = dto.phone,
                    password = passwordEncoder.encode(dto.password),
            )
            // Create UserDetail
            val newUserDetail = UserDetail(
                    users = newUsers,
                    username = dto.username,
                    gender = dto.gender,
                    age = dto.age,
            )

            dto.interests.map {
                // Create Interest Categories
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(name = it)
                }

                // InterestRelation Linking
                val rel = InterestRelations()
                rel.interest = interest
                rel.userDetail = newUserDetail
                newUserDetail.interestRelations.add(rel)
                interest.interestRelations.add(rel)
            }

            newUsers.detail = newUserDetail
            userRepository.save(newUsers)

            roles.map { newUsers.authentications.add(Authentication(users = newUsers, role = it)) }

            return jwtUtil.createJwt(newUsers.phone, newUsers.authentications.map { it.role })
        }
        return null
    }

    override fun checkMember(dto: CheckMemberData): Boolean {
        val user: Users? = userRepository.findByPhone(dto.phone)
        return user != null
    }

    @Throws(UsernameNotFoundException::class) // Require Security User Service
    override fun loadUserByUsername(username: String?): UserDetails? {
        val phone: String? = username // username 을 phone 으로 체크합니다.

        if (phone != null) {
            val usersEntity: Users? = userRepository.findByPhone(phone)
            if (usersEntity != null) {
                return CustomUser(
                        usersEntity.phone,
                        usersEntity.password,
                        usersEntity.authentications.map { SimpleGrantedAuthority(it.role) }
                )
            }
        }
        return null
    }
}