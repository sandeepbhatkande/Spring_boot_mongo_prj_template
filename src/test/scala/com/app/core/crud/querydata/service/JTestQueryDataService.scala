package com.app.core.crud.querydata.service

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.{InjectMocks, MockitoAnnotations}
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestQueryDataService extends AssertionsForJUnit {

  @InjectMocks var m_queryDataService : QueryDataService = _

  @Before def initialize(): Unit = {
    m_queryDataService = new QueryDataService()
    MockitoAnnotations.initMocks(this)
  }

  @Test def testEncryptPassword(): Unit = {
    assertEquals("MTExMTEx", m_queryDataService.encryptPassword("111111"))
  }
}