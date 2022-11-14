function fn() {
 var env = karate.env; // get system property 'karate.env'
 karate.log('karate.env system property was:', env);
 if (!env) {
   env = 'dev';
 }
 var config = {
   sqlFilesPath:"sql/"+env+"/",
   jsonFilesPath:"json/"+env+"/",
   xmlFilesPath:"xml/"+env+"/",
   dbConnection:Java.type('database.Database'),
 }
 if (env == 'dev') {
   config.dwDatabaseUrl = 'jdbc:mysql://localhost:3306/sonoo',
   config.dwDatabaseUserName = 'root',
   config.dwDatabasePassword ='root',
   config.dwDatabase=[config.dwDatabaseUrl,config.dwDatabaseUserName,config.dwDatabasePassword]
 } else if (env == 'e2e') {
   // customize
 }
 return config;
}