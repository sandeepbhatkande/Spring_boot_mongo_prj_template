package com.app.core.crud.user.repository

import com.app.core.crud.user.dataobject.dao.UserDAO
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends ReactiveCrudRepository[UserDAO, String]