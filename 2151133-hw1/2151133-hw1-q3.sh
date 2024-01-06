#!/bin/bash

# 获取输入的日志文件名作为参数
log_file="2151133-hw1-q2.log"

# 计算文件总行数
total_lines=$(wc -l < "$log_file")

# 计算文件总字符数
total_chars=$(wc -m < "$log_file")

# 获取第一行和最后一行的时间戳
first_timestamp=$(head -n 1 "$log_file" | grep -oE "[0-9]{2}:[0-9]{2}:[0-9]{2}")
last_timestamp=$(tail -n 1 "$log_file" | grep -oE "[0-9]{2}:[0-9]{2}:[0-9]{2}")

# 计算时间差
time_diff=$(date -ud "@$(($(date -d "$last_timestamp" +%s)-$(date -d "$first_timestamp" +%s)))" +"%H:%M:%S")

load1_sum=0
load5_sum=0
load15_sum=0

#逐行获取每列的值
while IFS= read -r line; do
    load1=$(echo "$line" | awk '{print $(NF-2)}')
    load5=$(echo "$line" | awk '{print $(NF-1)}')
    load15=$(echo "$line" | awk '{print $NF}')

    load1_sum=$(echo "$load1_sum + $load1" | awk '{printf "%.2f\n", $0}')
    load5_sum=$(echo "$load5_sum + $load5" | awk '{printf "%.2f\n", $0}')
    load15_sum=$(echo "$load15_sum + $load15" | awk '{printf "%.2f\n", $0}')

done < "$log_file"

load1_avg=$(echo "$load1_sum / $count" | awk '{printf "%.2f\n", $0}')
load5_avg=$(echo "$load5_sum / $count" | awk '{printf "%.2f\n", $0}')
load15_avg=$(echo "$load15_sum / $count" | awk '{printf "%.2f\n", $0}')

load_avg="$load1_avg $load5_avg $load15_avg"

# 输出到日志文件
echo "a) 文件 \"$log_file\" 的总行数：$total_lines" >> 2151133-hw1-q3.log
echo "b) 文件 \"$log_file\" 的总字符数：$total_chars" >> 2151133-hw1-q3.log
echo "c) 第一行和最后一行输出结果的时间差：$time_diff" >> 2151133-hw1-q3.log
echo "d) 最后三列平均值：$load_avg" >> 2151133-hw1-q3.log
