buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(platform("org.testcontainers:testcontainers-bom:1.17.3"))
        classpath("org.testcontainers:mysql")
        classpath("org.testcontainers:postgresql")
        classpath("mysql:mysql-connector-java:8.0.30")
        classpath("org.postgresql:postgresql:42.4.1")
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
        generatedSourceDirs =
            generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
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
            jdbc {
                val initScript = file("src/main/resources/init_mysql.sql")
                driver.set("org.testcontainers.jdbc.ContainerDatabaseDriver")
                url.set("jdbc:tc:mysql:8.0.25:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
                user.set("test")
                password.set("test")
            }
            packageName.set("$basePackage.mysql")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
        register("postgresql") {
            jdbc {
                val initScript = file("src/main/resources/init_postgresql.sql")
                driver.set("org.testcontainers.jdbc.ContainerDatabaseDriver")
                url.set("jdbc:tc:postgresql:13.3:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
                user.set("test")
                password.set("test")
            }
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
