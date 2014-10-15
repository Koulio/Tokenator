Tokenator
=========

The Open Tokenizer Project

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

To build and run the project use:

```
./gradlew clean bootRun
```

Curl examples using the API calls:

```
$ curl -X POST -H 'Content-Type: application/json' -d '{"pan": "4046460664629718", "expr": "1801"}' \
    http://localhost:8080/api/v1/primaries/
{
  "id" : 1,
  "pan" : "4046460664629718",
  "expr" : "1801",
  "surrogates" : [ ]
}

#
#  Two different ways to retrieve the primary data entry created (and returned)
#  in the previous command:
#
$ curl -X GET http://localhost:8080/api/v1/primaries/1
$ curl -X GET http://localhost:8080/api/v1/primaries/4046460664629718/1801

#
#  Add a surrogate entry to the above primary data entry:
#
curl -X POST -H 'Content-Type: application/json' -d '{"pan": "98765432109876", "expr": "1702"}' \
    http://localhost:8080/api/v1/primaries/1/surrogates/
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

#
#  Lookup the primary entry for the surrogate entry created above:
#
$ curl -X GET http://localhost:8080/api/v1/primaries/surrogates/98765432109876/1702
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


