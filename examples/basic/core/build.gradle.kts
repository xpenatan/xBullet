plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xBullet:gdx-gl:-SNAPSHOT")
        implementation("com.github.xpenatan.xBullet:bullet-core:-SNAPSHOT")
    }
    else {
        implementation(project(":extensions:gdx:gdx-gl"))
        implementation(project(":bullet:bullet-core"))
    }
}
