import java.lang.RuntimeException

class Graph<T>(
    val V: MutableList<T> = mutableListOf(),
    val neighbours : MutableMap<T, MutableMap<T, Float>> = mutableMapOf(),
    val directed: Boolean = false
) {
    init {
        for (v in V) {
            neighbours[v] = mutableMapOf()
        }
    }
    fun addVertex (v : T, edges : MutableMap<T, Float>) {
        V.add(v)
        neighbours[v] = edges
    }

    fun addEdge (v : T, u : T, weight : Float) {
        if (v in V) {
            neighbours[v]!!.set(u, weight)
            if (!directed) {
                if (u in V ) {neighbours[u]!!.set(v, weight)}
                else {throw RuntimeException("Trying to add edge of non existing vertex $u")}
            }
        }
        else{
            throw RuntimeException("Trying to add edge of non existing vertex $v")
        }
    }

    fun length(a : T, b: T) : Float {
        return neighbours[a]?.get(b) ?: Float.POSITIVE_INFINITY
    }
/*
    fun removeEdge(v: T, u: T) {
        if (v in V) {
            neighbours[v]!!.removeIf { it.first == u }
            if (!directed) {
                if (u in V) neighbours[u]!!.removeIf { it.first == v }
                else {
                    throw RuntimeException("Trying to add edge of non existing vertex $u")
                }
            }
        } else {
            throw RuntimeException("Trying to add edge of non existing vertex $v")
        }
    }

    fun removeVertex(v: T) {
        neighbours[v]?.forEach {
            neighbours[it.first]?.removeIf { n -> n.first == v }
        }
        neighbours[v] = mutableListOf()
    }


 */
    override fun toString(): String {
        var s = ""
        for (v in V) {
            s += "$v -> ("
            neighbours[v]?.forEach { s += "$it" }
            s += ")\n"
        }
        return s
    }
}