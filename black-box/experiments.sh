#!/bin/bash
path=$(pwd)
experiments_path='./experiments'

cd $experiments_path
rm *.png
cd $path

# declare -a functions=("filterFromMondayToThursday", "selectIdEnergyOutsideTemperatureSeason", "filterEnergyGreaterThan10", "averageTemperatureByDeviceIdSeason",
# "filterFromMondayToThursdayAndEnergyGreaterThan10", "filterFromMondayToThursdayOrEnergyGreaterThan10", "filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11",
#  "filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11", "filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason", "filterDeviceId5019AndAutumnOrFriday",
#  "filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12", "filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12", "filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026")

declare -a functions=("filterFromMondayToThursday", "selectIdEnergyOutsideTemperatureSeason", "averageTemperatureByDeviceIdSeason",
"filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason", "filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12")

declare -a tables=("test_input_one_eighth", "test_input_one_quarter", "test_input_one_half")

# get length of an array
functionslength=${#functions[@]}
tableslength=${#tables[@]}

for (( i=1; i<${functionslength}+1; i++ ));
do
    f=$(echo ${functions[$i-1]} | sed "s/,/ /g")
    rm -r $f
done

for (( i=1; i<${functionslength}+1; i++ ));
do
    f=$(echo ${functions[$i-1]} | sed "s/,/ /g")

    mkdir $f
    cd $f

    for (( j=1; j<${tableslength}+1; j++ ));    
    do 

        t=$(echo ${tables[$j-1]} | sed "s/,/ /g")
        mkdir $t

        cd $path

        cd time-metrics
        stats_file=$(echo "stats_"$t"_"$f".csv" | tr -d ' ')
        exec_time=$(cat $stats_file | tail -n 1 | tr ',' ' ' | awk '{print $2}')
        max_exec_time=$(echo 20 $exec_time | awk '{printf "%4.0f\n",$1*$2}' | tr -d ' ')
        echo $max_exec_time
        cd ..
       
        echo $f $t
        ./metrics.sh $f $t $max_exec_time
        cd $f

    done
    cd $path
done
