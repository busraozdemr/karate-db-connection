Feature: database connection

  Scenario: db connection
    #jdbc:oracle:thin:@url:port/database
    #sample : jdbc:oracle:thin:@sys.blabla.com.tr:1121/blatest
    * def config = { username: 'sa', password: '', url: 'jdbc:h2:mem:testdb', driverClassName: 'org.h2.Driver' }
    * def DbUtils = Java.type('database.DbUtils')
    * def db = new DbUtils(config)

    * def dogs = db.readRows('SELECT * FROM DOGS')
    * print dogs
