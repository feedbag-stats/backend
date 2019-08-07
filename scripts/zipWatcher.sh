#!/bin/bash

# usage: zipWatcher.sh

JAR="/path/to/jar"
ZIP_FOLDER="zips"
SAVE_FILE="list.txt"

if [ ! -f $SAVE_FILE ]; then
    touch $SAVE_FILE
fi

#run only a single instance
LOCK="/tmp/zipWatcher.lock"
if [ -f $LOCK ]; then
    echo "Already running!"
    exit 0
fi
touch $LOCK

PROCESSOR="java -jar ${JAR}"
LOCATION="$(pwd)"

echo "new:"

#find all files of first argument input and put it into a temp file
find $ZIP_FOLDER | sort | cut -d "/" -f-2,3,4,5,6- > temp.txt

# find all added files in argument 1 compared to argument 2
diff --new-line-format="" --unchanged-line-format="" <(sort temp.txt) <(sort $SAVE_FILE) | grep ".*zip" | while read -r ZIP ; do
# diff -a --suppress-common-lines -y $2 temp.txt | grep ".*zip" | while read -r ZIP ; do
    #for each line
    echo "$PROCESSOR add $LOCATION/$ZIP"
done

echo "removed:"

echo "$PROCESSOR remove"

# put content of temp file of new context in the file with the old context, because after the check it is considered as old.
cat temp.txt > $SAVE_FILE

# remove temp file
rm temp.txt
rm $LOCK