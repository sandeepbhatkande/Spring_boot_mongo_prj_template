package com.app.core.crud.media.service

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.service.EnterpriseService
import com.app.core.crud.media.dataobject.dao.MediaDAO
import com.app.core.crud.media.repository.MediaRepository
import com.app.framework.util.app.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "MediaService")
class MediaService {
  @Autowired
  var m_repo : MediaRepository = _

  @Autowired
  var m_appProps: ApplicationProperties = _

  @Autowired
  var m_enterpriseService: EnterpriseService = _

  def create(a_media: MediaDAO): Map[String, String] = {
    val w_mediaId = m_repo.save(a_media).block().id

    readImageUrl(w_mediaId, a_media.enterpriseid)
  }

  def read(a_imageId: String): MediaDAO = {
    m_repo.findById(a_imageId).block()
  }

  def readImageUrl(a_imageId: String, a_enterpriseId: String): Map[String, String] = {
    val w_enterpriseDAO: EnterpriseDAO = m_enterpriseService.read(a_enterpriseId)

    Map( "url" -> ( w_enterpriseDAO.issuecollectordataurl + "/core/media/read/" + a_imageId ) )
  }
}