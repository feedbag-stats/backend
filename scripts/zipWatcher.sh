#!/bin/bash

echo "processing..."

#find added files
# diff -rq $1 $2 -x '$1.zip' | grep $1 | grep -E "^Only in $1" | sed -n 's/://p' | awk '{print $3"/"$4}' 
# https://stackoverflow.com/questions/4997693/given-two-directory-trees-how-can-i-find-out-which-files-differ
echo "new:"
OUTPUT=$(rsync --recursive --delete --links --checksum --verbose --dry-run $1 $2)
echo "${OUTPUT}" | grep -v '^deleting.*zip' | grep '.*zip'

# #find removed files
# diff -rq  $1 $2 -x '$2.zip' | grep $2 | grep -E "^Only in $2" | sed -n 's/://p' | awk '{print $3"/"$4}'
echo "removed:"
echo "${OUTPUT}" | grep '^deleting.*zip'

# find modified files 
echo "modified:"
diff -rq $1 $2 -x '$2.zip' | grep $1 | grep -E "^Files $1" | awk '{print $2}'
