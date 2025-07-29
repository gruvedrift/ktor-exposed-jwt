package com.gruvedrift.service

import com.gruvedrift.exception.DomainEntity.USER
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.repository.AuthRepository
import com.gruvedrift.repository.User

class AuthService(
    private val authRepository: AuthRepository
) {
    fun getById(id: Int): User = authRepository.getUserById(id = id) ?: throw EntityNotFoundException(
        domainEntity = USER,
        id = id
    )
}