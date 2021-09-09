import java.util.*

object AStar {
    fun targetShortestPath (g: Graph, source: Int, target : Int, heuristic: (Int, Int) -> Float) : ArrayDeque<Int> {
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
        val closed = PriorityQueue<Pair<Int, Float>>(
            g.V.size,
            compareBy { it.second }
        )
        open.add(Pair(source, dist[source]!! + heuristic(source, source)))

        while (!open.isEmpty()) {
            //Choose min distance vertex and remove from open list
            val u = open.poll()
            closed.add(u)
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
                    open.remove(it)     //If in open queue -> remove
                    open.add(Pair(it.first, newDist + heuristic(source, it.first)))
                }
            }
        }
        return buildPath(source, target, prev)
    }

    /**
     * Rebuilding path from source to target, given the Map of previous vertex along the desired path
     */
    private fun buildPath (source : Int, target : Int, prev : MutableMap<Int, Int>) : ArrayDeque<Int> {
        val path = ArrayDeque<Int>()
        var u = target
        path.add(u)
        while (prev[u] != null) {
            path.addFirst(prev[u]!!)
            if (prev[u] == source) {
                println(prev[u])
                break
            }
            u = prev[u]!!
        }
        return path
    }
}