#!/bin/sh
echo "10.184.0.2 hadoop-primary" >> /etc/hosts
echo "10.184.0.3 hadoop-secondary-1" >> /etc/hosts
echo "10.184.0.4 hadoop-secondary-2" >> /etc/hosts
echo "10.184.0.5 zookeeper-1" >> /etc/hosts
echo "10.184.0.6 zookeeper-2" >> /etc/hosts
echo "10.184.0.7 zookeeper-3" >> /etc/hosts
exec java -Dhadoop.home.dir=/app -Djava.library.path=/app -jar app.jar
