import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs
import kotlin.math.sqrt

class AStarTest {

    @Test
    fun targetShortestPath() {
        //Create a simple 2D square graph, with movements allowed only along 4 directions
        val V = (0..15).toMutableList()
        val graph = Graph(V = V, directed = false)
        for (i in V) {
            if (i % 4 != 3) {
                graph.addEdge(i, i + 1, 1f)
            }
            if (i / 4 != 3) {
                graph.addEdge(i, i + 4, 1f)
            }
        }

        val h = Heuristic2D.manhattan(4)

        val path = AStar.targetShortestPath(graph, source = 0, target = 9, h)

        assertTrue(path.removeFirst() == 0)
        assertTrue(path.removeFirst() == 1)
        assertTrue(path.removeFirst() == 5)
        assertTrue(path.removeFirst() == 9)

    }
}