@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM delete data from previous run
if exist ..\bin\data rd /s /q ..\bin\data
if exist data rd /s /q data

REM compile the code into the bin folder
javac -cp ..\src\main\java -Xlint:none -d ..\bin ^
    ..\src\main\java\sigmawolf\task\TaskType.java ^
    ..\src\main\java\sigmawolf\task\Task.java ^
    ..\src\main\java\sigmawolf\task\Todo.java ^
    ..\src\main\java\sigmawolf\task\Deadline.java ^
    ..\src\main\java\sigmawolf\task\Event.java ^
    ..\src\main\java\sigmawolf\task\TaskList.java ^
    ..\src\main\java\sigmawolf\exception\SigmaWolfException.java ^
    ..\src\main\java\sigmawolf\storage\Storage.java ^
    ..\src\main\java\sigmawolf\ui\Ui.java ^
    ..\src\main\java\sigmawolf\parser\Parser.java ^
    ..\src\main\java\sigmawolf\SigmaWolf.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin sigmawolf.SigmaWolf < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
