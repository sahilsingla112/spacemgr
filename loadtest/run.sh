#!/bin/bash

# current directory is $projectdir/loadtest

cpath=`pwd`
cd $cpath/bak/
pwd
numfiles=${1:-14}
./copydata.sh ${numfiles}

cd $cpath/..
./loadtest/loadtest.sh

sleeptime=${2:-35}

#printf "will copy another batch in next %s sec...." "${sleeptime}"

#sleep ${sleeptime}

#cd $cpath/loadtest/bak/
#./copydata.sh ${numfiles}








