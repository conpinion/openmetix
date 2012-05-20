#!/bin/bash

export LC_ALL=de_DE

SCRIPTPATH=`dirname $0`

java -jar $SCRIPTPATH/../lib/hsqldb.jar --rcfile $SCRIPTPATH/../config/hsqldb.rc metix $SCRIPTPATH/metix-hsqldb.sql
