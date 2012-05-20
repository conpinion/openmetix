#!/bin/sh

PLATTFORM=`uname`

if [ "$PLATTFORM" == "Darwin" ]; then

  CP="."
  if [ -e "./bin" ]; then
    CP="./bin"
  fi
 
  java -classpath "$CP" LauncherBootstrap -verbose metix-client "$@"

else

  PRG="$0"

  while [ -h "$PRG" ]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '.*/.*' > /dev/null; then
      PRG="$link"
    else
      PRG=`dirname "$PRG"`/"$link"
    fi
  done

  PRGDIR=`dirname "$PRG"`
  if [ -r "$PRGDIR"/settings.sh ]; then
    . "$PRGDIR"/settings.sh
  fi

  if [ -e "$JAVA_HOME"/bin/java ]; then
    JAVA="$JAVA_HOME"/bin/java
  else
    JAVA=java
  fi

  exec "$JAVA" -classpath "$PRGDIR" LauncherBootstrap -verbose metix-client "$@"

fi

