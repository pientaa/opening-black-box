#!/bin/bash

unzip tcp-ds-tool.zip -d database/tcp-ds
mkdir database/tpc-data
cd database/tpc-ds/v3.0.0/tools
make
