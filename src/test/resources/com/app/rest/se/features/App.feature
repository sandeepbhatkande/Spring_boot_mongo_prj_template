Feature: External Work Requests App Features

  Background:
    Given url HOST+'/login'
    And header Content-Type = 'application/json'
    And request {"enterpriseID": "5e8dc2f808813b000119c611","loginID": "5e8dc2f808813b000119c611_admin","password": "111111"}
    When method POST
    Then status 200
    And def authToken = responseHeaders['Authorization'][0]

  @getConfig:sbhatkande
  Scenario: External Work Requests App scenario for getConfig
    Given url HOST+'/core/app/getConfig/5e8dc2f808813b000119c611/70050525/EXWR000001fps'
    And header Authorization = authToken
    And request {'soexturid':'workrequest','sologinid': 'workrequest','sointusrid':'70050553'}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200
    And  match $.projectId == "5e8deda108813b00016ac4a1"
    And  match $.user_data.source_userid == "70050553"
    And  match $.user_data.loginid == "workrequest"
    And  match $.card_type_list[0].projectId == "5e8deda108813b00016ac4a1"

  @verifyRecaptcha:sbhatkande
  Scenario: External Work Requests App scenario for verifyRecaptcha
    Given url HOST+'/core/app/verifyRecaptcha'
    And header Authorization = authToken
    And request {"response": "03AGdBq24eAa1EDC0I_qzrW9Dcr3lDP8jSDNZJwqyPYkowe2UFHaz-qhdqEqQ4B3M9HYYXgxrbuWwYEIRSH2zZdbYfGgLarMSD0klxkIT1xZ9lCRzZB_gnZzuEj6aPZ5MYXbZlMbLqBIrw5th2p9514hFMoYCsj2dm3N_XPKz7cDtxuCHhURE211Kztva5aOy-yyucjjR7W2a10Zed4J9jAPwFS5z88nEfAt5ZRrmcHhocxP95Vqiv9YBCW6GjdvktT9OPeKD86qn5U6pj7EIq1IIxMlipuPW7P_s8HE-shkqA3aqvBgxVYvznha82ppVrvyp8r5_Ci38-1Y5i5ydS8kNzn6Z0r5sfTDE8JYmPnc5-5wzPe6tZONgFx6ri8NKQRnYMW2E0CSQ1HEOR5K5i11PQLe0mBpRGtg"}
    And header Content-Type = 'application/json'
    When method POST
    Then status 200

