package com.app.framework.util.app

import javax.validation.constraints.Pattern
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApplicationProperties {

    @Pattern(regexp = "^(0|[1-9][0-9]*)$")
    @Value("${app.expiration.time}")
    val JWT_EXPIRATION_TIME = 864000000

    @Value("${data.server.hostname}")
    val DATA_SERVER_HOSTNAME: String = "http://issuecollector.digite.com/"

    @Value("${client.server.hostname}")
    val CLIENT_SERVER_HOSTNAME: String = "http://issuecollector.digite.com/"

    @Value("${data.server.context-path}")
    val DATA_SERVER_CONTEXT_PATH: String = "issuecollector"

    @Value("${external.httpproxy.ip}")
    val EXTERNAL_HTTP_PROXY_IP: String = null

    @Value("${external.httpproxy.port}")
    val EXTERNAL_HTTP_PROXY_PORT: String = null

    @Value("${external.httpproxy.noproxyhosts}")
    val EXTERNAL_HTTP_NON_PROXY_HOSTS: String = null

    @Value("${data.server.hostip}")
    val DATA_SERVER_HOSTIP: String = null

    @Value("${server.port}")
    val SERVER_PORT: Int = 0

    @Value("${google.recaptcha.enabled}")
    var GOOGLE_RECAPTCHA_ENABLED:String = _

    @Value("${google.recaptcha.sitekey}")
    var GOOGLE_RECAPTCHA_SITEKEY:String = _

    @Value("${google.recaptcha.secretkey}")
    val GOOGLE_RECAPTCHA_SECRETKEY:String = null

    @Value("${app.virtualization.mountebank.url}")
    val VIRTUALIZATION_MOUNTEBANK_URL: String = null

    @Value("${app.karate.thread.count}")
    val APP_KARATE_THREAD_COUNT: Int = 1
}
