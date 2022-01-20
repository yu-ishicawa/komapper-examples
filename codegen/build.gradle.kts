import org.komapper.jdbc.JdbcDatabase

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(platform("org.testcontainers:testcontainers-bom:1.16.3"))
        classpath("org.testcontainers:mysql")
        classpath("org.testcontainers:postgresql")
    }
}

plugins {
    application
    idea
    id("com.google.devtools.ksp")
    id("org.komapper.gradle")
}

val komapperVersion: String by project

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin")
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    implementation("org.komapper:komapper-starter-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

komapper {
    generators {
        val basePackage = "org.komapper.example"
        register("mysql") {
            val initScript = file("src/main/resources/init_mysql.sql")
            database.set(
                JdbcDatabase.create(
                    url = "jdbc:tc:mysql:8.0.25:///test?TC_INITSCRIPT=file:${initScript.absolutePath}",
                    user = "test",
                    password = "test"
                )
            )
            packageName.set("$basePackage.mysql")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
        register("postgresql") {
            val initScript = file("src/main/resources/init_postgresql.sql")
            database.set(
                JdbcDatabase.create(
                    url = "jdbc:tc:postgresql:13.3:///test?TC_INITSCRIPT=file:${initScript.absolutePath}",
                    user = "test",
                    password = "test"
                )
            )
            packageName.set("$basePackage.postgres")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
    }
}

tasks {
    named("clean") {
        doLast {
            delete("src/main/kotlin")
        }
    }
}
