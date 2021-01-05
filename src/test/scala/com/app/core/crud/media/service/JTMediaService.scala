package com.app.core.crud.media.service

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.service.EnterpriseService
import com.app.core.crud.media.repository.MediaRepository
import com.app.framework.util.app.ApplicationProperties
import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.{InjectMocks, Mock, MockitoAnnotations}
import org.scalatest.junit.AssertionsForJUnit
import org.mockito.Mockito.when

@RunWith(classOf[MockitoJUnitRunner])
class JTMediaService extends AssertionsForJUnit {

  @Mock
  var m_repo : MediaRepository = _

  @Mock
  var m_appProps: ApplicationProperties = _

  @Mock
  var m_enterpriseService: EnterpriseService = _

  @InjectMocks
  var m_mediaService : MediaService = _

  @Before
  def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testReadImageUrl(): Unit = {
    val w_mediaId: String = "media101"
    val w_enterpriseId: String = "ent101"
    val w_enterpriseDAO: EnterpriseDAO = new EnterpriseDAO {
      issuecollectordataurl = "issuecollectorURL"
    }

    when( m_enterpriseService.read(w_enterpriseId) ).thenReturn(w_enterpriseDAO)

    val w_actual: Map[String, String] = m_mediaService.readImageUrl(w_mediaId, w_enterpriseId)
    val w_expected: Map[String, String] = Map("url" -> ("issuecollectorURL/core/media/read/" + w_mediaId))

    assertEquals(w_expected, w_actual)
  }
}