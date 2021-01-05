Feature: External Work Requests User Features

  Background:
    Given url HOST+'/login'
    And header Content-Type = 'application/json'
    And request {"enterpriseID": "5e8dc2f808813b000119c611","loginID": "5e8dc2f808813b000119c611_admin","password": "111111"}
    When method POST
    Then status 200
    And def authToken = responseHeaders['Authorization'][0]

  @create:sbhatkande
  Scenario: External Work Requests User create scenario
    Given url HOST+'/core/user/create'
    And header Authorization = authToken
    And request {"source_userid": "123", "source_externaluserid": "Sandeep", "loginid":"sbhatkande","firstname": "sandeep","lastname": "bhatkande","email":"sandeep@digite.com","status":"active","password":"111111"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

  @read:sbhatkande
  Scenario: External Work Requests for read user scenario
    Given url HOST+'/core/user/get/5e8deda108813b00016ac4a1'
    And header Authorization = authToken
    And header Content-Type = 'application/json'
    When method GET
    Then status 200

  @update:sbhatkande
  Scenario: External Work Requests user update scenario
    Given url HOST+'/core/user/update'
    And header Authorization = authToken
    And request {"source_userid": "123", "source_externaluserid": "Sandeep", "loginid":"sbhatkande","firstname": "sandeep","lastname": "bhatkande","email":"sandeep1@digite.com","status":"deActive","password":"111111"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200
