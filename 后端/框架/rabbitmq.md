安装RabbitMQ各种踩坑详细教程
	https://blog.csdn.net/weixin_43493955/article/details/83304768
史上最全的RabbitMQ教程
	https://blog.csdn.net/Especially_Allen/article/details/107614917
idea springboot启动报SLF4J:Failed to load class “org.slf4j.impl.StaticLoggerBinder”
	https://blog.csdn.net/u010696630/article/details/84991116


java -classpath ".;G:\work\rabbitmq_demo\target\classes\lib\*" com.rabbitmq.four.EmitLogDirect error "Run. Run. Or it will explode."


java -cp $CP ReceiveLogsTopic "#"
java -classpath ".;G:\work\rabbitmq_demo\target\lib\*" com.rabbitmq.five.ReceiveLogsTopic "#"
java -cp $CP ReceiveLogsTopic "kern.*"
java -classpath ".;G:\work\rabbitmq_demo\target\lib\*" com.rabbitmq.five.ReceiveLogsTopic "kern.*"
java -cp $CP ReceiveLogsTopic "*.critical"
java -classpath ".;G:\work\rabbitmq_demo\target\lib\*" com.rabbitmq.five.ReceiveLogsTopic "kern.*" "*.critical"
java -cp $CP EmitLogTopic "kern.critical" "A critical kernel error"
java -classpath ".;G:\work\rabbitmq_demo\target\lib\*" com.rabbitmq.five.EmitLogTopic "kern.critical" "A critical kernel error"