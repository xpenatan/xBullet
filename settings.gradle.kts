include(":bullet:bullet-core")
include(":bullet:bullet-base")
include(":bullet:bullet-build")
include(":bullet:bullet-teavm")
include(":bullet:bullet-desktop")
include(":bullet:bullet-android")

// Extension
include(":extensions:gdx:gdx-utils")
include(":extensions:gdx:gdx-gl")

include(":examples:basic:core")
include(":examples:basic:desktop")
include(":examples:basic:teavm")
include(":examples:basic:android")

//includeBuild("E:\\Dev\\Projects\\java\\gdx-teavm") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-teavm:backend-teavm")).using(project(":backends:backend-teavm"))
//    }
//}

//includeBuild("E:\\Dev\\Projects\\java\\jParser") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.jParser:jParser-base")).using(project(":jParser:base"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build")).using(project(":jParser:builder"))
//        substitute(module("com.github.xpenatan.jParser:jParser-core")).using(project(":jParser:core"))
//        substitute(module("com.github.xpenatan.jParser:jParser-cpp")).using(project(":jParser:cpp"))
//        substitute(module("com.github.xpenatan.jParser:jParser-idl")).using(project(":jParser:idl"))
//        substitute(module("com.github.xpenatan.jParser:jParser-teavm")).using(project(":jParser:teavm"))
//        substitute(module("com.github.xpenatan.jParser:loader-core")).using(project(":jParser:loader:loader-core"))
//        substitute(module("com.github.xpenatan.jParser:loader-teavm")).using(project(":jParser:loader:loader-teavm"))
//    }
//}