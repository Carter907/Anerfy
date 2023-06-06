package com.example.carte.anerfy.model

import com.google.gson.GsonBuilder
import jakarta.persistence.*

@Entity
class Quiz (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    var description: String,
    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    var questions: Set<Question>
) {
    companion object {
        val printer = GsonBuilder().setPrettyPrinting().create();
    }



    override fun toString(): String {

        return printer.toJson(this);
    }

}