#!/bin/bash

sum=0

# 函数用于判断一个数是否为质数
is_prime() {
    num=$1
    if [ $num -lt 2 ]; then
        return 1
    fi
    for (( i=2; i<=$num/2; i++ )); do
        if [ $((num%i)) -eq 0 ]; then
            return 1
        fi
    done
    return 0
}

# 计算1-100的所有质数之和
for (( num=0; num<=100; num++ )); do
    if is_prime $num; then
        sum=$((sum + num))
    fi
done

# 将结果重定向到日志文件
echo "$sum" > 2151133-hw1-q1.log
echo "计算结果已保存到2151133-hw1-q1.log文件中。"
