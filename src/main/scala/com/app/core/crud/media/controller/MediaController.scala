package com.app.core.crud.media.controller

import java.io.OutputStream

import com.app.core.crud.media.dataobject.dao.MediaDAO
import com.app.core.crud.media.service.MediaService
import com.app.framework.util.general.WebUtil
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController(value = "mediacontroller")
@RequestMapping(value = Array("/core/media"))
class MediaController {
  @Autowired
  var m_mediaService: MediaService = _

  @PostMapping(path = Array("/create"))
  def create(@RequestBody a_media: MediaDAO): Map[String, String] = {
    m_mediaService.create(a_media)
  }

  @GetMapping(path = Array("/read/{id}"))
  def read(@PathVariable("id") a_id: String, a_response: HttpServletResponse): Unit = {
    val w_mediaDAO: MediaDAO = m_mediaService.read(a_id)
    val w_imageByte: Array[Byte] = w_mediaDAO.image
    var w_contentType: String = w_mediaDAO.mimetype

    w_contentType = WebUtil.removeCRLF(w_contentType)

    a_response.setContentType(w_contentType)
    a_response.setContentLength(w_imageByte.length)

    val w_out: OutputStream = a_response.getOutputStream

    w_out.write(w_imageByte, 0, w_imageByte.length)
    w_out.close()
  }
}