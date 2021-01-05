Feature: External Work Requests Enterprise Features

  Background:
    Given url HOST+'/login'
    And header Content-Type = 'application/json'
    And request {"enterpriseID": "5e8dc2f808813b000119c611","loginID": "5e8dc2f808813b000119c611_admin","password": "111111"}
    When method POST
    Then status 200
    And def authToken = responseHeaders['Authorization'][0]

  @create:sbhatkande
  Scenario: External Work Requests Enterprise scenario for Enterprise create
    Given url HOST+'/core/enterprise/create'
    And header Authorization = authToken
    And request {"source_enterpriseid": 100001,"enterprisetype": "SE-Karate","name": "Demo - Enterprise","source_url": "http://autosql2:8080/rest","source_loginid": "workrequest","source_password": "111111"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

  @get:sbhatkande
  Scenario: External Work Requests for get Enterprise scenario
    Given url HOST+'/core/enterprise/get/5e9fc0697f86c60001d2f5a1'
    And header Authorization = authToken
    And header Content-Type = 'application/json'
    When method GET
    Then status 200
    And  match $.source_enterpriseid == 100001
    And  match $.enterprisetype == "SE-Karate"
    And  match $.name == "Demo - Enterprise"

  @update:sbhatkande
  Scenario: External Work Requests Enterprise scenario for Enterprise create
    Given url HOST+'/core/enterprise/update'
    And header Authorization = authToken
    And request {"source_enterpriseid": 100001,"enterprisetype": "Work request-Karate","name": "EWR-Enterprise","source_url": "http://autosql2:8080/rest","source_loginid": "workrequest","source_password": "111111"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

