#!/bin/bash
echo "Choose GC (young&old): "
OPTIONS="Serial&Serial Parallel_Scavenge&Serial ParallelScavenge&Parallel_Old Serial&CMS Parallel_New&CMS"
           select opt in ${OPTIONS}; do
               if [ "$opt" = "Serial&Serial" ]; then
               ./scripts/Serial.sh
               exit
               elif [ "$opt" = "Parallel_Scavenge&Serial" ]; then
                ./scripts/Serial_parallel.sh
                exit
               elif [ "$opt" = "ParallelScavenge&Parallel_Old" ]; then
                ./scripts/Parallel.sh
                exit
               elif [ "$opt" = "Serial&CMS" ]; then
                ./scripts/Serial_CMS.sh
                exit
                elif [ "$opt" = "Parallel_New&CMS" ]; then
                ./scripts/CMS.sh
                exit
               else
                echo bad option
               fi
           done