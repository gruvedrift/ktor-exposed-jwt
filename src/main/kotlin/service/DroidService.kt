package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreateDroidRequest
import com.gruvedrift.domain.dto.request.UpdateDroidRequest
import com.gruvedrift.domain.model.Droid
import com.gruvedrift.exception.DomainEntity.DROID
import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.EntityNotUpdatedException
import com.gruvedrift.repository.DroidRepository

class DroidService(
    private val droidRepository: DroidRepository
) {
    fun getDroidById(id: Int): Droid =
        droidRepository.getDroidById(id = id) ?: throw EntityNotFoundException(domainEntity = DROID, id = id)

    fun getAllByPilotId(pilotId: Int): List<Droid> =
        droidRepository.getAllByPilotId(pilotId = pilotId)

    fun createDroid(createDroidRequest: CreateDroidRequest): Int {
        val createdDroidId = droidRepository.createDroid(createDroidRequest = createDroidRequest)
        if (createdDroidId <= 0) throw EntityNotCreatedException(domainEntity = DROID)
        return createdDroidId
    }

    fun updateDroidCrew(updateDroidRequest: UpdateDroidRequest): Int =
        droidRepository.updateCrewId(updateDroidRequest = updateDroidRequest).takeIf { it > 0 }
            ?: throw EntityNotUpdatedException(domainEntity = DROID)

    fun deleteDroid(id: Int): Int =
        droidRepository.deleteDroid(id = id).takeIf { it > 0 } ?: throw EntityNotDeletedException(domainEntity = DROID)
}