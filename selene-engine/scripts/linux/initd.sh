#!/bin/bash
### BEGIN INIT INFO
# Provides:          selene
# Required-Start:    $local_fs $network
# Required-Stop:     $local_fs
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: selene
# Description:       selene
### END INIT INFO

case $1 in
    start)
        /bin/bash /opt/selene/bin/start.sh
    ;;
    stop)
        /bin/bash /opt/selene/bin/stop.sh
    ;;
    restart)
        /bin/bash /opt/selene/bin/stop.sh
        /bin/bash /opt/selene/bin/start.sh
    ;;
esac
exit 0
