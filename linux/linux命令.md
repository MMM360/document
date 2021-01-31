#其他
	一. init 0 init1 init 3 init 5 init 6 启动级别 一般用init 3居多
		运行级别0：系统停机状态，系统默认运行级别不能设为0，否则不能正常启动 
		运行级别1：单用户工作状态，root权限，用于系统维护，禁止远程登陆 
		运行级别2：多用户状态(没有NFS) 
		运行级别3：完全的多用户状态(有NFS)，登陆后进入控制台命令行模式 
		运行级别4：系统未使用，保留 
		运行级别5：X11控制台，登陆后进入图形GUI模式 
		运行级别6：系统正常关闭并重启，默认运行级别不能设为6，否则不能正常启动
	二. X Window
		启动窗口 startx
		来回切换 init 3/Ctrl+Alt+F1  -  init 5/startx

	三. 防火墙
		查看 systemctl status firewalld.service
		关闭-开机失效 systemctl stop firewalld.service
		开启-开机失效 systemctl start firewalld.service
		永远关闭-需要重启生效 systemctl disable firewalld.service
		在开机时启用一个服务  systemctl enable firewalld.service
	
	四. 查看ip ifconfig 外网 curl ifconfig.me
		1.查询笔记本cpu,几核，内存，硬盘块数，查看机器所有硬件信息，任务管理器
			more /proc/cpuinfo |grep -i model
			cat /proc/cpuinfo |grep "cores"|uniq
			free -m
			cat /proc/scsi/scsi
			dmesg |more
			top    详细命令-https://www.cnblogs.com/zhoug2020/p/6336453.html
			查看cpu温度 cat /sys/class/thermal/thermal_zone0/temp
		查看存储结构 df -h
		Linux查看CPU和内存使用情况 top https://www.cnblogs.com/mengchunchen/p/9669704.html
#第四章 首次登陆与在线求助
	1. 日历
		日期时间 date
		年月 cal 2019
		某年指定月份 cal 10 2019
	2. 计算器
		bc 推出 quit
		值显示小数点后多少位 scale=3
	3. 在线求助 man page 与 info page
		输入未完整的命令+两下tab
		date --help
		man date
	4. 关机
		将数据同步写入硬盘中 sync
		常用关机 shutdown now
		重新启动，关机 reboot，halt，poweroff
		都基于 systemctl [命令] 
			halt 进入系统停止模式，屏幕
	5. 中断目前程序
		【ctrl】-C
		演示find/

#第五章Linux文件权限与目录配置
###创建用户删除用户
	su、sudo、su - root的区别:https://www.cnblogs.com/amyzhu/p/8526074.html
	tlblog163
	useradd testuser  创建用户testuser
	passwd testuser  给已创建的用户testuser设置密码
	说明：新创建的用户会在/home下创建一个用户目录testuser
	usermod --help  修改用户这个命令的相关参数
	userdel testuser  删除用户testuser
	rm -rf testuser  删除用户testuser所在目录
	Linux的chattr与lsattr命令详解 http://www.ha97.com/5172.html
	PS：有时候你发现用root权限都不能修改某个文件，大部分原因是曾经用chattr命令锁定该文件了。chattr命令的作用很大，其中一些功能是由Linux内核版本来支持的，不过现在生产绝大部分跑的linux系统都是2.6以上内核了。通过chattr命令修改属性能够提高系统的安全性，但是它并不适合所有的目录。chattr命令不能保护/、/dev、/tmp、/var目录。lsattr命令是显示chattr命令设置的文件属性。
	查询全部文件
			ls -al
	切换管理员/离开
		   su
		   su-
	权限
	文件属性/权限/各个权限数字对应表
			 例 -rwx r-x r--
			 第一组：文件拥有者可具备的权限
			 第二组：加入用户组之账号的权限
			 第三组：非本人且没有加入用户组的其他账号权限
			 r：可读：4
		     w：可写：2
			 x：可执行：1

     进入根目录 cd / cd

	 查询权限
		    ls -l
		    ll 指定文件或文件  
	 
	 修改文件属性与权限
					 chgrp：修改文件所属用户组
					 chown：修改文件拥有者
					 chmod：修改文件的权限，SUID,SGID,SBIT等特性。
	 			   例：
					  [R]:进行递归修改，亦即连同子目录下的所有文件，目录
					  chgrp [R] dirname/finenam  如：chgrp users initial-setup-ks.cfg
					  chown [R] 账号名称 文件或目录/账号名称：用户组名称 文件或目录  如：chown bin initial-setup-ks.cfg/chown root:root initial-setup-ks.cfg
					  数字类型修改
								各个权限数字对应表： r:4，w:2，x:1：基本权限有九个，拥有者(owner),所属群组(group),其他人(others)
								chmod [R] xyz 文件夹或者目录 注：xyz-代表各自[-rwxrwxrwx] 9个权限是三个三个一组
								如：chmod 777 .bashrc
					  符号类型修改
								chmod u/g/o/a +(加入)/-(移除)/=(设置) r/w/x 文件或者目录
							    例：chomd u=rwx go=rx .bashrc

#第六章 linux文件与目录管理
	相对路径和绝对路径
					绝对路径：一定由根目录/写起：/user/share/doc
					相对路径：不是由/写起,对于目前工作路径：cd ../man
	特殊目录
			. 代表此层目录
			.. 上一层目录
			- 前一个工作目录
			~ 目前使用者身份所在的家目录
			~account account这个使用这的家目录（account是账号名称）
	处理目录命令
			  cd 切换目录 ：cd /（跳到顶层目录）
			  pwd 显示当前目录/pwd -p 显示真实目录
			  mkdir 建立一个新目录/mkdir -p text/text1 创建多层目录
			  rmdir 删除一个新目录/rmdir -p test1/test2 删除多层目录/rm -r test 所有目录下东西删除
	关于执行变量
			  查询文件路径变量 echo $PATH
	文件目录查看
			  全部文件：ls -a
			  完整显示文件的修改时间：ls -al --full-time ~(~符号代表家目录)				 
	复制，删除与移动：cp,rm,mv
		cp -i ~/.bashrc /tmp/bashrc : -i 若目标文件已存在，再覆盖时回先询问操作的进行
		cp /tmp/log/wtmp . : 想要复制到目前的目录，最后的点不要忘。
	    cp -a /var/log/wtmp wtmp2 文件所有特征一起复制
		cp -d bashrc_sink bashrc_sink_2 : 是链接文件
		cp ~/.bashrc ~/bash_history /tmp ： 两个文件复制到/tmp下
		rm -f 文件或目录 ： 忽略不存在的文件，不会出现警告
		rm -i bashrc* : 交互模式，删除回询问使用者
		rmdir /tmp/etc 建立的目录删除，如果空的目录不能执行
		rm -r /tmp/etc 递归删除，用于目录删除，危险选项
		\rm -r /tmp/etc 删除目录不用询问，可以忽略alias的选定选项。
		注意：如有-字符开头文件,前面加上./： rm ./-aaa-
		touch /-aaa- 建立空文件
		mv bashrc mvtest
		mv mvtest mvtest2 修改文件名
		mv bashrc1 bashrc2 mvtest2 有多个源文件或目录，最后一个目录文件一定是【目录】
		basename /etc/sysconfig/network ： 取得文件名
		dirname /etc/sysconfig/network : 取得目录名

	文件内容查看
		cat 由第一行开始显示文件内容
		tac 从最后一行开始显示
		nl 显示的时候，同时输出行号
		more 一页一页显示
		less 与more类似，可以往前翻页
		tail 只看后面几行
		od 以二进制方式读取文件内容
			cat /etc/issue
			cat -n /etc issue ： 有行号显示
			cat -A /etc/man_db.conf ： 完整显示，包含特殊的字符
			nl /etc/issue
			nl -b -a /etc/issue
			more /etc/man_db.conf 
				/字符串 ： 查找字符串这个关键字
			less /etc/man_db.conf
				g:前进到这个数据的第一行
				G：前进最后一行
				q：退出
			head -20 /etc/man_db.conf 默认显示10行
			tail 显示后面行
		注：以上都是查纯文本文件内容
	
	非纯文本文件
	od -t c usr/bin/passwd
		a:默认字符输出
		c：ASCII字符
		d[size]：十进制(decimal)
		f:浮点数(floating)
		o:八进制(octal)
		x：十六进制(hexadecimal)
	od -t oCc /etc/issue : 内容以把八进制列出存储值的ASCII的对照表
	
  	修改文件时间或创建新文件
		touch test ： 创建文件
		修改时间(modificateion time, mtime):内容数据修改
		状态时间(status time,ctime):状态修改，如权限或属性修改
		读取时间(access time,atime):该文件的内容被读取时候
		ls -l --time=atime /etc/man_db.conf
		注：默认显示mtime，ls -l 等于 ll
		touch '2 days ago' bashrc：把ctime修改为前两天
	
	文件默认权限：umask
		数字指的是该默认值需要减掉的权限
		umask : 查看
		umask -S 查看 

	文件隐藏属性
		chattr命令只在ext2，ext3，ext4的Linux传统文件系统上面完整生效，其他的文件系统可能无法完整支持这个命令，例如xfs仅至此部分参数而已	
		chattr [+-=][ASacdistu] 文件目录名称
		chattr +i attrtest ： 文件不能被删除，改名，设置链接也无法写入或新增数据
			   +a ： 可以增加，不能修改旧数据与删除的参数
		lsattr [-adR] 文件或者目录 ： 显示文件隐藏属性
		chattr +aiS attrtest
	
	文件特殊权限：SUID,SGID,SBIT
		Set UID
			权限仅对二进制程序邮箱
			执行者对于该程序需要具有X的可执行权限
			本权限仅仅在执行该程序的过程邮箱
			执行者将具有该程序拥有者的权限
			注意：dmtsai执行passwd的过程中，会怕【暂时】获取root权限，【-rwsr-xr-x】
		Set GID
			SGID对二进制程序有用，也可以针对文件或目录涉资
			程序执行者对于该程序来说，具备X的权限
			执行者在执行的过程中将会获得该程序用户组的支持
		Sticky Bit
			目录只针对目录有效
			当用户对此目录具有W，X权限，既具有写入的权限；
			当用户在该目录下建立文件或目录时，仅有自己或root才有权力删除该文件
			注意：/tmp权限【drwxrwxrwt】，任何人都可以在/tmp内新增，修改文件，但仅有该文件/目录建立与root能够删除自己的目录文件
	SUID,SGID,SBIT设置
		数字
		SUID：4
		SGID：2
		SBIT：1
		chmod 4755 fienname ： 前面加数字
		符号
		STID：u+s
		SGID：g+s
		SBIT：o+t
		chmod g+s,o+t test
	
	观察文件类型：file
		file 文件或等等
		file ~/.bashrc 
	
	脚本文件的查找
		根据【PATH】这个环境变量所规范的路径，去查找执行文件的文件名
		which [-a] command
		注意：history是bash内置的命令，但是which默认是找PATH内所设置的目录
		whereis(由一些特定的目录查找文件)
		whereris [-bmsu] 文件或目录名
		whereis ifconfig
	
	locate [-ir] keyword
		locate: 依据/var/lib/mlocate内的数据库记录，找出用户所输入的关键词的文件名
		locate寻找的数据是由己建立的数据库/var/lib/mlocate里面的数据所查找到的
		locate -S ： 列出locate查询所使用的数据库文件之文件名与各种数据数量
		updatedb：根据/etc/updatedb.conf的设置去查找系统硬盘内的文件，并更新/var/lib/mlocate内的数据库文件
		注意：数据库的建立默认是每天执行一次
	
	find [PATH] [option] [action]
		-mtime n,-mtime +n,-mtime -n,-newer file 为一个存在的文件，列出比file还要新的文件
		find / -mtime 0 ： 0代表目前时间，所以从开始到24小时前
		-user name : name为使用者的账号名称
		-group name
		-nouser ： 查找文件的拥有者不在/etc/passwd中
		-nogroup ：查询文件的用户组不尊在/etc/group的文件，自行安装软件时，很可能该软件的属性当中没有文件拥有者
		find /home -user quan ： 查找/home下面属于quan的文件
		find / -nouser ： 查找系统中不属于任何人的文件
		find /run -type： 找出/run目录下，文件类型socket的文件名，可以用type找出怪异文件
		find / -perm /7000 ： 查找文件当中含有SGID，SUID，SBIT的属性
		find /usr/bin -perm /7000 -exec ls -l {} \; : 使用ls列出来
		-exec command ： command为其他命令，-exec后面可劫额外的命令来处理查找到的结果

#第七章 linux磁盘与文件管理 
	一. 处理分区 MBR分区表还是GPT的分区表
		每种操作系统所设置的文件属性/权限并不相同，为了存放这些文件所需的数据，因此就需要分区进行格式化。		
	二. 文件系统
		1. FAT格式-u盘，FAT格式的文件没有inode存在，将这个文件的所有区块在一开始就读取出来，每个区块号码都记录在前一个区块当中。FAT的文件系统需要不时地碎片整理一下	
		2. 索引式文件系统
			超级区块：记录此文件的整体信息，包括inode与数据区块的总量，使用量。剩余量，以及文件系统与县官信息等。
			inode：记录文件的属性，一个为念占用一个inode，同时记录此文件的数据所在区块号码
			数据区块：实际记录文件的内容，若文件太大会占用多个区块。
		3. ext文件系统
			ext文件系统格式化的时候基本是区分为多个区块群组，每个区块都有独立的inode，数据区块，超级区块系统。
			文件系统最前面有一个启动扇区，这个启动扇区可以安装启动引导程序。
			数据区块
			ext2文件系统中所至此的区块大小有1k，2k及4k三种格式化时区块的大小固定。
	三. 挂载点的意义
		挂载点一定是目录，该目录为进入改文件系统的入口
		查询目录的indo号码
			ls -lid 目录
			
	四. 其他Linux支持的文件系统与VFS
		linux支持文件系统有哪些	ls -l /lib/modules/$(uname -r)/kernel/fs
		加载到内存中文件系统有那些 cat/proc/filesystems
		linux识别的文件系统都是VFS（Virtual Filesystem Switch）进行管理
	五. 文件系统的简单操作
		1. 磁盘目录的容量
			文件系统整体磁盘使用量	df [-ahikHTm] 目录或者文件名
			查看文件系统使用量 du [-ahskm] 文件或者目录名称
	六. 硬连接与符号连接
		硬链接 连接到摸inode号码的关联记录，磁盘的空间与indoe数目不变，不能连接目录，不能阔文件系统
		符号连接 独立文件
			ln [-sf] 源文件 目录文件
		不加参数是硬链接
	七. 磁盘分区，格式化，检验，挂载
		MBR分区表用fdisk，GPT分区表用gdisk
		1. lsblk所有磁盘列表	lsblk [-dfimpt] [device]
		2. blkid设备UUID blkid
		3. parted分区类型与分区信息 parted [设备名称] print
		4. 磁盘分区gdisk/fdisk gdisk 设备名称 注：？查看命令
			Last secto最后一行自定义 +多少G，默认值会将所有容量用光。
		5. partprobe更新类和分区表信息 partprobe [-s]
		6. gdisk删除一个分区	d命令
			
#第十二章 学习Shell脚本
	一. Shell脚本良好的编写习惯
		1. 脚本功能
		2. 版本信息
		3. 作者联系方式
		4. 版权声明方式
		5. 历史记录（History）
		6. 特殊的命令，使用【绝对路径】的方式执行
		7. 运行时需要的环境变量预先声明与设置
		注意： 编写脚本工具Vi而不是Vi，Vim有语法检验功能
	二. 脚本执行方式差异（source、 sh script、 ./script）
		1. 直接执行方式（sh）：当子进程完成后，在子进程内的各项变量或操作将会结束而不会传会父进程当中
		2. source执行，在父进程执行：source showname.sh
	三. test命令测试功能
		例子： test -e /dmtsai && echo "exist" || echo "Not exist"
	四. 判断符号[]
		中括号两端需要有空格符来分隔
	五. Shell脚本默认变量（$0、$1）
		1. $#: 代表后街的参数【个数】，以下表为列显示4
		2. $@: 【"$1","$2","$3","$4"】,每个变量独立
		3. $*: 【"$1 $2 $3 $4"】
		注： $@与$*有所不同，一般用$@
		列：/path/to/scriptname opt1 opt2 opt3 opt4
			$0					$1	 $2	  $3   $4
		shift: 造成参数变量号码偏移（后面接数字（Shift 3））从前面开始偏移
	六. 条件判断式（if....then）		
		1. 单层、简单条件判断
			if [ 条件判断 ]; then
				当条件，进行命令
			fi <==将if反过来，结束if之意
			&& 代表 AND -a
			|| 代表 or -o
			列： [ "${yn}" == "Y" -o "${yn}" == "Y" ] 可替换为 ["${yn}" == "Y"] || ["${yn}" == "Y"]
		2. 多重、复杂条件判断
			if []; then
			elif []; then
			else
			if
		3. case...esac判断
			case $变量名称 in
				"第一个变量内容")
				程序段
				;;
				"")
				;;
				*) # *代表其他值，相当于通配符，0~无穷多个任意字符之意
				;;
			esac
		注： 参考 less /etc/init.d/netconsole
		4. function功能
			shell脚本执行由上而下，由左而右，因此shell脚本当中的function的设置一定要在程序最前面
			function 方法名称() {}
		5. 循环while
			循环可以不断的执行某个程序段落，直到用户设置的条件完成为止
			while [ condition ] #中括号内是判断式
			do
			done
			注： 当condition条件成立时，就进行循环，直到条件不成立才停止
			until [ condition ]
			do
			done	
			注：当condition成立时，就终止循环，否者就持续进行循环的程序段。
		6. for...do...done(固定循环)
			已知道要进行几次循环的状态
			for var in con1 con2 con3...
			do
				程序段
			done
			for的另一种写法
			for (( 初始值; 限制值; 赋值运算))
			do
				程序段
			done
		7. shell脚本的跟踪与调试
			sh [-nvx] scripts.sh
			-n: 不要执行脚本，仅查询语法问题
			-v: 在执行脚本前，先将脚本文件的内容输出到屏幕上
			-x: 将使用的脚本内容显示到屏幕上，比较有用的参数 
			 
#问题
	无法启动网络管理器在线等待
		引导至恢复模式，打开终端并使用： sudo systemctl disable NetworkManager-wait-online.service
	swp文件处理
		修复 vi -r {your file name}
		删除 rm .XXX.swp。注意有个小数点. 
	连接wifi
		https://blog.csdn.net/armybase11/article/details/52725058
		https://blog.csdn.net/wannahaha/article/details/108124294
	xshell的快捷键（非常实用）
		https://blog.csdn.net/qq_39785418/article/details/93322530
	linux命令查询: https://man.linuxde.net/
	netstat -tulpn
	systemctl restart network

	
	http://tlblog.top/
tlblog.top
	