import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

/**
 * This object implements some common heuristic functions for a square (or rectangular) grid, where every
 * square is a node of the graph.
 * The name are given as [Int] numbers, from left to right, top to bottom as:
 *
 * +-----------------+
 *
 * | 0 | 1 | 2| 3| 4 |
 *
 * | 5 | 6 | 7| 8| 9 |
 *
 * |10|11|12|13|14|
 *
 * |15|16|17|18|19|
 *
 * |20|21|22|23|24|



 * +-----------------+
 */

object Heuristic2D {

    /**
     * [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
     * on a squared grid of side = [width]
     */
    fun manhattan(width: Int): (Int, Int) -> Float {
        return { a, b ->
            val dx = abs(a - b) % width
            val dy = abs(a - b) / width
            (dx + dy).toFloat()
        }
    }

    /**
     * Similar to Manhattan distance, but considering also the possibility of walking
     * diagonally across the squares, by paying a cost [diagonalCost], which may be different from
     * [lateralCost], that is the cost of moving along the 4 cardinal directions.
     *
     * Default values return [Chebyshev distance](https://en.wikipedia.org/wiki/Chebyshev_distance)
     */

    fun diagonal(width : Int, lateralCost : Float = 1f, diagonalCost : Float = 1f): (Int, Int) -> Float {
        return { a, b ->
            val dx = abs(a - b) % width
            val dy = abs(a - b) / width
            lateralCost * (dx + dy) + (diagonalCost - 2f * lateralCost) * min(dx, dy)
        }
    }

    /**
     * Wrapper for [diagonal] distance, with the particular values for costs:
     * - lateralCost = 1
     * - diagonalCost = sqrt(2)
     */
    fun octile (width: Int): (Int, Int) -> Float {
        return diagonal(width, lateralCost = 1f, diagonalCost = sqrt(2f))
    }

    /**
     * Euclidean distance on a squared grid with side = [width]
     */
    fun euclidean(width: Int): (Int, Int) -> Float {
        return { a, b ->
            val dx = abs(a - b) % width
            val dy = abs(a - b) / width
            sqrt( (dx*dx + dy*dy).toFloat() )
        }
    }
}