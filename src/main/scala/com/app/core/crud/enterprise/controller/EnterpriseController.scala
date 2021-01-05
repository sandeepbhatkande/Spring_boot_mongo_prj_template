package com.app.core.crud.enterprise.controller

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.service.EnterpriseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController(value = "enterprisecontroller")
@RequestMapping(value = Array("/core/enterprise"))
class EnterpriseController(@Autowired m_enterpriseservice: EnterpriseService)
{
  @PostMapping(path=Array("/create"))
  def create(@RequestBody a_enterpriseDAO:EnterpriseDAO): String = {
    m_enterpriseservice.create(a_enterpriseDAO)
  }

  @GetMapping(path = Array("/get/{id}"))
  def read(@PathVariable("id") a_id: String): EnterpriseDAO = {
    m_enterpriseservice.read(a_id)
  }

  @PostMapping(path=Array("/update"))
  def update(@RequestBody a_enterpriseDAO:EnterpriseDAO): String = {
    m_enterpriseservice.update(a_enterpriseDAO)
  }
}