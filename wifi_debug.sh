#!/bin/sh

FULL_IP=$1
array=(${FULL_IP//:/ })
adb connect "$FULL_IP"
adb tcpip 5555
sleep 0.5
adb connect "${array[0]}:5555"
adb disconnect "$FULL_IP"
adb devices
