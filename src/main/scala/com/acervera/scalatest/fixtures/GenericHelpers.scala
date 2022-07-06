package com.acervera.scalatest.fixtures

import org.scalatest.TestSuite

trait GenericHelpers { this: TestSuite =>
  sealed trait CleanUpMode
  case object CLEAN_ALWAYS extends CleanUpMode
  case object KEEP_ON_ERROR extends CleanUpMode
  case object KEEP_ALWAYS extends CleanUpMode

  def testPath: String = suiteId.replace(".", "/")
}
