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
