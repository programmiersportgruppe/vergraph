package org.programmiersportgruppe.vergraph

import org.parboiled2._


class SequenceDiagramParser(val input: ParserInput) extends Parser {

    def parse(): SequenceDiagram = {
        import Parser.DeliveryScheme.Either
        diagram.run() match {
            case Right(diagram) => diagram
            case Left(error) => throw new RuntimeException(formatError(error, showTraces = true))
        }
    }

    def diagram = rule { zeroOrMore(space) ~ optional(newline) ~ oneOrMore(statement) ~ EOI ~> SequenceDiagram }

    def statement = rule { message }

    def message = rule { participant ~ " -> " ~ participant ~ ": " ~ contentLine ~> Message }

    def participant = rule { capture(oneOrMore(!(" -> " | ": ") ~ noneOf("\n"))) }

    def newline = rule { zeroOrMore(space) ~ oneOrMore("\n" ~ zeroOrMore(space)) }
    def contentLine = rule { capture(oneOrMore(noneOf("\n"))) ~ newline }
    def space = rule { anyOf(" \t") }
}
