#!/bin/bash
FUNCTION=${1:-'filterFromMondayToThursday'}
TABLE=${2:-'test_input_1000'}
# ITERATION=${3:-1}
TIME=${3:-600}
PID=$(docker top black-box | awk '{print $2}' | tail -n 1)
path=$(pwd)
COUNTER=0
TIME_BUFF=$TIME

rm *.csv
rm stop.txt
touch stop.txt
cd $FUNCTION
cd $TABLE

for (( i=1; i<31; i++ ));    
  do 

    echo "Execution started.. ${i}"
    filename="metrics_${i}.csv"
    top -p $PID -b -n 3 | sed -n '7,1p' | awk '{print "Timestamp \t"$9" \t"$10}' >>$filename


    while [ $TIME -ge 0 ]; do
      echo "Running.."
# top -S -d 0,1 -p $PID -b -n 1 | sed -n '8, 12p' | awk '{print " \t"$9" \t"$10}' | head -n 1 | awk '{printf("%0.f"$0,'$(date +%s%3N)')}'

      top -S -d 0,1 -p $PID -b -n 1 | sed -n '8, 12p' | awk '{print " \t"$9" \t"$10}' | head -n 1 | awk '{printf("%0.f"$0"\n",'$(date +%s%3N)')}' >>$filename

      if [ $COUNTER -gt 5 ]; then
        if [[ "$stop" == "$FUNCTION $TABLE" ]]; then
          TIME=-1
        fi
      fi  

      COUNTER=$(($COUNTER+1))

      cd $path
      if [ $COUNTER -eq 5 ]; then
        rm stop.txt
        touch stop.txt
        echo -e 'Running curl with parameters:'
        echo 'Function: '"$FUNCTION"
        echo 'Table: '"$TABLE"
        nohup ./curl.sh $FUNCTION $TABLE &
      fi

      stop=$(cat stop.txt | tail -n 1)
      echo $stop

      cd $FUNCTION
      cd $TABLE
    done  
    TIME=$TIME_BUFF
    COUNTER=0
done
cd $path
python3.6 resources-statistics.py $FUNCTION $TABLE
# python3.6 cpu.py $FUNCTION $TABLE $(pwd)/experiments/ & python3.6 memory.py $FUNCTION $TABLE $(pwd)/experiments/ && fg