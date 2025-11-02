plugins {
  id("java")
  id("eclipse")
  id("application")
    id("com.gradleup.shadow") version "9.2.2"
}

sourceSets {
  named("main") {
    java.setSrcDirs(listOf("./src/main/java"))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(fileTree("./gradle/jars") { include("*.jar") })
}

val MAIN = "net.betaProxy.main.Main"

application {
  mainClass.set(MAIN)
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  manifest {
    attributes["Main-Class"] = MAIN
  }

  from(
    configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
  )
}

tasks.named<JavaExec>("run") {
  group = "betaProxy"
  description = "Runs the Server"

  doFirst { file("run").mkdirs() }
  workingDir = file("run")
  standardInput = System.`in`

  mainClass.set(MAIN)
  classpath = sourceSets.named("main").get().runtimeClasspath

  jvmArgs("-Djava.library.path=" + System.getProperty("user.dir"))
  if (System.getProperty("os.name").lowercase().contains("mac")) jvmArgs("-XstartOnFirstThread")
}
