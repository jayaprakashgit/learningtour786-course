REM kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic nse-eod-topic --from-beginning

REM this console consumer poll msg from beginning from firsttopic
D:\App\kafka_2.12-2.1.0\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic firsttopic --from-beginning

REM this console consumer poll msg from beginning from twitter_topic
REM D:\App\kafka_2.12-2.1.0\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic twitter_topic --from-beginning