import org.junit.Test

import org.junit.Assert.*

class Grid2DTest {

    @Test
    fun Grid2DTest() {
        val blocks = mutableListOf(Pair(0, 0), Pair(2, 1))
        val grid = Grid2D(4, blocks)
        assertTrue(grid.graph.neighbours[0]?.isEmpty() ?: false)
        assertTrue(
            grid.graph.neighbours[5] ==
                    mutableListOf(
                        Pair(1, 1.0f),
                        Pair(4, 1.0f),
                        Pair(6, 1.0f)
                    )
        )
    }

    @Test
    fun addBlock() {
        val blocks = mutableListOf(Pair(0, 0), Pair(2, 1))
        val grid = Grid2D(4, blocks)
        val id = grid.getId(2, 3)
        assertFalse(grid.graph.neighbours[id]?.isEmpty() ?: true)
        grid.addBlock(2, 3)
        assertTrue(grid.graph.neighbours[id]?.isEmpty() ?: false)
    }
}