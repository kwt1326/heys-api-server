package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val userDetailRepository: IUserDetailRepository,
        @Autowired private val interestRepository: IInterestRepository,
        @Autowired private val authenticationRepository: IAuthenticationRepository,
): IUserService {

    @Transactional
    override fun signUp(dto: SignUpData): User? {
        val userDetail: UserDetail? = userDetailRepository.findByUsername(dto.username)

        if (userDetail == null) {
            val interests: MutableSet<Interest> = dto.interests.map {
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(it)
                    interestRepository.save(interest)
                }
                interest
            }.toMutableSet()

            val newUser = User(isAvailable = true)
            val newUserDetail = UserDetail(
                    user = newUser,
                    username = dto.username,
                    gender = dto.gender,
                    phone = dto.phone,
                    age = dto.age,
                    interests = interests
            )
            val newAuthentication = Authentication(
                    user = newUser,
                    phone = dto.phone,
                    password = dto.password, // Front 에서 암호화되어 전송되어 올 예정(서버 측 암호화 X)
            )
            newUser.detail = newUserDetail

            userRepository.save(newUser)
            authenticationRepository.save(newAuthentication)

            return newUser
        }
        return null
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}