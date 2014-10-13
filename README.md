Tokenator
=========

The Open Tokenizer Project


To build and run the project use:

```
./gradlew clean bootRun
```

Here are some examples feeding the stub URLs data:

```
$ curl -X POST -d "pan=0123456789" http://127.0.0.1:8080/tokenize ; echo
{"surrogatePan":"1234567890"}

$ curl -X POST -d "pan=1234567890" http://127.0.0.1:8080/detokenize ; echo
{"pan":"0123456789"}
```

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
