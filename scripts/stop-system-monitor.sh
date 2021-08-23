#!/bin/bash

kill -9 $(ps -aux | grep system-monitor | awk '{print $2}')