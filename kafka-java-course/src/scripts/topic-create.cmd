REM kafka-topics.bat --create --zookeeper localhost:2181 --topic nse-eod-topic --partitions 5 --replication-factor 3

REM create firsttopic with partition:1 and replication-factor:1
D:\App\kafka_2.12-2.1.0\bin\windows\kafka-topics.bat --zookeeper 127.0.0.1:2181 --create --topic firsttopic --partitions 1 --replication-factor 1

REM create twitter_topic with partition:1 and replication-factor:1
REM D:\App\kafka_2.12-2.1.0\bin\windows\kafka-topics.bat --zookeeper 127.0.0.1:2181 --create --topic twitter_topic --partitions 1 --replication-factor 1