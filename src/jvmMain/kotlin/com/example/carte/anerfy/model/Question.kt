package com.example.carte.anerfy.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jakarta.persistence.*

@Entity
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false)
    var content: String,
    @Column(nullable = false)
    var answer: String,
    @ElementCollection
    var possibleAnswers: Array<String>
) {
    companion object {
        val jsonConverter: Gson = GsonBuilder().setPrettyPrinting().create();
    }



    override fun toString(): String {

        return jsonConverter.toJson(this);
    }

}