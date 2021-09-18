import kotlin.random.Random

/**
 * 2D grid container as a graph
 *
 * This class builds a graph from a 2D grid of squares, where each square is a node, and the length of each edge
 * is 1.0.
 * 2 adjacent squares are considered also adjacent nodes.
 * Since [Graph] works with node IDs, also [Grid2D] does, but provides to the user a simplified interface,
 * with rows and cols indices, as one would expect on a grid.
 *
 * IDs are assigned from left to right, top to bottom increasingly.
 * Same applies for rows and cols. (0, 0) is the top-left square, ([nSquares]-1, [nSquares]-1) is the bottom-right one.
 *
 * Some nodes may be considered as blocks, meaning there cannot be any edge with them. This is meant to apply
 * some difficulties in the finding of the shortest path.
 *
 * It is also possible to create a grid where blocks are randomly positioned, with the [Boolean] flag [randomBlocks].
 * If true, each node has 10% of probability of becoming a block. This is compatible also with giving deterministic
 * blocks.
 *
 * Class properties are:
 * - [nSquares]: Number of squares per side in the grid. (Sets the dimension of the grid)
 * - [blocks]: List of the position
 * - [randomBlocks]: Boolean for creating a random grid.
 *
 * - [graph]: The [Graph] representing the grid
 * - [freeV]: The nodes that are not blocks
 *
 * @see Graph
 */

class Grid2D(
    val nSquares: Int,
    val blocks: MutableList<Pair<Int, Int>> = mutableListOf(),
    val randomBlocks: Boolean = false
) {

    private val V = (0 until nSquares * nSquares).toMutableList()
    val graph = Graph(V, directed = false)
    val freeV: MutableList<Pair<Int, Int>> = mutableListOf()
    private val rand = Random.Default

    /**
     * Initialization of graph. Edges between nodes are built, considering if there are blocks or not.
     * If [randomBlocks] is true a random generators decides whether each square may become a block
     */
    init {
        if (randomBlocks) {
            for (i in 0 until nSquares * nSquares) {
                if (rand.nextFloat() < 0.1) {
                    val row = i / nSquares
                    val col = i % nSquares
                    addBlock(row, col)
                }
            }
        }

        for (i in 0 until nSquares * nSquares) {
            val row = i / nSquares
            val col = i % nSquares
            //If block, no neighbour
            if (Pair(row, col) in blocks) continue
            if (row != nSquares - 1) {
                //If not in last (bottom) row --> add the neighbour under it
                //if not a block
                if (Pair(row + 1, col) !in blocks) graph.addEdge(i, i + nSquares, 1f)
            }
            if (col != nSquares - 1) {
                //If not in last (right) col --> add the neighbour to the right
                //if not a block
                if (Pair(row, col + 1) !in blocks) graph.addEdge(i, i + 1, 1f)
            }
            freeV.add(Pair(row, col))
        }
    }

    /**
     * Adds a block in position ([row], [col]). Each edge with this node as an end is removed, the node is
     * added to [blocks] and it is removed from [freeV]
     */
    fun addBlock(row: Int, col: Int) {
        blocks.add(Pair(row, col))
        val id = nSquares * row + col
        graph.removeVertex(id)
        freeV.remove(Pair(row, col))
    }

    /**
     * Returns the ID value of the square, given the coordinates as (row, col)
     */
    fun getId(row: Int, col: Int): Int {
        return nSquares * row + col
    }

    /**
     * Returns the ID value of the square, given the coordinates as Pair(row, col)
     */
    fun getId(coor: Pair<Int, Int>): Int {
        return nSquares * coor.first + coor.second
    }

    /**
     * Returns the coordinates value as a [Pair] = (row, col), given the ID value of the square.
     */
    fun getCoordinates(id: Int): Pair<Int, Int> {
        val row = id / nSquares
        val col = id % nSquares
        return Pair(row, col)
    }
}