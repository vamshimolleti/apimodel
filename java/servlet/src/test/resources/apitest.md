# The API Test API

This is an API model for testing

    Name = testapi
    BasePath = http://localhost:8080

    resource tests
      operation GET

    resource noHandler
      operation GET
      
    resource tests/object
      operation GET
      operation PUT
      operation POST
      operation DELETE
      
