package com.app.core.otherOperations.onBoarding.controller

import com.app.core.otherOperations.onBoarding.service.OnBoardingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._

@RestController(value = "onboardingcontroller")
@RequestMapping(value = Array("/onBoarding"))
class OnBoardingController (@Autowired m_Service: OnBoardingService)
{
  @GetMapping(path = Array("/boardData/{enterpriseId}/{sourceProjectId}/{sourceExternalProjectCode}/{sourceUserId}/{sourceExternalUserID}/{loginID}"))
  def onBoardingProcess(@PathVariable enterpriseId: String, @PathVariable sourceProjectId: Int,
    @PathVariable sourceExternalProjectCode: String, @PathVariable sourceUserId: String,
    @PathVariable sourceExternalUserID: String, @PathVariable loginID: String): ResponseEntity[Object] = {
    m_Service.onBoardingProcess(enterpriseId, sourceProjectId, sourceExternalProjectCode, sourceUserId, sourceExternalUserID, loginID)
  }
}