##一.linux安装mysql-5.7.18
	https://www.linuxidc.com/Linux/2018-08/153595.htm
	https://www.linuxidc.com/Linux/2018-08/153595.htm
	#tar -xvf mysql-5.7.18-1.el7.x86_64.rpm-bundle.tar
	https://downloads.mysql.com/archives/community/
	重启命令 https://www.jianshu.com/p/5e29228ce897
	service mysqld restart
	systemctl restart mysqld.service
	vim命令 https://blog.csdn.net/qq_32281471/article/details/91350744

##二.linux安装node.js-10.16.0
	https://www.cnblogs.com/zhi-leaf/p/10979629.html
	单用户模式下(文件可读但也可以修改): mount -o remount,rw /
	chmod 777 /etc/profile
	需要安装全局命令 npm install express -g --registry=https://registry.npm.taobao.org
	还需要当前运行目录下面添加环境 npm  install express --registry=https://registry.npm.taobao.org

##三.cenos7安装jdk1.8
	https://blog.csdn.net/dhr201499/article/details/81626466
	卸载openJdk
	https://blog.csdn.net/love3765/article/details/88783126
	export JRE_HOME=${JAVA_HOME}/jre
	export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib

##四.linux运行jar
	端口
		https://blog.csdn.net/zzz19920821/article/details/84990830
	maven打包 
		-mvn clean package  -Dmaven.skip.test=true
	增加/Log4j2-Log/springboot.log-个人项目原因
	修改hosts 文件-个人项目原因
	重启服务 
		/etc/init.d/network restart
	centos后台运行jar包
		符号意义: & 表示后台运行,nohup 表示不挂断运行命令行
		日志不输出
			nohup java -jar TianlanEureka7001.jar >/dev/null 2>&1 &
			nohup java -jar TianLanPortalRegister.jar > PortalRegister.log >/dev/null 2>&1 &
		日志只是输出错误的
			nohup java -jar TianLanPortalRegister.jar > PortalRegister.log >/dev/null 2>log &
		日志输出全部输出
			nohup java -jar TianLanPortalRegister.jar > TianLanPortalRegister.log 2>&1 &
	centos前台运行node项目
		nohup node /home/quan/ClientSystem/tlblog/server > /home/quan/ClientSystem/tlblog/tlblog.log 2>&1 &
		nohup npm run dev tlblog-admin.log 2>&1 &
		npm install --registry=https://registry.npm.taobao.org
		问题根本:这通常是因为运行NPM安装后环境发生了变化。运行“NPM重建节点SASS——强制”来为当前环境构建绑定。
		npm rebuild node-sass
		npm run dev
	停止项目
		通过ps aux命令查看进程号PID，然后执行  kill -9 PID
		如果是前台进程的话，直接执行 Ctrl+c 就可以终止了，也可以查看node运行项目 ps -ef|grep node
	tail -f PortalRegister.log查看日志
##五.安装nginx
docker容器	
	docker容器与虚拟机有什么区别？
	https://www.zhihu.com/question/48174633
jetty的方法
	https://blog.csdn.net/lzc4869/article/details/79817511
##运行便捷命令
	cd ServerSystem/TianlanEureka7001 nohup java -jar TianlanEureka7001.jar >/dev/null 2>&1 &
	cd ServerSystem/TianlanEureka7002 nohup java -jar TianlanEureka7002.jar >/dev/null 2>&1 &
	cd ServerSystem/TianlanEureka7003 nohup java -jar TianlanEureka7003.jar >/dev/null 2>&1 &
	cd /home/quan/ServerSystem/TianlanPortalRegister nohup java -jar TianLanPortalRegister.jar > TianLanPortalRegister.log >/dev/null 2>log &
	cd /home/quan/ClientSystem/tlblog nohup node server.js >tlblog.log 2>&1 &
	cd /home/quan/ClientSystem/tlblog-admin 

linux下shell脚本执行jar文件
	https://www.cnblogs.com/jpfss/p/10794081.html
linux下crontab定时执行java程序，java程序不执行的问题
	缺少权限
	https://blog.csdn.net/sc313121000/article/details/42462211?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.not_use_machine_learn_pai&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.not_use_machine_learn_pai
	https://blog.csdn.net/u011418530/article/details/83104193
Centos查看端口占用和开启端口命令
	https://www.cnblogs.com/gavin-yao/p/10505619.html
20 * * * * /home/quan/ServerSystem/ddns6000/ddns.sh restart
/usr/local/jdk1.8.0_271/bin
dos2unix TianlanEureka.sh

部署路途：
titleId为空要判断一下
日期前端显示格式不对
	


