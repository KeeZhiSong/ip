#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

# delete data from previous run
if [ -e "../bin/data" ]
then
    rm -rf ../bin/data
fi

if [ -e "./data" ]
then
    rm -rf ./data
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin \
    ../src/main/java/TaskType.java \
    ../src/main/java/Task.java \
    ../src/main/java/Todo.java \
    ../src/main/java/Deadline.java \
    ../src/main/java/Event.java \
    ../src/main/java/SigmaWolfException.java \
    ../src/main/java/Storage.java \
    ../src/main/java/Ui.java \
    ../src/main/java/TaskList.java \
    ../src/main/java/Parser.java \
    ../src/main/java/SigmaWolf.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ../bin SigmaWolf < input.txt > ACTUAL.TXT

# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT 2>/dev/null

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi