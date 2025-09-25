import java.io.File

plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}

dependencies {
    implementation(project(":examples:basic:core"))
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:${LibExt.gdxVersion}")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xBullet:bullet-desktop:-SNAPSHOT")
    }
    else {
        implementation(project(":bullet:bullet-desktop"))
    }
}

val mainClassName = "bullet.examples.basic.Main"
val assetsDir = File("../assets");

tasks.register<JavaExec>("bullet_basic_run_desktop") {
    dependsOn("classes")
    group = "bullet_examples_desktop"
    description = "Run bullet example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = assetsDir

    if (org.gradle.internal.os.OperatingSystem.current() == org.gradle.internal.os.OperatingSystem.MAC_OS) {
        // Required to run on macOS
        jvmArgs?.add("-XstartOnFirstThread")
    }
}
//
//tasks.register('dist', Jar) {
//    manifest {
//        attributes 'Main-Class': project.mainClassName
//    }
//    dependsOn configurations.runtimeClasspath
//    from {
//        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
//    }
//    with jar
//}