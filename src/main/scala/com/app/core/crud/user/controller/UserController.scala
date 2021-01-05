package com.app.core.crud.user.controller

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.crud.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController(value = "usercontroller")
@RequestMapping(value = Array("/core/user"))
class UserController(@Autowired m_service: UserService)
{
  @PostMapping(path=Array("/create"))
  def create(@RequestBody a_userDAO:UserDAO): String = {
    m_service.create(a_userDAO)
 }

  @GetMapping(path = Array("/get/{id}"))
  def read(@PathVariable("id") a_id: String): UserDAO = {
    m_service.read(a_id)
  }

  @PostMapping(path=Array("/update"))
  def update(@RequestBody a_userDAO:UserDAO): String = {
    m_service.update(a_userDAO)
  }
}