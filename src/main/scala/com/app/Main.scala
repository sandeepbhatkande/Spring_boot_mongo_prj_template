package com.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, ComponentScan, Primary}
import springfox.documentation.builders.{PathSelectors, RequestHandlerSelectors}
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.convert.{DefaultDbRefResolver, DefaultMongoTypeMapper, MappingMongoConverter}
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = Array("com.issuecollector"))
class Application
{
  @Autowired var mongoDbFactory: MongoDbFactory = _
  @Autowired var mongoMappingContext: MongoMappingContext = _

  @Value("${spring.data.mongodb.uri}")
  var db_url : String = _

  @Bean
  def apiDocket(): Docket =
  {
    new Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.issuecollector"))
      .paths(PathSelectors.any())
      .build()
  }

  @Bean
  @Primary
  def objectMapper(): ObjectMapper = {
    var objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }

  @Bean def mappingMongoConverter: MappingMongoConverter = {
    val dbRefResolver = new DefaultDbRefResolver(mongoDbFactory)
    val converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext)
    converter.setTypeMapper(new DefaultMongoTypeMapper(null))
    converter
  }
}


object Main
{
  def main(args: Array[String]): Unit =
  {
    SpringApplication.run(classOf[Application])
  }
}