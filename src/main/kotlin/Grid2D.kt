import kotlin.random.Random

class Grid2D(
    val nSquares: Int,
    val blocks: MutableList<Pair<Int, Int>> = mutableListOf(),
    val randomBlocks : Boolean = false
) {
    private val V = (0 until nSquares * nSquares).toMutableList()
    val graph = Graph(V, directed = false)
    val freeV : MutableList<Pair<Int, Int>> = mutableListOf()
    private val rand = Random.Default
    init {

        for (i in 0 until nSquares * nSquares) {
            val row = i / nSquares
            val col = i % nSquares
            if (randomBlocks) {
                if (rand.nextFloat() < 0.1) addBlock(row, col)
            }
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

    fun addBlock(row: Int, col: Int) {
        blocks.add(Pair(row, col))
        val id = nSquares * row + col
        graph.removeVertex(id)
        freeV.remove(Pair(row, col))
    }

    fun getId(row: Int, col : Int) : Int {return nSquares * row + col}
    fun getCoordinates(id : Int) : Pair<Int, Int> {
        val row = id / nSquares
        val col = id % nSquares
        return Pair(row, col)
    }
}