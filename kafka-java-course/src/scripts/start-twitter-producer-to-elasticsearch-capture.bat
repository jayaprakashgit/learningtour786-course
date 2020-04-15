#1 start zookeeper
start cmd
cd D:\App\kafka_2.12-2.1.0\bin\windows
d:
zookeeper-server-start.bat ..\..\config\zookeeper.properties

#2 start Kafka
start cmd
cd D:\App\kafka_2.12-2.1.0\bin\windows
d:
kafka-server-start.bat ..\..\config\server.properties