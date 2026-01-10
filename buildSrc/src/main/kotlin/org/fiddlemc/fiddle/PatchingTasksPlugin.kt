import org.gradle.api.Plugin
import org.gradle.api.Project

class FullPatchingPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // These tasks already exist in the project
        val applyTasks = listOf(
            "applyAllPatches"
        )
        val fixupTasks = listOf(
            "fixupMinecraftResourcePatches",
            "fixupMinecraftSourcePatches",
            "fixupPaperApiFilePatches",
            "fixupPaperServerFilePatches",
        )
        val rebuildTasks = listOf(
            "rebuildMinecraftPatches",
            "rebuildAllServerPatches",
            "rebuildPaperPatches",
            "rebuildPaperSingleFilePatches",
            "rebuildServerPatches",
        )

        val applyTask = project.tasks.register("apply") {
            group = "full patching"
            dependsOn(applyTasks)
        }

        val fixupTask = project.tasks.register("fixup") {
            group = "full patching"
            dependsOn(fixupTasks)
        }

        val rebuildTask = project.tasks.register("rebuild") {
            group = "full patching"
            dependsOn(rebuildTasks)
            mustRunAfter(fixupTasks)
        }

        val fixupAndRebuildTask = project.tasks.register("fixupAndRebuild") {
            group = "full patching"
            dependsOn(rebuildTasks)
        }
    }
}
