import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import java.awt.Color
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.math.min
import kotlin.system.measureNanoTime

class KSearch : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = KSearch().subcommands(Compute(), LoadFile()).main(args)

class Compute : CliktCommand(name = "path") {
    private val nSquares by option(
        "--nSquares", "-N",
        help = "Number of squares of the grid"
    )
        .int().default(30)
    private val source by option(
        "--source", "-s",
        help = "The starting point"
    )
        .int().pair().default(Pair(13, 3))
    private val target by option(
        "--target", "-t",
        help = "The target point"
    )
        .int().pair().default(Pair(21, 22))
    private val algorithm by option(
        "--algorithm", "-a",
        help = "Search algorithm"
    ).choice("dijkstra", "A*", "both").default("both")
    private val visualization by option(
        "--visualization", "-v",
        help = "If true displays visited nodes along the search"
    ).flag(default = false)
    private val heuristic by option(
        "--heuristic", "-h",
        help = "The heuristic function to use for A* algorithm"
    ).choice("trivial", "manhattan", "euclidean", "diagonal", ignoreCase = true)
        .default("trivial")
    private val blocksFile by option(
        "--blockfile", "-b",
        help = "File with coordinates of blocks"
    )
    private val randomBlocks by option(
        "--randomblocks", "-r",
        help = "If true grid has random blocks in it"
    ).flag(default = false)

    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    override fun run() {
        val blocks: MutableList<Pair<Int, Int>>
        if (blocksFile == null) {
            if (!randomBlocks) {
                blocks = MutableList(16) { Pair(it + 5, 21) }
                for (i in listOf(5, 20))
                    for (j in 5..20)
                        blocks.add(Pair(i, j))
            } else blocks = mutableListOf()
        } else blocks = loadBlocks(blocksFile.toString())

        val h = when (heuristic) {
            "trivial" -> Heuristic2D.trivial()
            "manhattan" -> Heuristic2D.manhattan(nSquares)
            "euclidean" -> Heuristic2D.euclidean(nSquares)
            "diagonal" -> Heuristic2D.diagonal(nSquares)
            else -> throw RuntimeException("Should never get here the code")
        }

        val grid = Grid2D(nSquares, blocks, randomBlocks = randomBlocks)
        val target = grid.getId(target)
        val source = grid.getId(source)

        if (algorithm == "dijkstra" || algorithm == "both")
            if (visualization) {
                val p: Pair<ArrayDeque<Int>, MutableList<Int>>
                val timeD = measureNanoTime {
                    p = Dijkstra.visualTargetShortestPath(
                        grid,
                        source = source,
                        target = target
                    )
                }
                println("Total Dijkstra* time $timeD ns = ${timeD * 1E-9} s")
                println(p.first)
                GridFrame(grid, p.first, p.second, color = Color(0, 0, 128), title = "Dijkstra")
            } else {
                val p: ArrayDeque<Int>
                val timeD = measureNanoTime {
                    p = Dijkstra.targetShortestPath(
                        grid,
                        source = source,
                        target = target
                    )
                }
                println("Total Dijkstra* time $timeD ns = ${timeD * 1E-9} s")
                GridFrame(grid, p, color = Color(0, 0, 128), title = "Dijkstra")
            }

        if (algorithm == "A*" || algorithm == "both") {
            if (visualization) {
                val p: Pair<ArrayDeque<Int>, MutableList<Int>>
                val elapsed = measureNanoTime {
                    p = AStar.visualTargetShortestPath(
                        grid,
                        source = source,
                        target = target,
                        heuristic = h
                    )
                }
                println(p.first)
                println("Total A* time $elapsed ns = ${elapsed * 1E-9} s")
                GridFrame(grid, p.first, p.second, color = Color(100, 149, 237), title = "A*")
            } else {
                val p: ArrayDeque<Int>
                val elapsed = measureNanoTime {
                    p = AStar.targetShortestPath(
                        grid,
                        source = source,
                        target = target,
                        heuristic = h
                    )
                }
                println("Total A* time $elapsed ns = ${elapsed * 1E-9} s")
                GridFrame(grid, p, color = Color(100, 149, 237), title = "A*")
            }

        }
    }

    private fun loadBlocks(fileIn: String): MutableList<Pair<Int, Int>> {
        val blocks = mutableListOf<Pair<Int, Int>>()
        File(fileIn).forEachLine {
            val coor = it.split("\\s".toRegex())
            blocks.add(Pair(coor[0].toInt(), coor[1].toInt()))
        }
        return blocks
    }
}

class LoadFile : CliktCommand(name = "fileRun") {
    private val algorithm by option(
        "--algorithm", "-a",
        help = "Search algorithm"
    ).choice("dijkstra", "a*", "both").default("both")
    private val visualization by option(
        "--visualization", "-v",
        help = "If true displays visited nodes along the search"
    ).flag(default = true)
    private val heuristic by option(
        "--heuristic", "-h",
        help = "The heuristic function to use for A* algorithm"
    ).choice("trivial", "manhattan", "euclidean", "diagonal", ignoreCase = true)
        .default("trivial")
    private val inputFile by option(
        "--input", "-i",
        help = "A file describing the grid. # normal squares, @ is the source, T is the target and X are the blocks"
    ).required()

    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    override fun run() {
        val file = readInput(inputFile)
        val grid = file.first
        val source = file.second
        val target = file.third
        val nSquares = grid.nSquares
        val h = when (heuristic) {
            "trivial" -> Heuristic2D.trivial()
            "manhattan" -> Heuristic2D.manhattan(nSquares)
            "euclidean" -> Heuristic2D.euclidean(nSquares)
            "diagonal" -> Heuristic2D.diagonal(nSquares)
            else -> throw RuntimeException("Should never get here the code")
        }




        if (algorithm == "dijkstra" || algorithm == "both")
            if (visualization) {
                val p: Pair<ArrayDeque<Int>, MutableList<Int>>
                val timeD = measureNanoTime {
                    p = Dijkstra.visualTargetShortestPath(
                        grid,
                        source = source,
                        target = target
                    )
                }
                println("Total Dijkstra* time $timeD ns = ${timeD * 1E-9} s")
                GridFrame(grid, p.first, p.second, color = Color(0, 0, 128), title = "Dijkstra")
            } else {
                val p: ArrayDeque<Int>
                val timeD = measureNanoTime {
                    p = Dijkstra.targetShortestPath(
                        grid,
                        source = source,
                        target = target
                    )
                }
                println("Total Dijkstra* time $timeD ns = ${timeD * 1E-9} s")
                GridFrame(grid, p, color = Color(0, 0, 128), title = "Dijkstra")
            }

        if (algorithm == "A*" || algorithm == "both") {
            if (visualization) {
                val p: Pair<ArrayDeque<Int>, MutableList<Int>>
                val elapsed = measureNanoTime {
                    p = AStar.visualTargetShortestPath(
                        grid,
                        source = source,
                        target = target,
                        heuristic = h
                    )
                }
                println("Total A* time $elapsed ns = ${elapsed * 1E-9} s")
                GridFrame(grid, p.first, p.second, color = Color(100, 149, 237), title = "A*")
            } else {
                val p: ArrayDeque<Int>
                val elapsed = measureNanoTime {
                    p = AStar.targetShortestPath(
                        grid,
                        source = source,
                        target = target,
                        heuristic = h
                    )
                }
                println("Total A* time $elapsed ns = ${elapsed * 1E-9} s")
                GridFrame(grid, p, color = Color(100, 149, 237), title = "A*")
            }

        }
    }

    private fun readInput(fileIn: String) : Triple<Grid2D, Int, Int> {
        var source : Pair<Int, Int> = Pair(0, 0)
        var target : Pair<Int, Int> = Pair(0, 0)
        val blocks = mutableListOf<Pair<Int, Int>>()

        var rows  = 0
        try {
            BufferedReader(FileReader(fileIn)).use { reader -> while (reader.readLine() != null) rows++ }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var width = rows
        var nRow = 0
        File(fileIn).forEachLine {
            val cols = it.count()
            if (cols!=rows) width = min(rows, cols)
            var nCol = 0
            for (ch in it) {
                when(ch) {
                    '@' -> source = Pair(nRow, nCol)
                    'T' -> target = Pair(nRow, nCol)
                    'X' -> blocks.add(Pair(nRow, nCol))
                }
                nCol++
            }
            nRow++
        }
        val g = Grid2D(width, blocks, randomBlocks = false)
        return Triple(g, g.getId(source), g.getId(target))
    }
}






