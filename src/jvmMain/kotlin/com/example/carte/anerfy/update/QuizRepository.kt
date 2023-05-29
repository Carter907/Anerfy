package com.example.carte.anerfy.update

import com.example.carte.anerfy.model.Quiz
import jakarta.persistence.Persistence
import kotlinx.coroutines.*


object QuizRepository : Repository<Quiz> {


    val entityManagerFactory = Persistence.createEntityManagerFactory("QuizzesPerU")


    fun getEntityManager() = entityManagerFactory.createEntityManager();


    override fun remove(entity: Quiz) {
        withTransaction {
            remove(if (contains(entity)) entity else merge(entity))
}
    }

    override fun findAll(): List<Quiz> {

        return withTransaction {
            createQuery("select q from Quiz q").resultList.map { it as Quiz };
        }
    }

    override fun find(id: Long): Quiz {

        return withTransaction {
            find(Quiz::class.java, id)
        }

    }

    override fun save(entity: Quiz) {
        withTransaction {
            persist(entity)
        }
    }

}