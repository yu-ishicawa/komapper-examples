plugins {
    idea
    id("org.springframework.boot") version "2.5.3"
    id("com.google.devtools.ksp")
    kotlin("plugin.allopen")
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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-spring-boot-starter-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-r2dbc:$komapperVersion")
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
