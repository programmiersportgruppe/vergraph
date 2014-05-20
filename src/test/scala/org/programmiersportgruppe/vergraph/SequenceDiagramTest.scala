package org.programmiersportgruppe.vergraph

import org.scalatest.FunSuite

class SequenceDiagramTest extends FunSuite {

    test("extracts participants") {
        val diagram = SequenceDiagram(
            Message("Ben", "Felix", "What do you think of this?") ::
                Message("Felix", "Ben", "Hmm…") ::
                Message("Felix", "Someone Else", "Um, something") ::
                Nil
        )

        assertResult(Seq("Ben", "Felix", "Someone Else")) {
            diagram.participants
        }
    }

}
