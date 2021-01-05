package com.app.core.crud.media.dataobject.dao

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class JTestMediaDAO extends AssertionsForJUnit {
  var m_mediaDAO: MediaDAO = _

  @Test def testDAO(): Unit = {
    m_mediaDAO = new MediaDAO{
      id = null
      image = null
      enterpriseid = null
      mimetype = null
    }
  }
}