import io.papermc.paperweight.tasks.ZippedTask
import org.gradle.configurationcache.extensions.capitalized
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("io.papermc.paperweight.patcher") version "1.4.0"
    id("com.github.ManifestClasspath") version "0.1.0-RELEASE"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content { onlyForConfigurations(configurations.paperclip.name) }
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.8.6:fat")
    decompiler("net.minecraftforge:forgeflower:2.0.605.1")
    paperclip("io.papermc:paperclip:3.0.2")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}

paperweight {
    serverProject.set(project(":fiddle-server")) // Fiddle - build changes

    remapRepo.set(paperMavenPublicUrl)
    decompileRepo.set(paperMavenPublicUrl)

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            apiOutputDir.set(layout.projectDirectory.dir("fiddle-api")) // Fiddle - build changes

            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            serverOutputDir.set(layout.projectDirectory.dir("fiddle-server")) // Fiddle - build changes
        }
    }
}

// Fiddle start - extend jar manifest
enum class RunnableJarKind(val text: String) {
    BUNDLER("bundler"),
    PAPERCLIP("paperclip")
}

enum class RunnableJarMappings(val text: String) {
    MOJMAP("mojmap"),
    REOBF("reobf")
}

@CacheableTask
abstract class RejarWithExtendedManifestTask : ZippedTask() {

    private lateinit var manifestElements: List<Pair<String, String>>

    fun configure(
        kind: RunnableJarKind,
        classifier: RunnableJarMappings,
        manifestElements: List<Pair<String, String>>
    ) {

        this.manifestElements = manifestElements

        // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
        fun jarName(kind: RunnableJarKind, classifier: RunnableJarMappings, withExtendedManifest: Boolean) =
            listOfNotNull(
                project.name,
                kind.text,
                project.version,
                classifier.text,
//                "WithExtendedManifest".takeIf { withExtendedManifest } // Commented out, to replace the original .jar
            ).joinToString("-") + ".jar"

        // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
        fun jarFileProvider(kind: RunnableJarKind, classifier: RunnableJarMappings, withExtendedManifest: Boolean) =
            layout.buildDirectory.file("libs/${jarName(kind, classifier, withExtendedManifest)}")

        inputZip.set(jarFileProvider(kind, classifier, false))
        outputZip.set(jarFileProvider(kind, classifier, true))

    }

    override fun run(rootDir: Path) {
        val manifestFile = rootDir.resolve("META-INF/MANIFEST.MF")
        manifestFile.writeLines(ArrayList(manifestFile.readLines()).apply {
            addAll(
                indexOfFirst { it.startsWith("Main-Class:") } + 1,
                manifestElements.map { "${it.first}: ${it.second}" }
            )
        })

    }

}

val extendedManifestElements: List<Pair<String, String>> = listOf(
    "Add-Opens" to "java.base/java.lang" // Fiddle - remove hard-coded Bukkit values - modify classes - add module opens to server jar
)

if (extendedManifestElements.isNotEmpty()) {
    for (kind in RunnableJarKind.values()) {
        for (classifier in RunnableJarMappings.values()) {

            // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
            fun createOriginalTaskName(kind: RunnableJarKind) =
                "create${classifier.text.capitalized()}${kind.text.capitalized()}Jar"

            fun createRejarTaskName(originalTaskName: String) =
                "${originalTaskName}WithExtendedManifest"

            val originalTaskName = createOriginalTaskName(kind)

            when (kind) {
                RunnableJarKind.BUNDLER -> {
                    val rejarTaskName = createRejarTaskName(originalTaskName)
                    tasks.register<RejarWithExtendedManifestTask>(rejarTaskName) {

                        group = "paperweight"
                        description = "Build a runnable $kind jar with $classifier mappings and an extended manifest"

                        dependsOn(originalTaskName)

                        configure(kind, classifier, extendedManifestElements)

                    }
                    tasks.named(originalTaskName) {
                        finalizedBy(rejarTaskName)
                    }
                }

                RunnableJarKind.PAPERCLIP -> {
                    val bundlerRejarTaskName = createRejarTaskName(createOriginalTaskName(RunnableJarKind.BUNDLER))
                    tasks.named(originalTaskName) {
                        dependsOn(bundlerRejarTaskName)
                    }
                }
            }

        }
    }
}
// Fiddle end - extend jar manifest
