import org.junit.Assert.*
import org.junit.Test

class DijkstraTest {
    @Test
    fun shortestPath() {
        val V = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        val graph = Graph(V = V, directed = false)

        graph.addEdge(0, 1, 4f)
        graph.addEdge(0, 7, 8f)
        graph.addEdge(1, 2, 8f)
        graph.addEdge(1, 7, 11f)
        graph.addEdge(2, 3, 7f)
        graph.addEdge(2, 8, 2f)
        graph.addEdge(2, 5, 4f)
        graph.addEdge(3, 4, 9f)
        graph.addEdge(3, 5, 14f)
        graph.addEdge(4, 5, 10f)
        graph.addEdge(5, 6, 2f)
        graph.addEdge(6, 7, 1f)
        graph.addEdge(6, 8, 6f)
        graph.addEdge(7, 8, 7f)

        val path = Dijkstra.shortestPath(graph, source=0)
        assertTrue(path[0] == 0f)
        assertTrue(path[1] == 4f)
        assertTrue(path[2] == 12f)
        assertTrue(path[3] == 19f)
        assertTrue(path[4] == 21f)
        assertTrue(path[5] == 11f)
        assertTrue(path[6] == 9f)
        assertTrue(path[7] == 8f)
        assertTrue(path[8] == 14f)
    }

    @Test
    fun targetShortestPath() {
        val V = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        val graph = Graph(V = V, directed = false)

        graph.addEdge(0, 1, 4f)
        graph.addEdge(0, 7, 8f)
        graph.addEdge(1, 2, 8f)
        graph.addEdge(1, 7, 11f)
        graph.addEdge(2, 3, 7f)
        graph.addEdge(2, 8, 2f)
        graph.addEdge(2, 5, 4f)
        graph.addEdge(3, 4, 9f)
        graph.addEdge(3, 5, 14f)
        graph.addEdge(4, 5, 10f)
        graph.addEdge(5, 6, 2f)
        graph.addEdge(6, 7, 1f)
        graph.addEdge(6, 8, 6f)
        graph.addEdge(7, 8, 7f)

        val path = Dijkstra.targetShortestPath(graph, source=0, target = 8)
        assertTrue(path.pop() == 0)
        assertTrue(path.pop() == 1)
        assertTrue(path.pop() == 2)
        assertTrue(path.pop() == 8)

    }
}