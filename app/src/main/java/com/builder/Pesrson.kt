package com.builder

data class Person(
    val name: String,
    val age: Int,
    val sex: String,
) {
    class Builder {
        private var name: String? = null
        private var age: Int = 0
        private var sex: String = "Female"
        fun withName(name: String) = apply {
            this.name = name
        }

        fun withAge(age: Int) = apply {
            this.age = age
        }

        fun withSex(sex: String) = apply {
            this.sex = sex
        }

        fun build(): Person {
            if (name.isNullOrEmpty()) {
                throw IllegalStateException("Person name is required")
            }
            return Person(name = name!!, age = age, sex = sex)
        }
    }

    override fun toString(): String {
        return "Person (name: $name, age: $age, sex: $sex)"
    }

}

fun person(block: Person.Builder.() -> Unit): Person {
    val builder = Person.Builder()
    builder.block()
    return builder.build()
}

fun main() {
    val person1 = person {
        withName("Ivan")
        withAge(33)
        withSex("Male")
    }
    val person2 = person {
        withName("Gergana")
        withAge(20)
        withSex("Female")
    }
    println(person1)
    println(person2)
}