package com.gruvedrift.exception

class InvalidIdException(message: String = "Missing or invalid id!") : RuntimeException(message = message)

class EntityNotFoundException(
    domainEntity: DomainEntity,
    id: Int,
): RuntimeException(message = "$domainEntity with id: $id not found!")

class EntityNotCreatedException(
    domainEntity: DomainEntity,
) : RuntimeException(message = "Failed to create entity: $domainEntity")

class EntityNotUpdatedException(
    domainEntity: DomainEntity,
) : RuntimeException(message = "Failed to update entity: $domainEntity")

class EntityNotDeletedException(
    domainEntity: DomainEntity
) : RuntimeException(message = "Failed to delete entity: $domainEntity")

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