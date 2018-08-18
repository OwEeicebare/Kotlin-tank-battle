group ="Tankdz"
version ="1.0-SNAPSHOT"
plugins {
    kotlin("jvm")
}
repositories {
    mavenCentral()
    maven { setUrl ("https://jitpack.io")  }
}
dependencies {
    compile(kotlin("stdlib"))
    compile ("com.github.shaunxiao:kotlinGameEngine:v0.0.4")
}