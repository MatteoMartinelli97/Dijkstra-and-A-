


fun main(args: Array<String>) {

    val blocks =  MutableList(4) {Pair(it, 3)}
    val grid = Grid2D(90, blocks, randomBlocks = true)
    val target = Pair(73, 85)
    val p = AStar.targetShortestPath(
        grid,
        source = (0),
        target = grid.getId(target.first, target.second),
        heuristic = Heuristic2D.euclidean(grid.nSquares)
    )
    val p2 = Dijkstra.targetShortestPath(
        grid.graph,
        source = (0),
        target = grid.getId(target.first, target.second)
    )

    GridFrame(grid, p)

}
