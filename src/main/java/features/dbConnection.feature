Feature: connection database

  Background:
    * def dwDatabase = new dbConnection(dwDatabase)

  Scenario: database
    * string query = read(sqlFilesPath + 'query.sql')
    * def queryResult = dwDatabase.insertUpdateDeleteQuery(query)
    * match queryResult == 'SUCCESS'

    
    * text selectQuery =
    """
    select query
    """
    * json resultJson = dwDatabase.runQueryAndGetJsonArray(selectQuery)
    * match resultJson[0].STATUS = 'FAIL'