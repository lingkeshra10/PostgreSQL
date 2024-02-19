#! /bin/sh
# chkconfig: 2345 20 80
#
while pgrep -u root java > /dev/null; do sleep 1; done
kill -9 1