plugins {
    idea
    id("org.springframework.boot") version "2.4.5"
    id("com.google.devtools.ksp") version "1.5.0-1.0.0-alpha09"
    kotlin("plugin.allopen") version "1.5.0"
}

apply(plugin = "io.spring.dependency-management")

val komapperVersion: String by project

sourceSets {
    main {
        java {
            srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

idea.module {
    generatedSourceDirs.add(file("build/generated/ksp/main/kotlin"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-ext-spring-boot-starter:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("org.springframework.context.annotation.Configuration")
    annotation("org.springframework.transaction.annotation.Transactional")
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
