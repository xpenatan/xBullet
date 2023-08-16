import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("de.undercouch.download") version("5.4.0")
}

val mainClassName = "Main"

dependencies {
    implementation(project(":bullet:base"))
    implementation("com.github.xpenatan.jParser:core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:builder:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl:${LibExt.jParserVersion}")
}

tasks.register<JavaExec>("build_project") {
    dependsOn("classes")
    group = "bullet"
    description = "Generate and build native project"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

val zippedPath = "$buildDir/bullet-source.zip"
val sourcePath = "$buildDir/bullet-source"
val unzippedFolder = "$sourcePath/bullet3-3.25"
val sourceDestination = "$buildDir/bullet/bullet/"

tasks.register<Download>("download_source") {
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