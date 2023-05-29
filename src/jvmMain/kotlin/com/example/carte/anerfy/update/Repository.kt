package com.example.carte.anerfy.update

import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence
import kotlinx.coroutines.*

interface Repository<T> {

    @OptIn(DelicateCoroutinesApi::class)
    fun <R> withTransaction(action: EntityManager.() -> R): R {
        var result: R;
        val entityManager = QuizRepository.getEntityManager()
        entityManager.use {
            it.transaction.begin();
            result = action.invoke(it);
            it.transaction.commit();
        }

        return result;
    }

    fun remove(entity: T)
    fun findAll(): List<T>
    fun find(id: Long): T
    fun save(entity: T)


}