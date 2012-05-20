@echo off
if "%OS%" == "Windows_NT" setlocal

set PRG=%0

set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

java -classpath %PRG%\..; LauncherBootstrap -verbose metix %CMD_LINE_ARGS%

:end
