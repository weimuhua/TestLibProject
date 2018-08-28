#!/bin/sh
./gradlew iD && adb shell am start -n baidu.com.testlibproject/baidu.com.testlibproject.MainActivity