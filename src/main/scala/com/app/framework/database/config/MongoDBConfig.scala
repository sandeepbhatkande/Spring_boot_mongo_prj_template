package com.app.framework.database.config

import com.github.mongobee.Mongobee
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoDBConfig(@Autowired val mongoTemplate: MongoTemplate)
{
  @Value("${spring.data.mongodb.uri}")
  var db_url : String = _

  @Bean
  def mongobee():Mongobee = {

    new Mongobee(db_url)
      .setMongoTemplate(mongoTemplate)
      .setChangeLogsScanPackage("com.app.framework.database.mongodb")
      .setChangelogCollectionName("app_db_changelog")
      .setLockCollectionName("app_db_migration_processlog")
  }
}
