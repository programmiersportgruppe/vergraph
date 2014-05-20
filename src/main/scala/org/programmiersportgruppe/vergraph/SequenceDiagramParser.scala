package org.programmiersportgruppe.vergraph

import org.parboiled2._


class SequenceDiagramParser(val input: ParserInput) extends Parser {

    def parse(): SequenceDiagram = {
        import Parser.DeliveryScheme.Throw
        diagram.run()
    }

    def diagram = rule { optional(newline) ~ oneOrMore(statement) ~> SequenceDiagram }

    def statement = rule { message }

    def message = rule { participant ~ " -> " ~ participant ~ ": " ~ contentLine ~> Message }

    def participant = rule { capture(oneOrMore(!(" -> " | ": ") ~ noneOf("\n"))) }

    def newline = rule { oneOrMore("\n") }
    def contentLine = rule { capture(oneOrMore(noneOf("\n"))) ~ newline }
}
