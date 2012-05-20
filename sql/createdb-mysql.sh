#!/bin/bash

SCRIPTPATH=`dirname $0`

mysql -u root -p < $SCRIPTPATH/metix-mysql.sql
