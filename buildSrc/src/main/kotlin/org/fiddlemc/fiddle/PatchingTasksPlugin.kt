import org.gradle.api.Plugin
import org.gradle.api.Project

class FullPatchingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val projectName = project.rootProject.name

        val applyTasks = listOf(
            "applyAllPatches"
        )
        val fixupTasks = listOf(
            "fixupPaperApiFilePatches",
            ":$projectName-server:fixupMinecraftResourcePatches",
            ":$projectName-server:fixupMinecraftSourcePatches",
            ":$projectName-server:fixupPaperServerFilePatches",
        )
        val rebuildTasks = listOf(
            "rebuildPaperPatches",
            "rebuildPaperSingleFilePatches",
            ":$projectName-server:rebuildMinecraftPatches",
            ":$projectName-server:rebuildAllServerPatches",
            ":$projectName-server:rebuildServerPatches",
        )

        val applyTask = project.tasks.register("apply") {
            group = "full patching"
            dependsOn(applyTasks)
        }
        project.tasks.register("fixup") {
            group = "full patching"
            dependsOn(fixupTasks)
        }
        project.tasks.register("rebuild") {
            group = "full patching"
            dependsOn(rebuildTasks)
            mustRunAfter(fixupTasks)
        }
        project.tasks.register("fixupAndRebuild") {
            group = "full patching"
            dependsOn(rebuildTasks)
        }
    }
}
