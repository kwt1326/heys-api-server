package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.*
import com.api.heys.security.domain.CustomUser
import com.api.heys.utils.JwtUtil
import com.querydsl.core.BooleanBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.jvm.Throws

@Service
class UserService(
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val userDetailRepository: IUserDetailRepository,
        @Autowired private val interestRepository: IInterestRepository,
        @Autowired private val passwordEncoder: PasswordEncoder,
        @Autowired private val jwtUtil: JwtUtil,
): IUserService {

    @Transactional
    override fun signUp(dto: SignUpData, roles: List<String>): String? {
        val userDetail: UserDetail? = userDetailRepository.findByUsername(dto.username)

        if (userDetail == null) {
            // Create Interest Categories
            val interests: MutableSet<Interest> = dto.interests.map {
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(it)
                    interestRepository.save(interest)
                }
                interest
            }.toMutableSet()

            // Create User
            val newUsers = Users(
                    isAvailable = true,
                    phone = dto.phone,
                    password = passwordEncoder.encode(dto.password),
            )
            val newUserDetail = UserDetail(
                    users = newUsers,
                    username = dto.username,
                    gender = dto.gender,
                    age = dto.age,
                    interests = interests
            )
            newUsers.detail = newUserDetail

            userRepository.save(newUsers)

            roles.map {
                newUsers.addAuthentication(Authentication(users = newUsers, role = it))
            }

            return jwtUtil.createJwt(newUsers.phone, newUsers.authentications.map { it.role })
        }
        return null
    }

    @Throws(UsernameNotFoundException::class)
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