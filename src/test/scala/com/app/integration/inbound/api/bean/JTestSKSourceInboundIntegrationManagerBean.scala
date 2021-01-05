package com.app.integration.inbound.api.bean

import com.app.integration.inbound.helper.InboundIntegrationHelper
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.{InjectMocks, Mock, MockitoAnnotations}

@RunWith(classOf[MockitoJUnitRunner])
class JTestSKSourceInboundIntegrationManagerBean {

  @Mock
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  @InjectMocks
  var m_skSourceInboundManagerBean: SKSourceInboundIntegrationManagerBean = _

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

}