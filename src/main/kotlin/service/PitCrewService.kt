package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreatePitCrewRequest
import com.gruvedrift.domain.dto.request.UpdatePitCrewRequest
import com.gruvedrift.domain.model.PitCrew
import com.gruvedrift.exception.DomainEntity.PIT_CREW
import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.EntityNotUpdatedException
import com.gruvedrift.repository.PitCrewRepository

class PitCrewService(
    private val pitCrewRepository: PitCrewRepository
) {
    fun getPitCrewById(id: Int): PitCrew = pitCrewRepository.getPitCrewById(id = id) ?: throw EntityNotFoundException(
        domainEntity = PIT_CREW,
        id = id
    )

    fun createPitCrew(createPitCrewRequest: CreatePitCrewRequest): Int =
        pitCrewRepository.createPitcrew(createPitCrewRequest = createPitCrewRequest).takeIf { it > 0 }
            ?: throw EntityNotCreatedException(domainEntity = PIT_CREW)

    fun updatePitCrew(updatePitCrewRequest: UpdatePitCrewRequest): Int =
        pitCrewRepository.updatePitCrew(updatePitCrewRequest = updatePitCrewRequest).takeIf { it > 0 }
            ?: throw EntityNotUpdatedException(domainEntity = PIT_CREW)

    fun deletePitCrew(id: Int): Int = pitCrewRepository.deletePitcrew(id = id).takeIf { it > 0 }
        ?: throw EntityNotDeletedException(domainEntity = PIT_CREW)
}