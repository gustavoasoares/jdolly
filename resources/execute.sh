#!/bin/bash

cd $2

seq_file=$1
std=$3
compiler=$4
param1=$5
param2=$6

./$seq_file

ret_val=$?

exit $ret_val
