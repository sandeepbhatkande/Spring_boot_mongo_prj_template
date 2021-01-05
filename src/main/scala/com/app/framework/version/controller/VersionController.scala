package com.app.framework.version.controller

import com.app.framework.version.dataobject.dao.VersionDAO
import com.app.framework.version.service.VersionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

@RestController(value = "versioncontroller")
@RequestMapping(value = Array("/version"))
class VersionController {
  @Autowired
  var m_versionService: VersionService = _

  @GetMapping(path=Array("/getCurrentVersion"))
  def getCurrentVersion: VersionDAO = {
    m_versionService.getCurrentVersion
  }
}