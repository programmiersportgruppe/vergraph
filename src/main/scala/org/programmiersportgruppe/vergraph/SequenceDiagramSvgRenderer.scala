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
//            <!--polygon xmlns="http://www.w3.org/2000/svg" fill="black" points="180,35 170,41 170,29"/-->
        </g>
    }
}

object Blah {
        <svg xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:cc="http://creativecommons.org/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" version="1.1" viewBox="0 0 600 238" shape-rendering="geometricPrecision" stroke-width="1" text-rendering="geometricPrecision" id="svg4297" inkscape:version="0.48.1 r9760" width="100%" height="100%" sodipodi:docname="debug-log-sequence-diagram.svg">
            <metadata id="metadata4435">
                <rdf:RDF>
                    <cc:Work rdf:about="">
                        <dc:format>image/svg+xml</dc:format>
                        <dc:type rdf:resource="http://purl.org/dc/dcmitype/StillImage"/>
                    </cc:Work>
                </rdf:RDF>
            </metadata>
            <defs id="defs4433"/>
            <sodipodi:namedview pagecolor="#ffffff" bordercolor="#666666" borderopacity="1" objecttolerance="10" gridtolerance="10" guidetolerance="10" inkscape:pageopacity="0" inkscape:pageshadow="2" inkscape:window-width="1605" inkscape:window-height="926" id="namedview4431" showgrid="false" inkscape:zoom="2.7551402" inkscape:cx="310.5" inkscape:cy="115.75" inkscape:window-x="157" inkscape:window-y="104" inkscape:window-maximized="0" inkscape:current-layer="svg4297"/>
            <polygon fill="white" points="43,6 75,6 75,16 43,16" id="polygon4299"/>
            <text x="60" y="15" textLength="30" font-family="Nokia Pure Text" font-size="12" fill="#124191" text-anchor="middle" id="text4301">

                Client
            </text>
            <polygon fill="white" points="149,6 209,6 209,16 149,16" id="polygon4303"/>
            <text x="180" y="15" textLength="58" font-family="Nokia Pure Text" font-size="12" fill="#124191" text-anchor="middle" id="text4305">

                Places API
            </text>
            <polygon fill="white" points="260,6 338,6 338,16 260,16" id="polygon4307"/>
            <text x="300" y="15" textLength="77" font-family="Nokia Pure Text" font-size="12" fill="#124191" text-anchor="middle" id="text4309">

                Dependency 1
            </text>
            <polygon fill="white" points="380,6 458,6 458,16 380,16" id="polygon4311"/>
            <text x="420" y="15" textLength="77" font-family="Nokia Pure Text" font-size="12" fill="#124191" text-anchor="middle" id="text4313">

                Dependency 2
            </text>
            <polygon fill="white" points="500,6 578,6 578,16 500,16" id="polygon4315"/>
            <text x="540" y="15" textLength="77" font-family="Nokia Pure Text" font-size="12" fill="#124191" text-anchor="middle" id="text4317">

                Dependency 3
            </text>
            <line x1="60" y1="22" x2="60" y2="49" stroke="black" id="line4319"/>
            <line x1="180" y1="22" x2="180" y2="49" stroke="black" id="line4321"/>
            <line x1="300" y1="22" x2="300" y2="49" stroke="black" id="line4323"/>
            <line x1="420" y1="22" x2="420" y2="49" stroke="black" id="line4325"/>
            <line x1="540" y1="22" x2="540" y2="49" stroke="black" id="line4327"/>
            <line x1="60" y1="35" x2="180" y2="35" stroke="black" id="line4329"/>
            <polygon fill="black" points="180,35 170,41 170,29" id="polygon4331"/>
            <line x1="60" y1="49" x2="60" y2="76" stroke="black" id="line4333"/>
            <line x1="180" y1="49" x2="180" y2="76" stroke="black" id="line4335"/>
            <line x1="300" y1="49" x2="300" y2="76" stroke="black" id="line4337"/>
            <line x1="420" y1="49" x2="420" y2="76" stroke="black" id="line4339"/>
            <line x1="540" y1="49" x2="540" y2="76" stroke="black" id="line4341"/>
            <line x1="180" y1="62" x2="300" y2="62" stroke="black" id="line4343"/>
            <polygon fill="black" points="300,62 290,68 290,56" id="polygon4345"/>
            <line x1="60" y1="76" x2="60" y2="103" stroke="black" id="line4347"/>
            <line x1="180" y1="76" x2="180" y2="103" stroke="black" id="line4349"/>
            <line x1="300" y1="76" x2="300" y2="103" stroke="black" id="line4351"/>
            <line x1="420" y1="76" x2="420" y2="103" stroke="black" id="line4353"/>
            <line x1="540" y1="76" x2="540" y2="103" stroke="black" id="line4355"/>
            <line x1="180" y1="89" x2="420" y2="89" stroke="black" id="line4357"/>
            <polygon fill="black" points="420,89 410,95 410,83" id="polygon4359"/>
            <line x1="60" y1="103" x2="60" y2="130" stroke="black" id="line4361"/>
            <line x1="180" y1="103" x2="180" y2="130" id="line4363" stroke="black"/>
            <line x1="300" y1="103" x2="300" y2="130" stroke="black" id="line4365"/>
            <line x1="420" y1="103" x2="420" y2="130" stroke="black" id="line4367"/>
            <line x1="540" y1="103" x2="540" y2="130" stroke="black" id="line4369"/>
            <line x1="180" y1="116" x2="540" y2="116" stroke="black" id="line4371"/>
            <polygon fill="black" points="540,116 530,122 530,110" id="polygon4373"/>
            <line x1="60" y1="130" x2="60" y2="157" stroke="black" id="line4375"/>
            <line x1="180" y1="130" x2="180" y2="157" stroke="black" id="line4377"/>
            <line x1="300" y1="130" x2="300" y2="157" stroke="black" id="line4379"/>
            <line x1="420" y1="130" x2="420" y2="157" stroke="black" id="line4381"/>
            <line x1="540" y1="130" x2="540" y2="157" stroke="black" id="line4383"/>
            <line x1="300" y1="143" x2="180" y2="143" stroke="black" stroke-dasharray="2,2" id="line4385"/>
            <polygon fill="black" points="180,143 190,149 190,137" id="polygon4387"/>
            <line x1="60" y1="157" x2="60" y2="184" stroke="black" id="line4389"/>
            <line x1="180" y1="157" x2="180" y2="184" stroke="black" id="line4391"/>
            <line x1="300" y1="157" x2="300" y2="184" stroke="black" id="line4393"/>
            <line x1="420" y1="157" x2="420" y2="184" stroke="black" id="line4395"/>
            <line x1="540" y1="157" x2="540" y2="184" stroke="black" id="line4397"/>
            <line x1="420" y1="170" x2="180" y2="170" stroke="black" stroke-dasharray="2,2" id="line4399"/>
            <polygon fill="black" points="180,170 190,176 190,164" id="polygon4401"/>
            <line x1="60" y1="184" x2="60" y2="211" stroke="black" id="line4403"/>
            <line x1="180" y1="184" x2="180" y2="211" stroke="black" id="line4405"/>
            <line x1="300" y1="184" x2="300" y2="211" stroke="black" id="line4407"/>
            <line x1="420" y1="184" x2="420" y2="211" stroke="black" id="line4409"/>
            <line x1="540" y1="184" x2="540" y2="211" stroke="black" id="line4411"/>
            <line x1="540" y1="197" x2="180" y2="197" stroke="black" stroke-dasharray="2,2" id="line4413"/>
            <polygon fill="black" points="180,197 190,203 190,191" id="polygon4415"/>
            <line x1="60" y1="211" x2="60" y2="238" stroke="black" id="line4417"/>
            <line x1="180" y1="211" x2="180" y2="238" stroke="black" id="line4419"/>
            <line x1="300" y1="211" x2="300" y2="238" stroke="black" id="line4421"/>
            <line x1="420" y1="211" x2="420" y2="238" stroke="black" id="line4423"/>
            <line x1="540" y1="211" x2="540" y2="238" stroke="black" id="line4425"/>
            <line x1="180" y1="224" x2="60" y2="224" stroke="black" stroke-dasharray="2,2" id="line4427"/>
            <polygon fill="black" points="60,224 70,230 70,218" id="polygon4429"/>
        </svg>
}
