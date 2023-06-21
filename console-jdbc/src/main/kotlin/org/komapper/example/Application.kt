package org.komapper.example

import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase
import org.komapper.tx.core.TransactionAttribute

@KomapperEntity
data class Foo(
    @KomapperId
    @KomapperAutoIncrement
    val id: Int = 0,
    val name: String,
)

@KomapperEntity
data class Bar(
    @KomapperId
    @KomapperAutoIncrement
    val id: Int = 0,
    val name: String,
)

fun main() {
    val fooDb = JdbcDatabase("jdbc:h2:mem:///foo;DB_CLOSE_DELAY=-1")
    val barDb = JdbcDatabase("jdbc:h2:mem:///bar;DB_CLOSE_DELAY=-1")

    fooDb.runQuery(QueryDsl.create(Meta.foo))
    barDb.runQuery(QueryDsl.create(Meta.bar))

    fooDb.withTransaction(TransactionAttribute.REQUIRES_NEW) {

        // OK
        val newFoo = fooDb.runQuery {
            QueryDsl.insert(Meta.foo).single(Foo(name = "hoge"))
        }

        println(newFoo)

        barDb.withTransaction(TransactionAttribute.REQUIRES_NEW) {

            // OK
            val newBar = barDb.runQuery {
                QueryDsl.insert(Meta.bar).single(Bar(name = "fuga"))
            }

            println(newBar)
        }
    }

    fooDb.withTransaction(TransactionAttribute.REQUIRED) {

        // OK
        val newFoo = fooDb.runQuery {
            QueryDsl.insert(Meta.foo).single(Foo(name = "hoge"))
        }

        println(newFoo)

        barDb.withTransaction(TransactionAttribute.REQUIRED) {

            // OK
            val newBar = barDb.runQuery {
                QueryDsl.insert(Meta.bar).single(Bar(name = "fuga"))
            }

            println(newBar)
        }
    }
}
