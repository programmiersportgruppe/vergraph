package org.programmiersportgruppe.vergraph

import scala.xml.{NodeSeq, Elem}
import java.io.{PrintWriter, File, ByteArrayInputStream}

object SequenceDiagramSvgRenderer {

    def main(args: Array[String]) {
        val svg = new SequenceDiagramSvgRenderer().render(new SequenceDiagramParser().parse(
            """
              |Ben -> Felix: What do you think?
              |Felix -> Everyone: Come take a look at this!
              |Felix -> Ben: This is amazing!
            """.stripMargin
        )).toString
        println(svg)

        val writer = new PrintWriter(new File("sequence.svg"))
        writer.write(svg)
        writer.close()
    }
}

case class Vector(x: Double, y: Double) {
    def +(other: Vector) = Vector(x + other.x, y + other.y)
    def -(other: Vector) = this + -other
    def unary_- = Vector(-x, -y)

    def *(scalar: Double) = Vector(x * scalar, y + scalar)

    def normalise = this * (1 / magnitude)
    def magnitude = Math.sqrt(x * x + y * y)

    def translation = s"translate($x, $y)"
    override def toString() = s"$x,$y"
}

class SequenceDiagramSvgRenderer {
    def render(diagram: SequenceDiagram): Elem = {
        val participantPosition: Map[String, Vector] = diagram.participants.zipWithIndex.toMap.mapValues(i => Vector(i * 100 + 50, 15))
        <svg xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:cc="http://creativecommons.org/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" version="1.1" viewBox="0 0 600 238" shape-rendering="geometricPrecision" stroke-width="1" text-rendering="geometricPrecision" width="100%" height="100%">
            {diagram.participants.map { participant =>
                val pos = participantPosition(participant)
                <g transform={s"${pos.translation}"}>
                    <text style="text-anchor: middle" fill="#124191" text-anchor="middle" id="text4301">{participant}</text>
                    <line xmlns="http://www.w3.org/2000/svg" x1="0" y1="5" x2="0" y2={(50 * diagram.events.size).toString} stroke="black" id="line4329"/> ++
                </g>
            }}
            {diagram.events.zipWithIndex.map { case (Message(from, to, message), index) =>
                def offset(y: Int) = (index * 50 + y).toString
                <g transform={s"translate(0 ${offset(30)})"}>
                    <text dx={(participantPosition(from) + (participantPosition(to) - participantPosition(from)) * 0.5).x.toString} dy="12" style="text-anchor: middle" fill="#124191" text-anchor="middle" id="text4301">{message}</text>
                    {arrow(participantPosition(from), participantPosition(to))}
                </g>
            }}
        </svg>
    }

    def arrow(from: Vector, to: Vector): Elem = {
        <g>
            <line xmlns="http://www.w3.org/2000/svg" x1={from.x.toString} y1={from.y.toString} x2={to.x.toString} y2={to.y.toString} stroke="black" id="line4329"/> ++
            {
                val head = to
                val direction = to - from
                val top = Vector(10, 6)
                val bottom = Vector(10, -6)
                val angle = Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 180
                <polygon xmlns="http://www.w3.org/2000/svg" fill="black" transform={s"${head.translation} rotate($angle)"} points={s"0,0 $top $bottom"}/>
            }
        </g>
    }
}
