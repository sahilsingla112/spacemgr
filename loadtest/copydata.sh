#!/bin/bash

rm *.jar
cp -p ../test*.jar .



count=${1:-14}
base=2
let numfiles=(${base}**${count})
echo "Going to produce $numfiles files"

foo=abcdefghijklmnopqrstuv
for (( i=0; i<${count}; i++ )); do
  echo "${foo:$i:1}"
 for f in *.jar; do
   cp -p "$f"  "${foo:$i:1}${f#test}"
 done

done

touch doc.txt

ls | wc -l

#for f in *.jar; do
#   cp -- "$f"  "b${f#test}"
#done
