plugins {
    id("net.freudasoft.gradle-cmake-plugin") version("0.0.2")
}

val mainClassName = "Main"

dependencies {
    implementation(project(":bullet:base"))
    implementation("com.github.xpenatan.jParser:jParser-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-idl:${LibExt.jParserVersion}")
}

tasks.register<JavaExec>("generate_project_classes") {
    dependsOn("classes")
    group = "bullet"
    description = "Generate and build native project"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/jni/build/"
        project.delete(files(srcPath))
    }
}

cmake {
    generator.set("MinGW Makefiles")

    sourceFolder.set(file("$projectDir/src/main/cpp"))

    buildConfig.set("Release")
    buildTarget.set("install")
    buildClean.set(true)
}

tasks.register("build_emscripten") {
    dependsOn("cmakeBuild")
    mustRunAfter("cmakeBuild")
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