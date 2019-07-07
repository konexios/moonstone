#!/bin/bash 
function sysstat {
echo -e "
Hostname:`hostname`
Kernel Version:`uname -r`
Uptime:`uptime | sed 's/.*up \([^,]*\), .*/\1/'`
Last Reboot Time:`who -b | awk '{print $3,$4}'`
"
MPSTAT=`which mpstat`
MPSTAT=$?
cpus=`lscpu | grep -e "^CPU(s):" | cut -f2 -d: | awk '{print $1}'`
i=0
while [ $i -lt $cpus ]
do
	echo "CPU$i:`mpstat -P ALL | awk -v var=$i '{ if ($3 == var ) print $4 }' `"
	let i=$i+1
done

echo -e "Load Average:`uptime | awk -F'load average: ' '{ print $2 }' | cut -f1 -d,`"
echo -e "
=> Top memory using processs/application

PID %MEM RSS COMMAND
`ps aux | awk '{print $2, $4, $6, $11}' | sort -k3rn | head -n 10`

=> Top CPU using process/application
`top b -n1 | head -17 | tail -11`
"
df -Pkh | grep -v 'Filesystem' > /tmp/df.status
while read DISK
do
	LINE=`echo $DISK | awk '{print $6,"| used",$5,"free space",$4}'`
	echo -e $LINE 
done < /tmp/df.status
TOTALMEM=`free -m | head -2 | tail -1| awk '{print $2}'`
TOTALBC=`echo "scale=2;if($TOTALMEM<1024 && $TOTALMEM > 0) print 0;$TOTALMEM/1024"| bc -l`
USEDMEM=`free -m | head -2 | tail -1| awk '{print $3}'`
USEDBC=`echo "scale=2;if($USEDMEM<1024 && $USEDMEM > 0) print 0;$USEDMEM/1024"|bc -l`
FREEMEM=`free -m | head -2 | tail -1| awk '{print $4}'`
FREEBC=`echo "scale=2;if($FREEMEM<1024 && $FREEMEM > 0) print 0;$FREEMEM/1024"|bc -l`
TOTALSWAP=`free -m | tail -1| awk '{print $2}'`
TOTALSBC=`echo "scale=2;if($TOTALSWAP<1024 && $TOTALSWAP > 0) print 0;$TOTALSWAP/1024"| bc -l`
USEDSWAP=`free -m | tail -1| awk '{print $3}'`
USEDSBC=`echo "scale=2;if($USEDSWAP<1024 && $USEDSWAP > 0) print 0;$USEDSWAP/1024"|bc -l`
FREESWAP=`free -m |  tail -1| awk '{print $4}'`
FREESBC=`echo "scale=2;if($FREESWAP<1024 && $FREESWAP > 0) print 0;$FREESWAP/1024"|bc -l`

echo -e "
Total physical memory:${TOTALBC}
Used physical memory:${USEDBC}
Free physical memory:${FREEBC}
Percent of free physical memory:$(($FREEMEM * 100 / $TOTALMEM))%
Total swap memory:${TOTALSBC}
Used swap memory:${USEDSBC}
Free swap memory:${FREESBC}
Percent of free swap memory:$(($FREESWAP * 100 / $TOTALSWAP))%
"
}
sysstat

