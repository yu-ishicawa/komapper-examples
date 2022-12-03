plugins {
    java
    kotlin("jvm")
    id("com.diffplug.spotless")version "6.12.0"
}

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot") || it.name == "jpetstore"
}

val ktlintVersion: String by project

allprojects {
    apply(plugin = "base")
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            ktlint(ktlintVersion)
            targetExclude("build/**")
            if (project.name == "codegen") {
                targetExclude("src/**")
            }
        }
        kotlinGradle {
            ktlint(ktlintVersion)
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    tasks {
        build {
            dependsOn(spotlessApply)
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation(kotlin("test"))
        if (project !in springBootProjects) {
            testImplementation(kotlin("test"))
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
        }
    }

    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions.jvmTarget = "11"
        }
    }
}
