#!/bin/bash

cd $2

seq_file=$1
std=$3
compiler=$4
param1=$5
param2=$6

#gcc *.c
#clang -Wall -fprofile-arcs -ftest-coverage *.c TIREI o -fprofile-arcs pois estah dando
#erro para retorno de tipo double, long e float. Investigar
# removido clang porque no linux não existe. Era assim: clang -Wall -std=$3 -ftest-coverage *.c 
$compiler -Wall $param1 $param2 -std=$std *.c

./$seq_file

ret_val=$?

exit $ret_val
