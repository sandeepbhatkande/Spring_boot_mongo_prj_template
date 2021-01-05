package com.app.framework.version.repository

import com.app.framework.version.dataobject.dao.VersionDAO
import org.springframework.data.repository.reactive.ReactiveCrudRepository

trait VersionRepository extends ReactiveCrudRepository[VersionDAO, String]