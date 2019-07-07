#!/bin/bash

pid=`ps aux | grep DseleneConfig | grep -v grep | awk '{print $2}'`
kill $pid
