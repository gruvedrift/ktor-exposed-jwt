package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.domain.dto.response.PodracerAnalyticsResponse
import com.gruvedrift.domain.dto.response.toAnalyticsResponse
import com.gruvedrift.domain.model.Podracer
import com.gruvedrift.exception.DomainEntity.PODRACER
import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.repository.PodracerRepository

class PodracerService(
    private val podracerRepository: PodracerRepository
) {
    fun getPodracerById(id: Int): Podracer = podracerRepository.getById(id = id) ?: throw EntityNotFoundException(
        domainEntity = PODRACER,
        id = id
    )

    fun createPodracer(createPodracerRequest: CreatePodracerRequest): Int =
        podracerRepository.createPodracer(createPodracerRequest = createPodracerRequest).takeIf { it > 0 }
            ?: throw EntityNotCreatedException(domainEntity = PODRACER)

    fun getEngineModel(id: Int): String =
        podracerRepository.getEngineModel(podracerId = id) ?: throw EntityNotFoundException(
            domainEntity = PODRACER,
            id = id
        )

    fun deletePodracer(id: Int): Int = podracerRepository.deletePodracer(id = id).takeIf { it > 0 }
        ?: throw EntityNotDeletedException(domainEntity = PODRACER)

    fun getAnalyticsForPodracer(id: Int): PodracerAnalyticsResponse {
        return podracerRepository.getAnalyticsCoreData(id = id)?.toAnalyticsResponse() ?: throw EntityNotFoundException(
            domainEntity = PODRACER,
            id = id
        )
    }
}

