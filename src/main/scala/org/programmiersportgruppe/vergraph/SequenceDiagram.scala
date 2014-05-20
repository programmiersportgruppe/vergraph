package org.programmiersportgruppe.vergraph

sealed abstract class SequenceDiagramEvent

case class Message(from: String, to: String, message: String) extends SequenceDiagramEvent

case class SequenceDiagram(events: Seq[SequenceDiagramEvent]) {
    val participants = events.flatMap {
        case Message(from, to, message) => List(from, to)
    }.distinct
}
