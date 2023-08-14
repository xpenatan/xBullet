import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("net.freudasoft.gradle-cmake-plugin") version("0.0.2")
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

cmake {
    generator.set("MinGW Makefiles")

    sourceFolder.set(file("$buildDir/bullet/"))

    buildConfig.set("Release")
    buildTarget.set("install")
    buildClean.set(true)
}

tasks.register("copy_emscripten_files") {
    copy {
        from("$projectDir/src/main/cpp/idl")
        into("$sourceDestination/../")
    }
}

tasks.register("build_emscripten") {
    dependsOn("copy_emscripten_files", "cmakeBuild")
    tasks.findByName("cmakeBuild")?.mustRunAfter("copy_emscripten_files")

    group = "bullet"
    description = "Generate javascript"

    doLast {
        copy{
            from(
                "$buildDir/cmake/bullet.js",
                "$buildDir/cmake/bullet.wasm.js"
            )
            into("$projectDir/../teavm/src/main/resources")
        }
    }
}

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