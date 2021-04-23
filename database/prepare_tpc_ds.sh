#!/bin/bash

unzip ~/opening-black-box/tpc-ds-tool.zip -d ~/opening-black-box/database/tcp-ds
mkdir ~/opening-black-box/database/tpc-data
cd ~/opening-black-box/database/tpc-ds/v3.0.0/tools
make
