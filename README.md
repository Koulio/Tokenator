# Tokenator

The Open Tokenizer Project

### Building and running for the first time

#### Check out the source code
```
$ git clone https://github.com/SimplyTapp/Tokenator
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

#### Build and run the project
```
./gradlew clean bootRun
```

#### Test REST API calls with curl

###### Setup
Even with JSON pretty printing enabled in your application.properties, there will
not be a newline at the end of the HTTP body.  Put this line in your .curlrc for
prettier output:
```
$ echo '-w "\n"' >> ~/.curlrc
```

###### Create a Primary entry
The PAN below may already exist in the database, so adjust the number or
experation (YYMM) as needed.  You can optionally terminate the PAN with an 'X'
and the Luhn checkdigit will be automatically calculated and appended.

```
$ curl -X POST -H 'Content-Type: application/json' -d '{"pan": "4046460664629X", "expr": "2201"}' http://localhost:8080/api/v1/primaries/
```
###### Output of Primary Entry Creation
```
{
  "id" : 9,
  "pan" : "40464606646297",
  "expr" : "2201",
  "surrogates" : [ ]
}
```

###### Retrieve a Primary entry
By ID (in this example using ID=9):
```
$ curl -X GET http://localhost:8080/api/v1/primaries/1
```
By PAN and expiration (YYMM):
```
$ curl -X GET http://localhost:8080/api/v1/primaries/40464606646297/2201
```
Later we'll also show how to retrieve a primary entry by one of its surrogate
entries.

###### Add a Surrogate PAN
Again, adjust the PAN as needed.  If you don't want to calculate a valid Luhn
check digit, append an 'X' to the end of the PAN.  The command below attaches
the Surrogate entry to the Primary entry with ID=9

```
curl -X POST -H 'Content-Type: application/json' -d '{"pan": "9876543210987X", "expr": "1801"}' http://localhost:8080/api/v1/primaries/9/surrogates/
```
###### Surrogate Creation Returns the full Primary Entry
```
{
  "id" : 9,
  "pan" : "40464606646297",
  "expr" : "2201",
  "surrogates" : [ {
    "id" : 5,
    "pan" : "98765432109875",
    "expr" : "1801"
  } ]
}
```

###### Lookup the Primary PAN for a Surrogate:
This method uses the surrogate PAN and expiration (YYMM) date:
```
$ curl -X GET http://localhost:8080/api/v1/primaries/surrogates/98765432109875/1801
```

###### Delete a Surrogate Entry
The command below deletes the surrogate ID=5.  The ID values of surrogates are
separate from primary ID values, but globaly unique between surrogates.
````
$ curl -X DELETE http://localhost:8080/api/v1/surrogates/5
```

###### Delete a Primary Entry
Delete the primary entry with ID=9.
````
$ curl -X DELETE http://localhost:8080/api/v1/primaries/9
````
###### Notes:
Successful HTTP Response Codes:
* 200 (OK) -- successfull lookup
* 201 (Created) -- successfull creation
* 204 (No Content) -- successfull deletions (response body is empty)

Error HTTP Response Codes:
* 404 (Not Found)
* 400 (Bad Request)
