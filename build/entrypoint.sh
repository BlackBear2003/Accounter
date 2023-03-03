#!/bin/bash

nohup java -jar /eureka.jar > eureka.log &
nohup java -jar /gateway.jar > gateway.log &
nohup java -jar /auth.jar > auth.log &
nohup java -jar /api.jar > api.log &
nohup java -jar /file.jar > file.log &
nohup java -jar /ti.jar > ti.log &

# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
#需要写死循环吗？