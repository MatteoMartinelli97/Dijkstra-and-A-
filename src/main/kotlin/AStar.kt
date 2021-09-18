import java.util.*
import kotlin.collections.ArrayDeque

object AStar {
    fun targetShortestPath (g: Graph<Int>, source: Int, target : Int, heuristic: (Int, Int) -> Float) : ArrayDeque<Int> {
        if (source !in g.V) {
            throw RuntimeException("Source ($source) is not in graph vertices, cannot compute path!")
        }
        if (target !in g.V) {
            throw RuntimeException("Target ($target) is not in graph vertices, cannot compute path!")
        }
        if (target == source) return ArrayDeque(target)

        val prev = mutableMapOf<Int, Int>()
        val dist = mutableMapOf<Int, Float>()
        dist[source] = 0f

        val open = PriorityQueue<Pair<Int, Float>>(
            g.V.size,
            compareBy { it.second }
        )
        open.add(Pair(source, heuristic(source, target)))

        while (!open.isEmpty()) {
            //Choose min distance vertex and remove from open list
            val u = open.poll()
            if (u.first == target) {break}
            //For each neighbour of u
            // evaluate distance from source with intermediate step 'u'
            g.neighbours[u.first]?.forEach {
                val newDist = u.second + it.second
                //If lesser than before (or first path for that vertex), and update distance
                // and remember previous vertex (u)
                if (it.first !in dist.keys || newDist < dist[it.first]!!) {
                    prev[it.first] = u.first
                    dist[it.first] = newDist
//                    open.remove(it)     //If in open queue -> remove and add again with new distance
                    open.add(Pair(it.first, newDist + heuristic(it.first, target)))
                }
            }
        }
        return buildPath(source, target, prev)
    }

    fun targetShortestPath (g: Grid2D, source: Int, target : Int, heuristic: (Int, Int) -> Float) : ArrayDeque<Int> {
        if (g.getCoordinates(source) in g.blocks) throw java.lang.RuntimeException("Given source is a block." +
                " Cannot start finding shortest path")
        if (g.getCoordinates(target) in g.blocks) println("Target is a block. No path will exist")
        return targetShortestPath(g.graph, source, target, heuristic)
    }

    /**
     * Rebuilding path from source to target, given the Map of previous vertex along the desired path
     */

    private fun buildPath (source : Int, target : Int, prev : MutableMap<Int, Int>) : ArrayDeque<Int> {
        val path = ArrayDeque<Int>()
        var u = target
        path.add(u)
        while (prev[u] != null && prev[u] != source) {
            path.addFirst(prev[u]!!)
            u = prev[u]!!
        }
        path.addFirst(source)
        return path
    }


    fun visualTargetShortestPath (g: Graph<Int>, source: Int, target : Int, heuristic: (Int, Int) -> Float) : Pair<ArrayDeque<Int>, MutableList<Int>> {
        if (source !in g.V) {
            throw RuntimeException("Source ($source) is not in graph vertices, cannot compute path!")
        }
        if (target !in g.V) {
            throw RuntimeException("Target ($target) is not in graph vertices, cannot compute path!")
        }
        val visitedNodes = mutableListOf(source)
        if (target == source) return Pair(ArrayDeque(target), visitedNodes)


        val prev = mutableMapOf<Int, Int>()
        val dist = mutableMapOf<Int, Float>() //distance from source
        dist[source] = 0f

        val open = PriorityQueue<Pair<Int, Float>>(
            g.V.size,
            compareBy { it.second }
        )
        open.add(Pair(source, heuristic(source, target)))

        while (!open.isEmpty()) {
            //Choose min distance vertex and remove from open list
            val u = open.poll()
            visitedNodes.add(u.first)
            if (u.first == target) {break}
            //For each neighbour of u
            // evaluate distance from source with intermediate step 'u'
            g.neighbours[u.first]?.forEach {
                val newDist = dist[u.first]!! + it.second
                //If lesser than before (or first path for that vertex), and update distance
                // and remember previous vertex (u)
                if (it.first !in dist.keys || newDist < dist[it.first]!!) {
                    prev[it.first] = u.first
                    dist[it.first] = newDist
//                    open.remove(it)     //If in open queue -> remove and add again with new distance
                    open.add(Pair(it.first, newDist + heuristic(it.first, target)))
                }
            }
        }
        println(open)
        return Pair(buildPath(source, target, prev), visitedNodes)
    }

    fun visualTargetShortestPath (g: Grid2D, source: Int, target : Int, heuristic: (Int, Int) -> Float) : Pair<ArrayDeque<Int>, MutableList<Int>> {
        if (g.getCoordinates(source) in g.blocks) throw java.lang.RuntimeException("Given source is a block." +
                " Cannot start finding shortest path")
        if (g.getCoordinates(target) in g.blocks) println("Target is a block. No path will exist")
        return visualTargetShortestPath(g.graph, source, target, heuristic)
    }
}