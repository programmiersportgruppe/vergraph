package org.programmiersportgruppe.vergraph

import org.parboiled.scala.Parser
import org.parboiled.scala.parserunners.ReportingParseRunner

class SequenceDiagramParser extends Parser {

    def parse(source: String): SequenceDiagram = {
        val parsedDiagram = ReportingParseRunner(diagram).run(source)
        parsedDiagram.result match {
            case Some(d) => d
            case None => ???
        }
    }


    def diagram = rule { optional(newline) ~ oneOrMore(statement) ~~> SequenceDiagram }

    def statement = rule { message }

    def message = rule { participant ~ " -> " ~ participant ~ ": " ~ contentLine ~~> Message }

    def participant = rule { oneOrMore(!(" -> " | ": ") ~ noneOf("\n")) ~> identity }

    def newline = rule { oneOrMore("\n") }
    def contentLine = rule { oneOrMore(noneOf("\n")) ~> identity ~ newline }
}
