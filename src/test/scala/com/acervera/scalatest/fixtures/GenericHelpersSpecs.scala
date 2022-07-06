package com.acervera.scalatest.fixtures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GenericHelpersSpecs
    extends AnyWordSpecLike
    with GenericHelpers
    with Matchers {

  def withFixture(param: String)(test: String => Unit): Unit = test(param)

  "GenericHelpers" should {
    "create path per tests" when {
      "is not inside a fixture" in {
        testPath shouldBe "com/acervera/scalatest/fixtures/GenericHelpersSpecs"
      }
      "is inside a fixture" in withFixture("p1") { param1 =>
        withFixture("p2") { param2 =>
          testPath shouldBe "com/acervera/scalatest/fixtures/GenericHelpersSpecs"
        }
      }
    }
  }
}
