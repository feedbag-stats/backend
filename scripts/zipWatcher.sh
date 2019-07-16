#!/bin/bash

echo "\nnew:"

#find all files of first argument input and put it into a temp file
find $1 | sort | cut -d "/" -f-2,3,4,5,6- > temp.txt

# find all added files in argument 1 compared to argument 2
grep -v -F -x -f $2 temp.txt | grep ".*zip"

# other alternatives which did not precicly work 
# diff -rq $1 $2 -x '$1.zip' | grep $1 | grep -E "^Only in $1" | sed -n 's/://p' | awk '{print $3"/"$4}'
# OUTPUT=$(rsync --recursive --delete --links --checksum --verbose --dry-run $1 $2)
# echo "${OUTPUT}" | grep -v '^deleting.*zip' | grep '.*zip'
#comm -2 temp.txt $2  


echo "\nremoved:"

# find all removed files in argument 1 compared to argument 2
grep -v -F -x -f temp.txt $2 | grep ".*zip"

# put content of temp file of new context in the file with the old context, because after the check it is considered as old.
cat temp.txt > $2

# remove temp file
rm temp.txt

# other alternatives which did not precicly work 
# diff -rq  $1 $2 -x '$2.zip' | grep $2 | grep -E "^Only in $2" | sed -n 's/://p' | awk '{print $3"/"$4}'
# echo "${OUTPUT}" | grep '^deleting.*zip' | grep ' .*zip' | awk '{print $2}'
#comm -1 temp.txt $2 | grep ".*zip"





# find only modified files 
# echo "\nmodified:"
# diff -rq $1 $2 -x '$2.zip' | grep $1 | grep -E "^Files $1" | awk '{print $2}'

