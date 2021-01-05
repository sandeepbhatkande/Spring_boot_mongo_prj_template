package com.app

import java.io.File
import java.util

import com.intuit.karate.{Results, Runner}
import com.app.framework.util.app.ApplicationProperties
import cucumber.api.CucumberOptions
import cucumber.api.java.After
import net.masterthought.cucumber.{Configuration, ReportBuilder}
import org.apache.commons.io.FileUtils
import org.junit.Test

@CucumberOptions (features = Array("src/test/resources/com/storymapping/rest/#source#"), format = Array({"json:target/cucumber.json"}))
class karateRunner() {

    var m_appProps: ApplicationProperties = new ApplicationProperties()

    @Test
    def testParallel(): Unit = {
        val results: Results = Runner.parallel(getClass, m_appProps.APP_KARATE_THREAD_COUNT)
        generateReport(results.getReportDir)
    }

    @After
    def generateReport(karateOutputPath: String): Unit = {
        val jsonFiles: util.Collection[File] = FileUtils.listFiles(new File(karateOutputPath), Array({"json"}), true)
        val jsonPaths: util.ArrayList[String] = new util.ArrayList(jsonFiles.size())

        jsonFiles.forEach(file => jsonPaths.add(file.getAbsolutePath))

        val config: Configuration = new Configuration(new File("build"), "MS-RestIntegration")
        val reportBuilder: ReportBuilder = new ReportBuilder(jsonPaths, config)

        reportBuilder.generateReports()
    }
}