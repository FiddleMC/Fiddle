import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

if (!file(".git").exists()) {
    // Fiddle start - build changes
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Fiddle project directory is not a properly cloned Git repository.
         
         In order to build Fiddle from source you must clone
         the Fiddle repository using Git, not download a code
         zip from GitHub.
         
         Built Fiddle jars are available for download at
         https://github.com/FiddleMC/Fiddle/actions
         
         See https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // Fiddle end - build changes
    error(errorText)
}

rootProject.name = "fiddle" // Fiddle - build changes

for (name in listOf("fiddle-api", "fiddle-server")) { // Fiddle - build changes
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}