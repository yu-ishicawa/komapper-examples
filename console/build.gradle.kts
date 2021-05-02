plugins {
    application
    idea
    id("com.google.devtools.ksp") version "1.5.0-1.0.0-alpha09"
}

val komapperVersion: String by project

idea.module {
    generatedSourceDirs.add(file("build/generated/ksp/main/kotlin"))
}

sourceSets {
    main {
        java {
            srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

dependencies {
    implementation("org.komapper:komapper-starter:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
