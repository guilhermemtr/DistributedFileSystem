#!/bin/bash
wsimport -d bin -s src -p wsClient http://"$1"/"$2"?wsdl;
