package org.programmiersportgruppe.vergraph

import org.scalatest.FunSuite

class SequenceDiagramParserTest extends FunSuite {

    test("parses simple diagram") {
        assertResult(SequenceDiagram(
            Message("Ben", "Felix", "What do you think of this?") ::
            Message("Felix", "Ben", "Hmm…") ::
            Message("Felix", "Someone Else", "Um, something") ::
            Nil
        )) {
            new SequenceDiagramParser(
                """
                  |Ben -> Felix: What do you think of this?
                  |Felix -> Ben: Hmm…
                  |Felix -> Someone Else: Um, something
                """.stripMargin).parse()
        }
    }

    test("doesn't parse erroneous text") {
        assert(intercept[RuntimeException] {
            new SequenceDiagramParser(
                """Ben -> Felix: What do you think of this?
                  |What on earth is this line here?
                  |Felix -> Someone Else: Um, something
                """.stripMargin).parse()
        }.getMessage.contains("Invalid input"))
    }

}
