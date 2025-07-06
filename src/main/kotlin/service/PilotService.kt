package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreatePilotRequest
import com.gruvedrift.domain.dto.request.UpdatePilotPodracerRequest
import com.gruvedrift.domain.model.Pilot
import com.gruvedrift.exception.DomainEntity.PILOT
import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.EntityNotUpdatedException
import com.gruvedrift.repository.PilotRepository

class PilotService(
    private val pilotRepository: PilotRepository
) {
    fun getPilotById(id: Int): Pilot = pilotRepository.getPilotById(id = id) ?: throw EntityNotFoundException(
        domainEntity = PILOT,
        id = id
    )

    fun createPilot(createPilotRequest: CreatePilotRequest): Int =
        pilotRepository.createPilot(createPilotRequest = createPilotRequest).takeIf { it > 0 }
            ?: throw EntityNotCreatedException(
                domainEntity = PILOT,
            )

    fun updatePilot(updatePilotPodracerRequest: UpdatePilotPodracerRequest): Int =
        pilotRepository.updatePilotPodracer(updatePilotPodracerRequest = updatePilotPodracerRequest).takeIf { it > 0 }
            ?: throw EntityNotUpdatedException(
                domainEntity = PILOT
            )

    fun deletePilot(id: Int): Int =
        pilotRepository.deletePilotById(id = id).takeIf { it > 0 } ?: throw EntityNotDeletedException(
            domainEntity = PILOT
        )
}