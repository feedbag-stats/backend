#!/bin/bash

# https://stackoverflow.com/questions/4997693/given-two-directory-trees-how-can-i-find-out-which-files-differ

echo "processing..."

#find added files (modified files included)
OUTPUT=$(rsync --recursive --delete --links --checksum --verbose --dry-run $1 $2)
echo "\nnew:"
echo "${OUTPUT}" | grep -v '^deleting.*zip' | grep '.*zip'

# other alternative which does not always show all zip files
# diff -rq $1 $2 -x '$1.zip' | grep $1 | grep -E "^Only in $1" | sed -n 's/://p' | awk '{print $3"/"$4}' 

# #find removed files
echo "\nremoved:"
echo "${OUTPUT}" | grep '^deleting.*zip' | grep ' .*zip' | awk '{print $2}'

# other alternative which does not always show all zip files
# diff -rq  $1 $2 -x '$2.zip' | grep $2 | grep -E "^Only in $2" | sed -n 's/://p' | awk '{print $3"/"$4}'

# find only modified files 
# echo "\nmodified:"
# diff -rq $1 $2 -x '$2.zip' | grep $1 | grep -E "^Files $1" | awk '{print $2}'
