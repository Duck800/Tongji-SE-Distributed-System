#!/bin/bash

log_file="2151133-hw1-q2.log"
counter=0

# 每隔10秒钟执行uptime命令，并将结果追加到日志文件
while [ $counter -lt 15 ]; do
    uptime >> $log_file
    counter=$((counter + 1))
    sleep 10
done

echo "已将uptime命令的输出结果追加到$log_file文件。"
