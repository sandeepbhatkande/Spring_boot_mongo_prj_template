Feature: External Work Requests Project Features

  Background:
    Given url HOST+'/login'
    And header Content-Type = 'application/json'
    And request {"enterpriseID": "5e8dc2f808813b000119c611","loginID": "5e8dc2f808813b000119c611_admin","password": "111111"}
    When method POST
    Then status 200
    And def authToken = responseHeaders['Authorization'][0]

  @create:sbhatkande
  Scenario: External Work Requests Project create scenario
    Given url HOST+'/core/project/create'
    And header Authorization = authToken
    And request {"source_projectid": "123", "enterpriseid": "5e8dc2f808813b000119c611", "projectname":"New project","source_externalprojectid": "12345","projectcode": "12345","status":"open"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

  @read:sbhatkande
  Scenario: External Work Requests for read project scenario
    Given url HOST+'/core/project/get/5e8deda108813b00016ac4a1'
    And header Authorization = authToken
    And header Content-Type = 'application/json'
    When method GET
    Then status 200
    And match $.projectname == "Food Processiong"
    And match $.source_projectid == 70050525

  @read:sbhatkande
  Scenario: External Work Requests for read project scenario
    Given url HOST+'/core/project/get/70050525/5e9fc0697f86c60001d2f5a1'
    And header Authorization = authToken
    And header Content-Type = 'application/json'
    When method GET
    Then status 200

  @update:sbhatkande
  Scenario: External Work Requests Project Update scenario
    Given url HOST+'/core/project/update'
    And header Authorization = authToken
    And request {"source_projectid": "123", "enterpriseid": "5e8dc2f808813b000119c611", "projectname":"New project","source_externalprojectid": "12345","projectcode": "12345","status":"close"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200
