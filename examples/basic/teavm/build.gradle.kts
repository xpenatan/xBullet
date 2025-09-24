plugins {
    id("org.gretty") version("3.1.0")
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

gretty {
    contextPath = "/"
    extraResourceBase("build/dist/webapp")
}

val mainClassName = "bullet.examples.basic.Build"

dependencies {
    implementation(project(":examples:basic:core"))
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}")
    implementation(project(":bullet:bullet-teavm"))
}

tasks.register<JavaExec>("bullet_basic_build_teavm") {
    dependsOn("classes")
    group = "bullet_examples_teavm"
    description = "Build Bullet example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("bullet_basic_run_teavm") {
    group = "bullet_examples_teavm"
    description = "Run Bullet example"
    val list = listOf("bullet_basic_build_teavm", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("bullet_basic_build_teavm")
}