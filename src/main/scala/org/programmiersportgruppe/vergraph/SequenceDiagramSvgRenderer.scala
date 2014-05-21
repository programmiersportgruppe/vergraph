package org.programmiersportgruppe.vergraph

import java.io.{File, PrintWriter}
import scala.xml._

object SequenceDiagramSvgRenderer {

    def main(args: Array[String]) {
        val svg = new SequenceDiagramSvgRenderer().render(new SequenceDiagramParser(
            """
              |Ben -> Felix: What do you think?
              |Felix -> Everyone: Come take a look at this!
              |Felix -> Ben: This is amazing!
            """.stripMargin
        ).parse()).toString
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

    def angleInDegrees = Math.atan2(y, x) * (180 / Math.PI)

    def translation = s"translate($x, $y)"
    def rotation = s"rotate($angleInDegrees)"
    override def toString() = s"$x,$y"
}

case class Line(from: Vector, to: Vector) {
    def vector = to - from
    def midpoint = from + vector * 0.5
}
case class Arrow(line: Line, head: Elem)

class SequenceDiagramSvgRenderer {
    def render(diagram: SequenceDiagram): Elem = {
        val interParticipantSpace = 200.0
        val interMessageSpace = 40.0
        val participantPosition: Map[String, Vector] = diagram.participants.zipWithIndex.toMap.mapValues(i => Vector((i + 0.5) * interParticipantSpace, 15))
        <svg xmlns="http://www.w3.org/2000/svg" version="1.1" shape-rendering="geometricPrecision" stroke-width="1" text-rendering="geometricPrecision" width="100%" height="100%">
            {diagram.participants.map { participant =>
                val pos = participantPosition(participant)
                <g class="participant" transform={s"${pos.translation}"}>
                    <text style="text-anchor: middle" fill="#124191" text-anchor="middle">{participant}</text>
                    {renderLine(Vector(0, 5) -> Vector(0, (diagram.events.size + 0.75) * interMessageSpace))}
                </g>
            }}
            {diagram.events.zipWithIndex.map { case (Message(from, to, message), index) =>
                val offset = Vector(0, (index + 1) * interMessageSpace)
                val line = Line(participantPosition(from), participantPosition(to))
                <g class="message" transform={offset.translation}>
                    <text dx={line.midpoint.x} dy="10" style="text-anchor: middle" fill="#124191" text-anchor="middle">{message}</text>
                    {renderArrow(line)}
                </g>
            }}
        </svg>
    }

    implicit def pair2Line(endpoints: (Vector, Vector)): Line = Line(endpoints._1, endpoints._2)

    implicit def double2text(value: Double): Text = Text(value.toString)

    implicit def renderLine(line: Line): Elem =
        <line x1={line.from.x} y1={line.from.y} x2={line.to.x} y2={line.to.y} stroke="black"/>

    implicit def renderArrow(line: Line, head: Elem = <polygon class="arrow-head" fill="black" points="0,0 -10,6 -10,-6"/>): Elem =
        <g class="arrow">{
            renderLine(line) ++
            head % Attribute(null, "transform", s"${line.to.translation} ${line.vector.rotation}", Null)
        }</g>
}
