#!/bin/bash
FUNCTION=${1}
TABLE=${2}

JSON_STRING='{"function":"'"$FUNCTION"'","table":"'"$TABLE"'"}'

curl -d $JSON_STRING -H "Content-Type: application/json" -X POST http://localhost:8090/test 

echo $FUNCTION" "$TABLE >> stop.txt

echo -e '\n'