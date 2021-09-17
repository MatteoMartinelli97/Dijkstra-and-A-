import de.topobyte.osm4j.core.model.iface.OsmNode
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.geom.Line2D
import java.lang.RuntimeException
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.math.*


internal object DrawLines {
    @JvmStatic
    fun main(args: Array<String>) {
    }
}

internal class LineComponent(width: Int, height: Int, val bbox : Array<Double>) : JComponent() {
    val lines: MutableList<Line2D.Double>
    val realWidth : Double
    val realHeight : Double
    val minLatScale : Double
    val minLon : Double

    init {
        preferredSize = Dimension(width, height)
        lines = mutableListOf()
        if (bbox.size != 4) throw RuntimeException("Bounding box must be of the form [latMin, lonMin, latMax, lonMax]")
        realHeight = ln( (1.0+sin( abs(bbox[2] - bbox[0]) * PI/180.0)) / (1.0-sin(abs(bbox[2] - bbox[0]) * PI/180.0)) )
        realWidth = abs(bbox[3] - bbox[1])
        minLon = bbox[1]
        minLatScale = ln( (1.0+sin(bbox[0] * PI/180.0))/(1.0-sin(bbox[0] * PI/180.0)) )
    }

    fun addLine(n1 : OsmNode, n2 : OsmNode) {
        val x1 = (n1.longitude - minLon) / realWidth
        val y1 = (ln( (1.0+sin(n1.latitude * PI/180.0))/(1.0-sin(n1.latitude * PI/180.0)) )  - minLatScale) / realHeight
        val x2 = (n2.longitude - minLon) / realWidth
        val y2 = (ln( (1.0+sin(n2.latitude * PI/180.0))/(1.0-sin(n2.latitude * PI/180.0)) )  - minLatScale) / realHeight

        println("n1 = ($x1, $y1); n2 = ($x2, $y2)")
        val line = Line2D.Double(
            x1, y1, x2, y2
        )
        lines.add(line)
        repaint()
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.color = Color.white
        g.fillRect(0, 0, width, height)
        val d: Dimension = preferredSize
        g.color = Color.black
        for (line in lines) {
            g.drawLine(
                (line.getX1() * width).toInt(),
                (line.getY1() * height).toInt(),
                (line.getX2() * width).toInt(),
                (line.getY2() * height).toInt()
            )
        }
    }


}
/*

fun kindOfMain () {
    val r = Runnable {
        val lineComponent = LineComponent(400, 400, bbox = bbox)
        for (v in graph.V) {
            if (graph.neighbours[v] != null)
                for (e in graph.neighbours[v]!!) lineComponent.addLine(nodes[v]!!, nodes[e.first]!!)
        }
        JOptionPane.showMessageDialog(null, lineComponent)
    }

    SwingUtilities.invokeLater(r)
}

 */