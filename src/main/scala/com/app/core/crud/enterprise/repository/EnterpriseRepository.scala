package com.app.core.crud.enterprise.repository

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
trait EnterpriseRepository extends ReactiveCrudRepository[EnterpriseDAO, String]