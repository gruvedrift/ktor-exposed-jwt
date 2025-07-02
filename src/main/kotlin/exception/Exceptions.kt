package com.gruvedrift.exception

class InvalidIdException(message: String = "Missing or invalid id!") : RuntimeException(message)

class EntityNotFoundException(
    domainEntity: DomainEntity,
    id: Int,
): RuntimeException("$domainEntity with id: $id not found!")

class EntityNotCreatedException(
    domainEntity: DomainEntity,
) : RuntimeException("Failed to create entity: $domainEntity")

class EntityNotUpdatedException(
    domainEntity: DomainEntity,
) : RuntimeException("Failed to update entity: $domainEntity")

class EntityNotDeletedException(
    domainEntity: DomainEntity
) : RuntimeException("Failed to delete entity: $domainEntity")

enum class Operation {
    CREATE,
    READ,
    UPDATE,
    DELETE,
}

enum class DomainEntity {
    DROID,
    ENGINE,
    PIT_CREW,
    PODRACER,
    PILOT,
}