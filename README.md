# Tokenator

The Open Tokenizer Project

### Building and running for the first time

#### Check out the source code
```
# Won't need the username after we make this repository public
$ git clone https://YOUR_GITHUB_USERNAME@github.com/SimplyTapp/Tokenator
```

#### Configure the application.properties file
```
$ cd Tokenator/src/main/resources
$ cp application.properties-TEMPLATE application.properties
$ vi application.properties
# Edit properties to your environment
```

#### Create your MySQL user and schema
If you're using MySQL, you can use the commands below to create a database
and role user for it before running tokenator for the first time:
```
create database YOUR_DB_SCHEMA_NAME;

create user 'YOUR_TOKENATOR_USERNAME'@'localhost' identified by 'YOUR_PASSWORD';
create user 'YOUR_TOKENATOR_USERNAME'@'%' identified by 'YOUR_PASSWORD';

grant all privileges ON YOUR_DB_SCHEMA_NAME.* TO 'YOUR_TOKENATOR_USERNAME'@'localhost';
grant all privileges ON YOUR_DB_SCHEMA_NAME.* TO 'YOUR_TOKENATOR_USERNAME'@'%';

flush privileges;
quit;
```

#### Build and run the project:

```
./gradlew clean bootRun
```

#### Test REST API calls with curl


This will create a new primary PAN entry.  The entry below may already exist in the database,
so adjust the PAN or experation date as needed.  The PAN can be optionally terminated with an
'X', in which case the Luhn checkdigit will be automatically calculated.

```
$ curl -X POST -H 'Content-Type: application/json' -d '{"pan": "4046460664629X", "expr": "2201"}' http://localhost:8080/api/v1/primaries/
```
Output:
```
{
  "id" : 1,
  "pan" : "4046460664629718",
  "expr" : "1801",
  "surrogates" : [ ]
}
```


Two different ways to retrieve the primary data entry created (and returned)
in the previous command:
```
$ curl -X GET http://localhost:8080/api/v1/primaries/1
$ curl -X GET http://localhost:8080/api/v1/primaries/4046460664629718/1801
```


Add a surrogate entry to the above primary data entry:
```
curl -X POST -H 'Content-Type: application/json' -d '{"pan": "98765432109876", "expr": "1702"}' http://localhost:8080/api/v1/primaries/1/surrogates/
```
Output:
```
{
  "id" : 1,
  "pan" : "4046460664629718",
  "expr" : "1801",
  "surrogates" : [ {
    "id" : 1,
    "pan" : "98765432109876",
    "expr" : "1702"
  } ]
}
```

Lookup the primary entry for the surrogate entry created above:
```
$ curl -X GET http://localhost:8080/api/v1/primaries/surrogates/98765432109876/1702
```

Output:
```
{
  "id" : 1,
  "pan" : "4046460664629718",
  "expr" : "1801",
  "surrogates" : [ {
    "id" : 1,
    "pan" : "98765432109876",
    "expr" : "1702"
  } ]
}
```
