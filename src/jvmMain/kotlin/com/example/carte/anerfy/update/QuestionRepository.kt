package com.example.carte.anerfy.update

import com.example.carte.anerfy.model.Question
import jakarta.persistence.Persistence

object QuestionRepository : Repository<Question> {


    val entityManagerFactory = Persistence.createEntityManagerFactory("QuizzesPerU")


    fun getEntityManager() = entityManagerFactory.createEntityManager();


    override fun remove(entity: Question) {
        withTransaction {
            remove(entity)
            detach(entity)
        }
    }

    override fun findAll(): List<Question> {

        return withTransaction {
            createQuery("select q from Question q").resultList.map { it as Question };
        }
    }

    override fun find(id: Long): Question {

        return withTransaction {
            find(Question::class.java, id)
        }

    }

    override fun save(entity: Question) {
        withTransaction {
            persist(entity)
        }
    }


}