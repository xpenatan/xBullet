import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("de.undercouch.download") version("5.4.0")
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

val mainClassName = "Build"

dependencies {
    implementation(project(":bullet:bullet-base"))
    implementation("com.github.xpenatan.jParser:jParser-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build-tool:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-idl:${LibExt.jParserVersion}")
}

val buildDir = layout.buildDirectory.get().asFile
val zippedPath = "$buildDir/bullet-source.zip"
val sourcePath = "$buildDir/bullet-source"
val unzippedFolder = "$sourcePath/bullet3-3.25"
val sourceDestination = "$buildDir/bullet/"

tasks.register<Download>("bullet_download_source") {
    group = "bullet"
    description = "Download bullet source"
    src("https://github.com/bulletphysics/bullet3/archive/refs/tags/3.25.zip")
    dest(File(zippedPath))
    doLast {
        unzipTo(File(sourcePath), dest)
        copy{
            from(unzippedFolder)
            into(sourceDestination)
        }
        delete(sourcePath)
        delete(zippedPath)
    }
}

tasks.register<JavaExec>("bullet_build_project") {
    dependsOn("classes")
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_teavm") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("teavm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_windows64") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("windows64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_linux64") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("linux64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_mac64") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("mac64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_macArm") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("macArm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_android") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("android")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("bullet_build_project_ios") {
    group = "bullet"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("ios")
    classpath = sourceSets["main"].runtimeClasspath
}