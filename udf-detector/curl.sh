#!/bin/bash
FUNCTION=${1}
TABLE=${2}

JSON_STRING='{"function":"'"$FUNCTION"'","table":"'"$TABLE"'"}'

curl -d $JSON_STRING -H "Content-Type: application/json" -X POST http://localhost:8090/detectUDF 

echo -e '\n'