package com.app.core.crud.app.dataobject.dao

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class JTestAppConfigDAO extends AssertionsForJUnit {
  var m_appConfigDAO : AppConfigDAO = _

  @Test def testDAO(): Unit = {
    m_appConfigDAO = new AppConfigDAO {
      projectId = ""
      user_fullname = ""
      enterpriseType = ""
      helpURL = ""
      user_data = null
      versionInfo = null
    }
  }
}