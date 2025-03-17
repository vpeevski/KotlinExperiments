package com.builder.dsl

class SqlQueryBuilder {
    private val columns = mutableListOf<String>()
    private lateinit var table: String
    private lateinit var condition: Condition

    fun select(vararg columnNames: String) {
        columns.addAll(columnNames)
    }

    fun from(tableName: String) {
        table = tableName
    }

    fun where(conditionInitializer: Condition.() -> Unit) {
        condition = And().apply(conditionInitializer)
    }

    abstract class Condition {
        protected val conditions = mutableListOf<Condition>()
        private fun addCondition(condition: Condition) {
            conditions.add(condition)
        }

        infix fun String.eq(value: Any?) {
            addCondition(Eq(this, value))
        }

        fun and(initializer: Condition.() -> Unit) {
            addCondition(And().apply(initializer))
        }

        fun or(initializer: Condition.() -> Unit) {
            addCondition(Or().apply(initializer))
        }
    }

    data class Eq(
        private val column: String,
        private val value: Any?,
    ) : Condition() {
        override fun toString(): String {
            return when (value) {
                null -> "$column is null"
                is String -> "$column = '$value'"
                else -> "$column = $value"
            }
        }
    }

    abstract class CompositeCondition(private val separator: String) : Condition() {
        override fun toString(): String = if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(prefix = "(", postfix = ")", separator = separator)

        }
    }

    class And : CompositeCondition(" and ")
    class Or : CompositeCondition(" or ")

    fun build(): String {
        val columnsToSelect = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val condition = if (::condition.isInitialized) " where $condition" else ""
        return "SELECT $columnsToSelect FROM $table$condition"
    }
}

fun query(initializer: SqlQueryBuilder.() -> Unit): SqlQueryBuilder =
    SqlQueryBuilder().apply(initializer)

fun main() {
    val query1 = query {
        select("FirstName", "LastName")
        from("Person")
        where {
            "FirstName" eq "Pesho"
            and {
                "LastName" eq "Peshev"
            }
        }
    }.build()
    println(query1)
}

