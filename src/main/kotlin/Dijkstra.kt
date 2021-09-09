import java.awt.image.AreaAveragingScaleFilter
import java.util.*

object Dijkstra {

    fun shortestPath(g: Graph, source: Int): Map<Int, Float> { //Pair<List<Int>, Float> {
        if (source !in g.V) {
            throw RuntimeException("Source is not in graph vertices, cannot compute path!")
        }
        val dist = mutableMapOf<Int, Float>()
        dist[source] = 0f

        val Q = PriorityQueue<Pair<Int, Float>>(
             g.V.size,
             compareBy { it.second }
         )
/*
         //Initialize priority queues with distances from source
         for (v in g.V) {
             if (v != source) { dist[v] = g.length(source, v) }
             Q.add(Pair(v, dist[v]!!))
         }

        while (!Q.isEmpty()) {
            //Choose min distance vertex
            val u = Q.poll()

            //For each neighbour still in Q
            // re-evaluate distance from source with intermediate step 'u'
            g.neighbours[u.first]?.keys?.forEach {
                if (Pair(it, dist[it]!!) in Q) {
                    val newDist = u.second + g.length(u.first, it)

                    //If lesser than before, change the queue order, and update distance
                    if (newDist < dist[it]!!) {
                        Q.remove(Pair(it, dist[it]))
                        dist[it] = newDist
                        Q.add(Pair(it, newDist))
                    }
                }
            }
         }


 */

        g.neighbours[source]?.forEach { dist[it.first] = it.second }
        //Initialize priority queues with distances from source
        for (v in g.V) {
            if (v != source && dist[v] == null) {
                dist[v] = Float.POSITIVE_INFINITY
            }
            Q.add(Pair(v, dist[v]!!))
        }
        while (!Q.isEmpty()) {
            //Choose min distance vertex
            val u = Q.poll()
            //For each neighbour still in Q
            // re-evaluate distance from source with intermediate step 'u'
            g.neighbours[u.first]?.forEach {
                if (Pair(it.first, dist[it.first]!!) in Q) {
                    val newDist = u.second + it.second
                    //If lesser than before, change the queue order, and update distance
                    if (newDist < dist[it.first]!!) {
                        Q.remove(it)
                        dist[it.first] = newDist
                        Q.add(Pair(it.first, newDist))
                    }
                }
            }
        }

        return dist
    }

    fun targetShortestPath (g: Graph, source: Int, target : Int) : ArrayDeque<Int>{
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

        val Q = PriorityQueue<Pair<Int, Float>>(
            g.V.size,
            compareBy { it.second }
        )

        g.neighbours[source]?.forEach {
            dist[it.first] = it.second
            prev[it.first] = source
        }
        //Initialize priority queues with distances from source
        for (v in g.V) {
            if (v != source && dist[v] == null) {
                dist[v] = Float.POSITIVE_INFINITY
            }
            Q.add(Pair(v, dist[v]!!))
        }
        while (!Q.isEmpty()) {
            //Choose min distance vertex
            val u = Q.poll()
            if (u.first == target) {break}
            //For each neighbour still in Q
            // re-evaluate distance from source with intermediate step 'u'
            g.neighbours[u.first]?.forEach {
                if (Pair(it.first, dist[it.first]!!) in Q) {
                    val newDist = u.second + it.second
                    //If lesser than before, change the queue order, and update distance
                    if (newDist < dist[it.first]!!) {
                        Q.remove(it)
                        dist[it.first] = newDist
                        Q.add(Pair(it.first, newDist))
                        prev[it.first] = u.first
                    }
                }
            }
        }

        return buildPath(source, target, prev)
    }

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