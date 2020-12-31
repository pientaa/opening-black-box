#!/bin/bash

declare -a functions=("filterFromMondayToThursday", "selectIdEnergyOutsideTemperatureSeason", "filterEnergyGreaterThan10", "averageTemperatureByDeviceIdSeason",
"filterFromMondayToThursdayAndEnergyGreaterThan10", "filterFromMondayToThursdayOrEnergyGreaterThan10", "filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11",
 "filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11", "filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason", "filterDeviceId5019AndAutumnOrFriday",
 "filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12", "filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12", "filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026")

# declare -a functions=("filterFromMondayToThursday", "selectIdEnergyOutsideTemperatureSeason")

declare -a tables=("test_input_one_eighth", "test_input_one_quarter", "test_input_one_half")

mkdir time-metrics
cd time-metrics
rm *.csv
cd .. 

# get length of an array
functionslength=${#functions[@]}
tableslength=${#tables[@]}

for (( i=1; i<${functionslength}+1; i++ ));
do
    f=$(echo ${functions[$i-1]} | sed "s/,/ /g")

    for (( j=1; j<${tableslength}+1; j++ ));    
    do 

        t=$(echo ${tables[$j-1]} | sed "s/,/ /g")
        filename=$(echo $t"_"$f".csv" | tr -d ' ')
        echo $filename

        echo $f $t

        for (( k=1; k<31; k++ ));    
        do

            START=$(date +%s.%N)    
            ./curl.sh $f $t
            END=$(date +%s.%N)

            DIFF=$(echo "$END - $START" | bc)
            cd time-metrics
            echo ${k}','${DIFF} >>"${filename}"
            cd ..

            echo $DIFF
        done

        cd time-metrics
        python3.6 time-statictics.py $f $t
        cd ..
    done
done