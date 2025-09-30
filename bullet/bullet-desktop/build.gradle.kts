plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}

val moduleName = "bullet-desktop"

val libDir = "${rootProject.projectDir}/bullet"

val windowsFile = "$libDir/bullet-build/build/c++/libs/windows/vc/bullet64.dll"
val linuxFile = "$libDir/bullet-build/build/c++/libs/linux/libbullet64.so"
val macArmFile = "$libDir/bullet-build/build/c++/libs/mac/arm/libbulletarm64.dylib"
val macFile = "$libDir/bullet-build/build/c++/libs/mac/libbullet64.dylib"

tasks.jar {
    from(windowsFile)
    from(linuxFile)
    from(macArmFile)
    from(macFile)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            group = LibExt.groupId
            version = LibExt.libVersion
            from(components["java"])
        }
    }
}