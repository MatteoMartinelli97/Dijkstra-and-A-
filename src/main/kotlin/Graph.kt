import java.lang.RuntimeException

class Graph (
    val V : MutableList<Int> = mutableListOf(),
    //val neighbours : MutableMap<Int, MutableMap<Int, Float>> = mutableMapOf(),
    val neighbours : MutableMap<Int, MutableList<Pair<Int, Float>>> = mutableMapOf(),
    val directed : Boolean = false
)
{
    init{
        for (v in V) {
            neighbours[v] = mutableListOf()
        }
    }
/*    fun addVertex (v : Int, edges : MutableMap<Int, Float>) {
        V.add(v)
        neighbours[v] = edges
    }

    fun addEdge (v : Int, u : Int, weight : Float) {
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

    fun length(a : Int, b: Int) : Float {
        return neighbours[a]?.get(b) ?: Float.POSITIVE_INFINITY
    }

 */

    fun addVertex (v : Int, edges : MutableList<Pair<Int, Float>>) {
        V.add(v)
        neighbours[v] = edges
    }

    fun addEdge (v : Int, u : Int, weight : Float) {
        if (v in V) {
            neighbours[v]!!.add(Pair(u, weight))
            if (!directed) {
                if (u in V ) {neighbours[u]!!.add(Pair(v, weight))}
                else {throw RuntimeException("Trying to add edge of non existing vertex $u")}
            }
        }
        else{
            throw RuntimeException("Trying to add edge of non existing vertex $v")
        }
    }

    override fun toString(): String {
        var s = ""
        for (v in V) {
            s += "$v -> ("
            neighbours[v]?.forEach{s += "$it"}
            s += ")\n"
        }
        return s
    }
/*    fun length(a : Int, b: Int) : Float {
        return neighbours[a]?.get(b) ?: Float.POSITIVE_INFINITY
    }

 */
}