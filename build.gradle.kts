import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktor) apply false

    `maven-publish`
}

subprojects {
    group = "${extra["ktor.cloud.group"]}"
    version = "${extra["ktor.cloud.version"]}"

    tasks.withType<KotlinCompile> {

        kotlinOptions {
            freeCompilerArgs += "-Xcontext-receivers"
        }
    }

    this.afterEvaluate {
        extensions.findByType<PublishingExtension>()?.let {
            extensions.configure<PublishingExtension> {
                publications {
                    components.findByName("kotlin")?.let {
                        create<MavenPublication>("maven") {
                            from(it)
                        }
                    }
                }
                val local = Properties().apply {
                    load(FileInputStream(File(rootDir, "local.properties")))
                }
                repositories {
                    maven {
                        val codingArtifactsRepoUrl = "${local["codingArtifactsRepoUrl"]}"
                        val codingArtifactsGradleUsername = "${local["codingArtifactsGradleUsername"]}"
                        val codingArtifactsGradlePassword = "${local["codingArtifactsGradlePassword"]}"
                        url = uri(codingArtifactsRepoUrl)
                        credentials {
                            username = codingArtifactsGradleUsername
                            password = codingArtifactsGradlePassword
                        }
                    }
                }
            }
        }
    }
}