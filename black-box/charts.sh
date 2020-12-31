#!/bin/bash
path=$(pwd)


declare -a functions=("filterFromMondayToThursday", "selectIdEnergyOutsideTemperatureSeason", "averageTemperatureByDeviceIdSeason",
"filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason", "filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12")

declare -a tables=("test_input_one_eighth", "test_input_one_quarter", "test_input_one_half")

# get length of an array
functionslength=${#functions[@]}
tableslength=${#tables[@]}

for (( i=1; i<${functionslength}+1; i++ ));
do
    f=$(echo ${functions[$i-1]} | sed "s/,/ /g")

    for (( j=1; j<${tableslength}+1; j++ ));    
    do 
        t=$(echo ${tables[$j-1]} | sed "s/,/ /g")
        cd $f
        cd $t 

        rm *.png
        cd $path

        python3.6 resources-statistics.py $f $t
    done

done