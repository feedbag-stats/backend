#!/bin/bash

echo "new:"

#find added files
diff -Ewbur $1 $2 -x '$1.zip' | grep $1 | grep -E "^Only in $1" | sed -n 's/://p' | awk '{print $3"/"$4}' 

echo "removed:"

#find removed files
diff -rq  $1 $2 -x '$2.zip' | grep $2 | grep -E "^Only in $2" | sed -n 's/://p' | awk '{print $3"/"$4}'

echo "modified:"

# find modified files 
diff -rq $1 $2 | grep $1 | grep -E "^Files $1" | awk '{print $2}' 
