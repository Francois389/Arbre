import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import java.io.File

data class TreeStats(var directories: Int = 0, var files: Int = 0)

class TreeCommand : CliktCommand(
    name = "tree",
    help = "Affiche l'arborescence d'un répertoire"
) {
    private val path by argument(
        name = "PATH",
        help = "Chemin du répertoire à afficher"
    ).default(".")

    private val maxDepth by option(
        "-d", "--depth",
        help = "Profondeur maximum d'affichage"
    ).int()

    private val showHidden by option(
        "-a", "--all",
        help = "Afficher les fichiers cachés"
    ).flag()

    private val dirsOnly by option(
        "-D", "--dirs-only",
        help = "Afficher seulement les répertoires"
    ).flag()

    private val showStats by option(
        "-s", "--stats",
        help = "Afficher les statistiques"
    ).flag()

    override fun run() {
        val rootDir = File(path)

        if (!rootDir.exists()) {
            echo("Erreur: Le chemin '$path' n'existe pas", err = true)
            return
        }

        if (!rootDir.isDirectory) {
            echo("Erreur: '$path' n'est pas un répertoire", err = true)
            return
        }

        val stats = TreeStats()
        echo("📂 ${rootDir.absolutePath}")
        displayTree(rootDir, "", maxDepth ?: Int.MAX_VALUE, 0, stats)

        if (showStats) {
            echo("\n📊 Statistiques:")
            echo("   Répertoires: ${stats.directories}")
            echo("   Fichiers: ${stats.files}")
            echo("   Total: ${stats.directories + stats.files}")
        }
    }

    private fun displayTree(
        dir: File,
        prefix: String,
        maxDepth: Int,
        currentDepth: Int,
        stats: TreeStats
    ) {
        if (currentDepth >= maxDepth) return

        val files = dir.listFiles()?.let { files ->
            files.filter { file ->
                when {
                    !showHidden && file.name.startsWith(".") -> false
                    dirsOnly && !file.isDirectory -> false
                    else -> true
                }
            }.sortedWith(compareBy<File> { !it.isDirectory }.thenBy { it.name.lowercase() })
        } ?: return

        files.forEachIndexed { index, file ->
            val isLast = index == files.lastIndex
            val connector = if (isLast) "└── " else "├── "
            val fileIcon = when {
                file.isDirectory -> "📁"
                file.extension in listOf("kt", "java") -> "☕"
                file.extension in listOf("js", "ts", "vue") -> "🟨"
                file.extension in listOf("md", "txt") -> "📝"
                file.extension in listOf("json", "yml", "yaml") -> "⚙️"
                else -> "📄"
            }

            echo("$prefix$connector $fileIcon ${file.name}")

            if (file.isDirectory) {
                stats.directories++
                val nextPrefix = prefix + if (isLast) "    " else "│   "
                displayTree(file, nextPrefix, maxDepth, currentDepth + 1, stats)
            } else {
                stats.files++
            }
        }
    }
}

fun main(args: Array<String>) = TreeCommand().main(args)