#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 [seconds]"
  exit 1
fi

seconds=$1

while [ $seconds -gt 0 ]; do
  echo "Countdown: $seconds seconds remaining"
  sleep 10
  seconds=$((seconds - 10))
done
