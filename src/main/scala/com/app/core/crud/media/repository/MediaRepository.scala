package com.app.core.crud.media.repository

import com.app.core.crud.media.dataobject.dao.MediaDAO
import org.springframework.data.repository.reactive.ReactiveCrudRepository

trait MediaRepository extends ReactiveCrudRepository[MediaDAO, String]