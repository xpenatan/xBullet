val moduleName = "bullet-desktop"

val windowsFile = "$projectDir/../generator/build/c++/libs/windows/bullet64.dll"

tasks.jar {
    from(windowsFile)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}