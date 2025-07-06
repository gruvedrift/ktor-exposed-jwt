package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreateEngineRequest
import com.gruvedrift.domain.dto.request.UpdateEngineEffectRequest
import com.gruvedrift.domain.model.Engine
import com.gruvedrift.exception.DomainEntity.ENGINE
import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.EntityNotUpdatedException
import com.gruvedrift.repository.EngineRepository

class EngineService(
    private val engineRepository: EngineRepository
) {
    fun getEngineById(id: Int): Engine =
        engineRepository.getEngineById(id = id) ?: throw EntityNotFoundException(domainEntity = ENGINE, id = id)

    fun createEngine(createEngineRequest: CreateEngineRequest): Int =
        engineRepository.createEngine(createEngineRequest = createEngineRequest).takeIf { it > 0 }
            ?: throw EntityNotCreatedException(domainEntity = ENGINE)

    fun updateEngineEffect(updateEngineEffectRequest: UpdateEngineEffectRequest): Int =
        engineRepository.updateEngineEffect(updateEngineRequest = updateEngineEffectRequest).takeIf { it > 0 }
            ?: throw EntityNotUpdatedException(domainEntity = ENGINE)

    fun deleteEngine(id: Int): Int =
        engineRepository.deleteEngine(id = id).takeIf { it > 0 }
            ?: throw EntityNotDeletedException(domainEntity = ENGINE)
}