package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.*
import com.querydsl.core.BooleanBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val userDetailRepository: IUserDetailRepository,
        @Autowired private val interestRepository: IInterestRepository,
): IUserService {

    @Transactional
    override fun signUp(dto: SignUpData, roles: List<String>): Users? {
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
                    password = dto.password,
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

            return newUsers
        }
        return null
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}