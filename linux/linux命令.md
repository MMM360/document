[toc]

#第零章 计算机概论
#第一章 Linux是什么与如何学习
#第二章 主机规划与磁盘分区
#第三章 安装CentOS7.x

#第四章 首次登陆与在线求助
```
一. 日历
	日期时间 date
	年月 cal 2019
	某年指定月份 cal 10 2019
二. 计算器
		bc 推出 quit
		值显示小数点后多少位 scale=3
三. 在线求助 man page 与 info page
		输入未完整的命令+两下tab
		date --help
		man date
四. 关机
		将数据同步写入硬盘中 sync
		常用关机 shutdown now
		重新启动，关机 reboot，halt，poweroff
		都基于 systemctl [命令] 
			halt 进入系统停止模式，屏幕
五. 中断目前程序
		【ctrl】-C
		演示find/
```
#第五章Linux文件权限与目录配置
###创建用户删除用户
	su、sudo、su - root的区别:https://www.cnblogs.com/amyzhu/p/8526074.html
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
							    例：chomd u=rwx,go=rx .bashrc
	tlblog163
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
```
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
```

#第八章 文件与文件系统的压缩
```
压缩文件扩展名：
*.Z	        compress程序解压文件；
*.zip		zip程序压缩文件；
*.gz		gzip程序压缩文件；
*.bz2		bzip2程序压缩文件；
*.xz		xz程序压缩文件；
*.tar		tar程序打包的文件，并且没有压缩过；
*.tar.gz	tar程序打包的文件，并且经过gzip的压缩；
*.tar.bz2	tar程序打包的文件，并且经过bzip2的压缩；
*.tar.xz	tar程序打包的文件，并且经过xz的压缩；

一. gzip
	语法：
		gzip [-cdtv#] 文件名
		zcat 文件名
	选项与参数：  
	| -c | 将压缩数据传输到屏幕上，可通过数据流重定向来处理 |
	| -d | 解压缩的参数 |
	| -t | 可以用来验证一个压缩文件的一致性，看看文件有无错误 |
	| -v | 可以显示出源文件/压缩文件的压缩比等信息 |
	| -# | #为数字的意思，代表压缩等级，-1最块，但是压缩比差，-9最慢，但是压缩比最好，默认-6 |
	列：
	gzip -v /etc/services
	zcat services
	gzip -d services.gz
	gzip -9 -c services > service.gz
	zgrep -n 'http' services.gz

二. bzip2
	语法：
		bzip2 [-cdkzv#] 文件名
		bzcat 文件名.bz2
	选项与参数：
	| -c | 将压缩的过程产生的数据输出到屏幕上 |
	| -d | 解压缩的参数 |
	| -k | 保留原始文件，而不会删除原始的文件 |
	| -z | 压缩的参数（默认值，可以不加） |
	| -v | 可以显示出源文件/压缩文件的压缩比等信息 |
	| -# | #为数字的意思，代表压缩等级，-1最块，但是压缩比差，-9最慢，但是压缩比最好，默认-6 |
	列：
	bzip2 -v services
	bzcat services.bz2
	bzip2 -d services.bz2
	bzip2 -9 -c services > services.bz2

三. xz
	语法：
		xz [-dtlkc#] 文件名
		xcat 文件名
	选项与参数：
	| -c | 将压缩的过程产生的数据输出到屏幕上 |
	| -d | 解压缩 |
	| -t | 测试压缩文件的完整性，看有没有错误 |
	| -l | 列出压缩文件相关信息 |
	| -k | 保留原始文件，而不会删除原始的文件 |
	| -# | #为数字的意思，代表压缩等级，-1最块，但是压缩比差，-9最慢，但是压缩比最好，默认-6 |
	列：
	xz -v services
	xz -l services.xz
	xzcat services.xz
	xz -d services.xz
	xz -k services

已/etc/services为例子：
| 名称	 | 压缩比=压缩后/压缩前 | 时间s |
| :----: | :----: | :----: |
| -gzip  | 21%    | 0.019s |
| -bzip2 |        | 0.420s |
| -xz    | 15%    | 0.261s |

	
四. tar打包命令
	语法：
	tar [-z|-j-J] [cv] [-f 待建立新的文件夹] filename..   <==打包与压缩。
	tar [-z|-j-J] [tv] [-f 既有的tar文件名]               <==查看文件名。
	tar [-z|-j-J] [xv] [-f 既有的tar文件名] [-c 目录]      <==解压缩。
	选项与参数：
	| -c     | 建立打包文件，可搭配-v来查看过程中被打包的文件名（filename） |
	| -t     | 查看打包文件的内容包含哪些文件名，重点查【文件名】 |
	| -x     | 解压或解压缩的功能，可以搭配-C（大写）在特定目录解压，-c、-t、-x不可同时出现在一圈命令行中 |
	| -z     | 通过gzip的支持进行压缩/解压缩: 此时文件名最好为 *.tar.gz |
	| -j     | 通过bzip2的支持进行压缩/解压缩: 此时文件名最好为 *.tar.bz2 |
	| -J     | 通过xz的支持进行压缩/解压缩: 此时文件名最好为: *.tar.xz,特别留意,-z、-j、-J不可以同时出现在一串命令行中 |
	| -v     | 在压缩/解压缩过程中,将正在处理的文件名显示出来 |
	| -f     | + filename: -f后面立刻接要被处理的文件名,建议-f单独写一个选择。 |
	| -C     | + 目录: 这个选项用在解压缩,能在特定目录解压缩 |
	| -p     | 保留备份数据的原本权限与属性,常用于备份(-c)重要的配置文件 |
	| -P     | 保留结对路径,亦即允许备份数据中含有根目录存在之意 |
	| --exclude=FILE| 在压缩过程中,不要将FILE打包 |
	列：
	time tar -zpcv -f /root/etc.tar.gz /etc <==加time表示运行花费时间
	time tar -jpcv -f /root/etc.tar.bz2 /etc
	time tar -Jpcv -f /root/etc.tar.xz /etc
	说明:
	解压去掉根目录原因,安全(防止数据被覆盖)
	有根目录,解压缩的文件名就会绝对路径 ==>/etc/xxx
	没有根目录,当前所在目录下解压 ==>/tmp/etc/xxx

五. XFS文件系统的备份与还原
XFS文件系统备份 xfsdump
	功能：
	1. 能完成备份
	2. 能增量备份
	xfsdump备份限制: 
	1. 不支持没有挂载的文件系统备份，所以只能备份已挂载的文件系统
	2. 必须使用root的权限才能操作（涉及文件系统的关系）
	3. 只能备份xfs文件系统
	4. 备份下来的数据（文件或存储媒介）只能让xfsrestor解析
	5. 是通过文件系统的UUID来辨别各备份文件，因此不能备份连个具有相同UUID的文件系统
	语法：
	xfsdump [-L S_lable] [-M M_lable] [-l #] [-f 备份文件] 待备份数据
	xfsdump -I
	选项与参数：
	| -L     | xfsdump会记录每次备份的session标头，这里可以填写针对此文件系统的简易说明 |
	| -M     | xfsdump可以记录存储媒介的标头，这里可以填写此媒介的简易说明 |
	| -l     | 是L的小写，就是指定等级，有0~9共10各等级。（默认为0，即为完整备份） |
	| -f     | 有点类似tar，后面接产生的文件，亦可接列入/dev/st0设备文件名或其他一般文件名 |
	| -I     | 从/var/lib/xfsdump/inventory列出目前备份信息状态 |
	列：
	# 将完整备份文件名记录为/srv/boot.dump
	xfsdump -l 0 -L boot_all -M boot_all -f /srv/boot.dump /boot 
	# 建立差异备份文件，此时我们使用level 1
	xfsdump -l 1 -L boot_2 -M boot_2 -f /srv/boot.dump /boot

XFS文件系统还原 xfsrestore
	语法：
	xfsrestore -I #查看备份文件
	xfsrestore [-f 备份文件] [-L S_lable] [-s] 待恢复目录 #单一文件全系统恢复
	xfsrestore [-f 备份文件] -r 待恢复目录 #用过增量备份文件来恢复系统
	xfsrestore [-f 备份文件] -i 待恢复目录 #进入交互模式
	选项与参数：
	| -I     | 可查询备份数据，包括Label名称与备份时间等 |
	| -f     | 后面接着就是备份文件。企业界很有可能会接/dev/st0等磁盘机，我们这里接文件名 |
	| -L     | 就是session的Label name，可用-I查询到的数据，在这个选项输入 |
	| -s     | 需要接某特定目录，亦即仅恢复某一个文件或目录之意 |
	| -r     | 如果用文件储存备份数据，则不需要使用，如果一个磁带内有多个文件，需要此选项来恢复累计恢复 |
	| -i     | 进入交互模式，高级管理员使用，一般我们不要需要操作它 |
	列：
	xfsrestore -I
	# 直接将数据覆盖回去即可
	xfsrestore -f /srv/boot.dump -L boot all /boot
	# 将备份的数据在/tmp/boot下面解开
	xfsrestore -f /srv/boot.dump -L boot all /tmp/boot
	# 比较两个文件差异
	diff -r /boot /tmp/boot
	# 仅恢复备份文件内的grub2到/tmp/boot2/里面去
	xfsrestore -f /srv/boot.dump1 /tmp/boot
	# 恢复增量备份数据
	# 继续恢复level 1到/tmp/boot当中
	xfsrestore -f /srv/boot.dump1 /tmp/boot
	# 仅还原部分文件的xfsrestore交互模式
	xfsrestore -f /srv/boot.dump -i /tmp/boot3
	选项与参数：
	| -add [<path>]    | 可以加入恢复文件列表中 |
	| -delete [<path>] | 从恢复列表去掉文件，并非删除 |
	| extract [<path>] | 开始恢复操作 |
	add grup
	add grup2
	extract
```

#第九章 vim程序编辑器
```
一般命令模式（command mode）
编辑模式（insert mode）
命令模式（command-line mode）
注：前一个版本中，一般命令模式被称一般模式

一. 按键说明
	URL：https://www.runoob.com/linux/linux-vim.html

二. vim的缓存、恢复与打开时的警告信息
	缓存存在问题与决解
	1. 可能有其他人或程序同时在编辑这个文件
	   找到另外那个程序或人员，结束vim工作
       输入【O】只读预览
	2. 不知名原因导致vim中断
	   对文件操作过后，删除缓存
	选项与参数：
	| [O]pen Read-Only | 打开此文件成为只读文件，只查看不修改 |
	| (E) dit anyway   | 正常方式打开你要编辑的那个文件，并不会加载缓存内容 |
	| (R) ecover       | 加载缓存内容，救会之前未保存的操作，保存退出vim后，需手动删除那个缓存 |
	| (D) elete it     | 打开文件前会先将这个缓存删除掉 |
	| (Q) uit          | 退出vim |
	| (A) bort         | 忽略这个编辑操作 |

三. 可视区块（Visual Block）
	文件在一般命令模式下输入v或V或[Ctrl]+v时，光标移动反白选择
	可视区块的按键意义：
	| v        | 字符选择，会将光标经过的地方反白选择 |
	| V        | 行选择 |
	| [Crtl]+v | 可视区块，可以用矩形的方式选择数据 |
	| y        | 将反白的地方复制起来 |
	| d        | 将反白的地方删除掉 |
	| p        | 将刚刚复制的区块，在光标所在处粘贴 |

四. 多文件编辑
	语法
	vim [文件名称1] [文件名称2]
	多文件编辑的按键：
	| :n        | 编辑下一个文件 |
	| :N        | 编辑上一个文件 |
	| :files    | 列出目前这个vim开启所有文件 |

五. 多窗口功能
	语法
	1. vim [文件名称]
	2. :sp [文件名称]
	多窗口情况下的按键功能：
	| :sp [filename]               | 打开一个新窗口 |
	| [Ctrl]+w+j 或 [Ctrl]+w+方向下 | 光标移动到下方的窗口 |
	| [Ctrl]+w+k 或 [Ctrl]+w+方向上 | 光标移动到上方的窗口 |
	| [Ctrl]+w+q                   | 退出当前窗口 |
	| :close                       | 关闭所有窗口 |	

```	
#第十章 认识与学习BASH
```
一. 硬件、内核、与Shell
	1. 硬件：计算机需要的物理硬件，如，CPU，显示器等
	2. 内核管理：操作系统的内核支持这些芯片组，需要提供内核驱动程序
	3. 应用管理：需要用户（就是你）输入命令
	要通过Shell将我们输入的命令与内核沟通，好让内核可以控制硬件来正确无误的工作
	
二. Bash shell的功能
	1. 历史命令（hostory）
	~/.bash_history记录的是前一次登陆以前执行的命令，而至于这一次执行的命令都被缓存在内存中，当你成功注销系统后，改命令才会记录到.bash_history当中
	2. 命令与文件补全功能
	3. 命令别名设置功能：（alias）
	4. 任务管理、前台、后台控制：（job contorl、foreground、background）
	5. 程序和脚本：（shell scripts）
	6. 通配符：（wildcard）

三. 查询命令时候为Bash shell的内置命令：type
	语法 type [-tpa] name
	选择与参数
	|    | 不加任何选项与参数时，type会显示出name是外部命令还是bash内置命令 |
	| -t | 当加入-t是，type将name以下面描述显示出他的意义 |
	|    | file    ：表示为外部命令 |
	|    | alias   ：表示该命令为命令别名所设置的命令 |
	|    | builtin ：表示该命令为bash内置的命令功能 |
	| -p | 如果后面接的name为外部命令，才会显示完整文件名 |
	| -a | 会由PATH变量定义的路径中，将所有含name的命令都列出来，包含alias |

四. 命令的执行与快速编辑按钮
	快捷键
	| \+[Enter]         | 命令换行 |
	| [ctrl]+u\[ctrl]+k | 分别是从光标向前删除命令串（[Ctrl+u]）及向后删除命令串（[Ctrl+k]）|
	| [ctrl]+a\[ctrl]+e | 分别是从光标移动最前面（[Ctrl+a]）或最后面（[Ctrl+e]）|

五. Sell的变量功能
	1. 双引号可以保有变量的内容，单引号是一边符，而不会有特殊符号
	2. 子进程仅会几次父进程的环境变量，子进程不会继承父进程的自定义变量	
	语法
	echo ${name}   <==设置变量
	unset name     <==取消设置变量
	set			   <==观察所有变量（含环境变量与自定义变量）
	export 变量名称 <==设置环境变量
	其他
	uanme -r      <==获取内核信息
	
六. PS1:提示字符的设置
	其他
	$:（关于本shell的PID）
	美元符号本身也是变量，代表目前这个shell的进程号，PID（Process ID）
	
七. 影响显示结果的语系变量（locale）
	1. 设置LANG或是LC_All时，则其他的语系变量就会被这两个变量所替换
	2. 
	语法
	locale -a                <==查询linux支持语系
	cat /etc/locale.conf     <==查询系统默认支持语系
	export LC_ALL=zh_CN.utf8 <==设置系统默认（整体语系环境）
	
八. 变量键盘读取：read
	语法
	read [-pt] variable
	选项与参数
	| -p | 后面可以提示字符 |
	| -t | 后面可以接等待的【秒数】 |
	列
	# 提示使用者30秒内输入自己的名称，将输入字符作为named的变量内容
	read -p "Please keyin your name:" -t 30 named

九. 声明变量类型：declare
	1. 变量类型默认为字符串
	2. bash环境中的数字运算，默认最多仅能达到整数形态，所以1/3结果是0
	语法
	declare [-aixr] variable
	var[index]=content <==数组变量类型
	选项与参数
	| -a | 定义为数组类型（array） |
	| -i | 定义为整数类型（integer）|
	| -x | 定义为环境变量 |
	| -r | 定义为只读类型（readonly），不能被更改内容，也不能unset |
	| -p | 列出变量类型 |
	列
	declare -i sum=100+300+500
	echo $(sum)
	declare +x sum   <==将-变成+可以进行【取消】操作
	var[1]="var1"    <==数组定义
	echo "${var[1]}" <==数组读取 
	注
	1. 如果你将变量设置为【只读】，通常要注销再登陆才能恢复变量的类型
	
十. 与文件系统及程序的限制关系: ulimit
	bash是可以限制用户默写系统资源的，包括可以开启的文件数量，可以使用CPU时间，可以使用的内存总量等
	省略
	
十一. 变量内容的删除
	# ：符合替换文字的【最短的】哪一个；
	##：符合替换文字的【最长的】哪一个；
	列
	# 删除前一个目录
	echo ${path#/*local/bin:}

十二. 命令别名设置：alias、unalias
	语法
	alias 别名='命令 选项...' <==设置命令别名
	unalias 别名             <==删除别名
	列
	alias rm='rm -i'
	unalias lm
	
十三. 历史命令：history
	语法
	history [n]
	history [-c]
	history [-raw] histfiles
	选项与参数
	| n  | 数字，【列出最近的n行命令列表】 |
	| -c | 将目前的shell中的所有history内容全部清除 |
	| -a | 将目前新增的history命令新增如histfiles中，如每加histfiles，则默认写入~/.bash_history |
	| -r | 将histfiles的内容读到目前这个shell的history记录中 |
	| -w | 将目前history记录内容写入histfiles中 |
	语法【history其他功能】
	!number
	!command
	!!
	选项与参数
	| number  | 执行第几条命令的意思 |
	| command | 由最近的命令向前查找【命令串开头为command】的那个命令，并执行 |
	| !!      | 就是执行上一个命令 |

十四. bash的登陆与欢迎信息：/etc/issue、/etc/motd
	省略
	
十五. bash的环境配置文件
	读入环境配置文件的命令：source
	语法
	source 配置文件文件名
	
十六. 终端的环境设置：stty、set
	省略
	
十七. 通配符与特殊符号
	
十八. 数据重定向
	说明
	1. 标准输入（stdin）：代码为0，使用 < 或 <<;
	2. 标准输出（stdout）：代码为1，使用 > 或 >>;
	3. 标准错误输出（stderr）：代码为2，使用 2> 或 2>>;
	符号说明
	1. 1> : 以覆盖的方法将【正确的数据】输出到指定的文件或设备上；
	2. 1>>: 以累加的方法将【正确的数据】输出到指定的文件或设备上；
	3. 2> : 以覆盖的方法将【错误的数据】输出到指定的文件或设备上；
	4. 2>>: 以累加的方法将【错误的数据】输出到指定的文件或设备上；
	列
	# 将stdout与stderr分别存到不同文件中
	find /home -name .bashrc > list_right 2> list_error
	# /dev/null 垃圾桶黑洞设备与特殊写法
	cat > catfile << "eof" #当键盘输入eof结束
	注
	如两股数据同时写入一个文件，又没有使用特殊的语法，此时两个数据可能会交叉写入该文件内，造成次序错乱 

十九. 命令执行的判断根据：；、&&、||
	语法
	cmd;cmd（不考虑命令相关的性的连续命令执行）
	列
	sync; sync; shutdown -h now
	$?(命令返回值)与&&或||
	| cmd1 && cmd2 | cmd1正确-执行cmd2，cmd1错误-cmd2不执行 |
	| cmd1 || cmd2 | cmd1正确-不执行cmd2，cmd1错误-cmd2执行 |

二十. 管道命令
	【|】：仅能处理经由前面一个命令传来的正确信息，也是标准输出信息，对于错误信息没有直接处理能力
	选取命令：cut
	语法	
	cut -d '分隔字符' -f fields <==用于有特定的分隔字符
	cut -c 字符区间
	选项与参数
	| -d | 后面接分隔字符，与-f一起使用 |
	| -f | 根据-d的分隔字符将一段信划分成为数段，用-f取出第几段的意思 |
	| -c | 以字符的单位取出固定字符区间 |
	列
	echo ${PATH} | cut -d ':' -f 5
	echo ${PATH} | cut -d ':' -f 3,5
	export | cut -c 12-
	选取命令：grep
	grep分析行信息，若有，将该行拿出来
	语法
	grep [-acinv] [--color=auto] '查找字符' filename
	选项与参数
	| -a | 将二妗子文件以文本文件的方式查找数据 |
	| -c | 计算找到'查找字符'的次数 |
	| -i | 忽略大小写的不同，所以大小写视为相同 |
	| -n | 顺便输出行号 |
	| -v | 反向选择，亦即显示出没有 '查找字符'内容哪一行 |
	注
	grep支持的语法太多，还有一些没列举出来，比如正则表达式
	
二十一. 排序命令：sort、wc、uniq
	sort：根据不用数据形式进行排序
	语法
	sort [-fbMnrtuk] [file or stdin]
	选项与参数
	| -f | 忽略大小写的差异 |
	| -b | 忽略最前面的空格字符部分 |
	| -M | 以月份的名字排序 |
	| -n | 使用【纯数字】进行排序（默认以文字形式排序）|
	| -r | 反向排序 |
	| -u | uuiq，相同数据中，仅出现一行代表 |
	| -t | 分隔符号，默认是用[Tab]键来分隔 |
	| -k | 以那个区间（field）来进行排序的意思 |
	unuq:数据仅列出一个显示
	语法
	uniq [-ic]
	选项与参数
	| -i | 忽略大小写字符的不同 |
	| -c | 进行计数 |
	wc:计算输出信息的整体数据
	语法
	wc [-lwm]
	选项与参数
	| -l | 仅列出行 |
	| -w | 仅列出多少字（英文字母）|
	| -m | 多少字符 |

二十二. 双向重定向：tee
	数据流的处理中将某段信息存下来
	语法
	tee [-a] file <==若加-a能将信息累加
	选项与参数
	| -a | 以累加（append）的方式，将数据加入file当中 |
	列
	# ls的数据寸一份到~/homefile，同时屏幕也有输出信息
	ls -l / | tee -a ~/homefile | more

二十三. 字符转换命令：tr、col、join、paste、expand
	省略
	
```

#第十一章 正则表达式与文件格式化处理

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
			 
#第十三章 Linux账号管理与ACL权限设置
```
一. 用户账号相关文件
	1. 管理用户UID与GID重要参数的：/etc/passwd
	2. 管理密码相关数据的：		/etc/shadow
	用户组：有效与初始化用户组：groups、newgr
	1. 用户组配置文件：			/etc/group
	列
	groups: 有效与支持用户组的观察
	groups
	newgrp：有效用户组的切换
	newgrp users #是另外一个shell来提供功能
	groups
	exit  #注意，记得退出newgrp环境
二. 账号管理
	1. useradd
	语法
	useradd [-u UID] [-g 初始用户组] [-G 次要用户组] [-mM] [-c 说明栏] [-d 家目录绝对路径] [-s shell] 使用者账号名
	选项与参数
	| -u | 后面接UID，是一组数字 |
	| -g | 后面接的用户组就是初始用户组，GID会放到/etc/passwd的第四个栏位内 |
	| -G | 后面接的用户组测试该账号还可加入得用户组，会修改/etc/group内相关内容 |
	| -M | 强制，不要建立使用者家目录（系统默认值）|
	| -m | 强制，要建立使用者家目录（一般账号默认值）|
	| -c | 这个就是/etc/passwd的第五栏说明内容，随意我们设置 |
	| -d | 指定某个目录成为家目录，而不要使用默认值，使用绝对路径 |
	| -r | 建立一个系统账号，这个账号UID会有限制（参考/etc/login.defs）|
	| -s | 后面接一个shell，若没指定默认/bin/bash |
	| -e | 后面接日期，格式【YYYY-MM-DD】此选项可写入shadow第八个位，账号失效日的设置选项 |
	| -f | 后面接shadow的第七个栏位，指定密码是否会失效。0：立马失效/-1：永远不会失效（密码只会过期而强制于登陆时重新设置而已）
	列
	useradd wxq
	useradd -u 1500 -g users wxq
	
	2. passwd
	使用useradd建立账号后，默认情况，该账号暂时锁定的。
	语法
	passwd [--stdin] [账号名称] <==所有人均可以使用来改自己密码
	passwd [-l] [-u] [--stdin] [-S] [-n 日数] [-x 日数] [-w 日数] [-i 日期] 账号 <==root功能
	选项与参数
	| --stdin | 可以通过来自前一个管道数据，作为密码输入，对shell脚本有帮助 |
	| -l      | Lock的意思，会将/etc/shadow的第二栏最前面加上!使密码失效 |
	| -u      | 与-l相对，是Unlock的意思 |
	| -S      | 列出密码相关参数 |
	| -n      | 后面接天数，shadow第4栏位，多久不可以修改密码天数 |
	| -x      | 后面接天数，shadow第5栏位，多久内必须要修改密码 |
	| -w      | 后面接天数，shadow第6栏位，密码过期前警告天数 |
	| -i      | 后面接【日期】，shadow第7栏位，密码失效日期 |
	列
	passwd wxq
	
	3. chage：更详细密码参数显示
	省略
	
	4. usermod：账号修改参数
	语法
	usermod [-cdegGlsuLU] username
	| -c | 后面接账号的说明，即/etc/passwd第五栏的说明栏，可以加入一些账号的说明 |
	| -d | 后面接账号的家目录， 即修改/etc/passwd的第六栏 |
	| -e | 后面接日期， 格式是YYY-M-DD也就是在/etc/shadow内的第八个栏位的内容 |
	| -f | 后面接天数，为shadow的第七栏位 |
	| -g | 后面接初始用户组，修改/etc/passwd的第四个栏位，亦即是GID的栏位 |
	| -G | 后面接次要用户组，修改这个使用者能够支持的用户组，修改的是/etc/group |
	| -a | 与-G合用，可[增加次要用户组的支持]而非[设置]
	| -l | 后面接账号名称，亦即是修改账号名称，/etc/passwd的第一栏 |
	| -s | 后面接shell的实际文件，例如/bin/bash或/bin/csh等 |
	| -u | 后面接UID数字，即/etc/passwd第三栏的数据 |
	| -L | 暂时将使用者的密码冻结，让它无法登录，其实仅改/etc/shadow的密码栏 |
	| -U | 将/etc/shadow密码栏的感叹号(!)拿掉，解锁 |
	列
	# 使用者wxq这个账号在2015/10/10失效
	usermod -e "20158-10-10" wxq

	5. userdel:删除用户相关数据
	删除的文件
	1. 用户账号/密码相关参数：/etc/passwd、/etc/shadow
	2. 用户组相关参数：/etc/group、/etc/gshadow
	3. 用户个人文件数据：/home/username、/var/spool/mail/username
	语法
	userdel [-r] username
	选项与参数
	| -r | 连同使用者家目录一起删除

三. 用户功能
	1. id：查询默认或自己相关UID/GID等信息
	语法
	id [username]
	
	2. finger
	省略

	3. chfn
	省略

	4. chsh
	省略

四. 新增与删除用户组
	1. groupadd
	语法
	groupadd -[g gid] [-r] 用户组名称
	选项与参数
	| -g | 后面接某个特定的GID，用来设置GID |
	| -r | 建立系统用户组 |
	列
	groupadd group1
	
	2. groupmod
	语法
	groupmod [-g gid] [-n group_name] 用户组名
	选项与参数	
	| -g | 修改既有GID数字 |
	| -n | 修改既有的用户组名称 |
	列
	# group1名称改为mygroup，GID为201
	groupmod -g 201 -n mygroup group1
	
	3. groupdel
	groupdel [groupname]
	
	4. gpasswd: 用户组管理员功能
	语法（系统管理员root做的操作）
	gpasswd groupname
	gpasswd [-A user1,...] [-M user3,...] groupname
	gpasswd [-rR] groupname
	选项与参数
	|    | 若没有任何参数，表示设置groupname密码（etc/gshadow）
	| -A | 将groupname的管理权交由后面的使用管理者管理（该用户组的管理员）
	| -M | 将某些账号加入这个用户组中 |
	| -r | 将groupname的密码删除 |
	| -R | 让groupname的密码栏失效 |
	列
	gpasswd -A wxq testgroup <==加入用户组管理员为wxq
	语法（用户组管理员（Group administrator）做的操作）
	选项与参数
	| -a | 将某位使用者加入到groupname这个用户组当中 |
	| -d | 将某位使用者删除出groupname这个用户组当中 |
	列
	gpasswd -a wxq testgroup

五. 主机的详细权限规划：ACL的使用
	1.什么是ACL
	ACL是Access Control List 的英文缩写，中文译为访问控制列表
	ACL针对以下方面来控制权限：
	1. 用户（user）：可以针对用户来设置权限
	2. 用户组（group）：针对用户组为对象来设置其权限
	3. 默认属性（mask）：还可以针对该目录下建立新目录/目录时，规范新数据的默认权限
	使用ACL原因：
	如果你有一个目录，需要给一堆人使用，每个人或每个用户组所需要的权限并不相同时，传统的Linux三种身份的三种权限是无法达到的
	查看是否开启ACL（用内核挂载是显示信息命令）
	dmesg | grep -i acl
	
六. ACl的设置技巧：getfacl
	1. getfacl：获取某个文件/目录的ACl设置选项
	2. setfacl: 设置某个目录/文件的ACL规范
	语法
	setfacl [-bkRd] [{-m|x} acl 参数] 目标文件名
	选项与参数
	| -m | 设置后续的ACL参数给文件使用，不可与-x合用 |
	| -x | 删除后续的ACL参数，不可与-m合用 |
	| -b | 删除[所有的]ACL设置参数 |
	| -k | 删除[默认的]ACL参数，关于所谓的 [默认]参数于后续范例中介绍 |
	| -R | 递归设置ACL, 即包括子目录都会被设置起来 |
	| -d | 设置[默认ACL参数]的意思，只对目来有效，在该目录新建的数据会引用此默认值 |
	列
	#1. 利用【u:用户:权限设置】
	setfacl -m u:wxq:rx acl_test1
	# 查询文件的权限结果会多了个【+】符号
	setfacl -m u::rwx acl_test1
	#2. 特定的单一用户组的权限设置：【g:用户组名:权限】
	setfacl -m g:mygroup1:rx acl_test1
	#3 针对有效权限设置【m:权限】
	#  有效权限意思：用户或用户组所设置的权限必须要存在于mask的权限设置范围内才会生效，此即【有效权限（effective permission）】
	setfacl -m m:r acl_test1
	#4 使用默认权限设置目录未来文件的ACL权限继承【d:[u|g]:[user|group]:权限】 <==重点
	setfacl -m d:u:myuser1:rx /srv/projecta
	语法
	getfacl filename
	选项与参数
	getfacl与setfacl选项参数基本相同，省略

七. 用户身份切换
	1. su
	语法
	su [-lm] [-c 命令] [username]
	选项与参数
	| -  | 单纯使用一如[su -]代表使用login-shell 的变量文件读取方式来登录系统 |
	|    | 若使用者名称没有加上去，则代表切换为root的身份 |
	| -l | 与-类似，但后面需要加欲切换的使用者账号，也是login-shell的方式 |
	| -m | -m与-p是一样的，表示[使用目前的环境设置，而不读取新使用者的配置文件 |
	| -c | 仅进行一次命令，所以-c后面可以加上命令 |
	说明
	只使用【su】切换root的身份，读取的变量设置方式为非登陆shell的方式，这种方式很多原本的变量不会被修改
	列
	su -
	exit <==这样可以退出su -环境
	2. sudo
	说明
	并非所有人都能执行sudo，而是仅有规范到/etc/sudoers内的用户才能执行sudo这个命令
	语法
	sudo [-b] [-u 新使用者的账号]
	选项与参数
	| -b | 将后续的命令放到后台中让系统自行执行，而不与目前的shell产生影响 |
	| -u | 后面可以接欲切换的使用者，若无此项规则代表切换身份为root |
	3. visudo修改/etc/sudoers（让操作的用户具备root命令功能）
	列
	visudo
	root All=(ALL) ALL <==找到这一行，大约98行
	wxq All=(All) All  <==这一行是你要新增的
	# 限制命令参数
	visudo
	wxq ALL=(root) !/usr/bin/passwd,/usr/bin/passwd [A-Za-z]*,!/usr/bin/passwd root <==不能更改root密码
	4. 利用wheel用户组以及免密码的功能处理visudo
	省略
	
```

#第十四章 磁盘配额（Quota）与高级文件系统管理
```
一. 磁盘配额的应用与实践
二. 软件磁盘阵列 (Software RAID)
三. 逻辑卷管理起（LogicalVolume Manager）
1. 什么是LVM: PV、PE、VG、LV的意义

```

#第十五章 计时任务（crontab）
```
一. 仅执行一次的计划任务
1. atd的启动与at运行的方式
	systemctl restart atd # 重新启动atd这个服务
	systemctl enable atd  # 让这个服务开机就自动启动
	systemctl status atd  # 查看一下atd目前的状态
2. 实际运行单一计划任务
	at [-mldv] TIME
	at -c 任务号码
	选项与参数
	| -m | 当at的任务完成后，即使没有输出信息，亦发email通知使用者该任务已完成 |
	| -l | at -l相当于atq,列出目前系统上面的所有该使用者的用者的at计划 |
	| -d | at -d相当于atrm, 可以取消个在at计划中的任务 |
	| -v | 可以使用较明显的时间格式列出at计划中的任务列表 |
	| -c | 可以列出后面接的该项任务的实际命令内容 |
	TIME : 时间格式，这里可以定义出[什么时候要执行at这项任务]的时间，格式有:
		HH:MM                       ex> 04:00
		     在今日的 HH:MM时刻执行，若该时刻已超过，则明天的HH:MM执行此任务。
		HH:MM YYYY-MM-DD            ex> 04:00 2015-07-30
		     强制规定在某年某月的某天的特殊时 刻执行该任务
		HH:MM[am|pm] [Month] [Date] ex> 04pm July 30
			 也是一样，强制在某年某月某日的某时刻执行。
		HH:MM[am|pm] + number [minutes|hours|days|weeks]
		     ex> now + 5 minutes      ex> 04pm + 3 days
		就是说，在某个时间点[再加几个时间后]才执行。

	列
	# 再过五分钟，将/root/.bashrc发给root自己
	at now + 5 minutes
	/bin/mail -s "testing at job" root < /root/.bashrc
	<EOT> <==这里输入[ctrl]+d就会出现<EOF>的字样，代表结束
	# 我想在2015/08/04 23:00关机
	at 23:00 2015-08-04
	/bin/sync
	/bin/sync
	/sbin/shutdown -h now
	<EOT>
3. at任务管理
	语法
	atq              <==查看
	atrm (jobnumber) <==删除
	列
	atq
	atrm 3
4. batch：系统有空时才执行后台任务
	说明
	1. 他是cpu的任务负载小于0.8的时候，才执行你的工作任务
	2. Cenos7下面的batch不再支持时间参数
	
二. 循环执行的计划任务
	说明
	1. 当用户使用crontab这个任务建立计划任务之后，该项任务就会被记录到/var/spool/cron/中
	2. cron执行的每一项任务都会被记录到/var/log/cron这个日志文件中
	语法
	crontab [-u username] [-l|-e|-r]
	选项与参数
	| -u | 只有root才能执行这个任务，亦即帮其他使用者建立/删除crontab计划任务
	| -e | 编辑crontab的任务内容 |
	| -l | 查看crontab的任务内容 |
	| -r | 删除所有的crontab的任务内容，若仅要删除一项，请用-e编辑 |
	列
	#用wxq的身份在每天12:00发信给自己
	crontab -e
	0 12 * * * mail -s "at 12:00" wxq < /home/wxq/.bashrc	

	crontab表达式
	| 代表意义 | 分钟  | 小时 | 日期  | 月份  | 周   | 命令         |
	| 数字范围 | 0~59 | 0~23 | 1~31 | 1~12  | 0~7 | 需要执行的命令 |
	特殊符号
	| *（星号）| 代表任何时刻都接收的意思，列日、月、周都是*，表示何月、何日都执行 |
	| ,（逗号）| 代表分隔时间段的意思，列执行3:00与6:00时,0 3,6 * * * command |
	| -（减号）| 代表一点范围内，列8点到12点之间没小时20分钟执行，20 8-12 * * * command |
	|/n（斜线）| n代表数字，【每隔n单位间隔】意思，列，没五分钟执行一次，*/5 * * * * |
	其他
	如果每个流程都在同一个时间启动的话，那么在某个时段，系统会变得相当繁忙
	vim /etc/crontab
	1,6,11,16,21,26,31,36,41,46,51,56 * * * * root CMD1
	2,7,12,17,22,27,32,37,42,47,52,57 * * * * root CMD2
	3,8,13,18,23,28,33,38,43,48,53,58 * * * * root CMD3
	4,9,14,19,24,29,34,39,44,49,54,59 * * * * root CMD4

三. 可唤醒停机期间的工作任务
	说明：什么是anacron
	1. 24小时运行的Linux系统所执行的crontab，以及因为某些原因导致的超过时间而没有被执行的任务
	2. anaron会没小时执行一次
	语法
	anacron [-sfn] [job]..
	anacron -u [job]..
	选项与参数
	| -s | 开始连续地执行各项任务（job），会根据时间记录文件的数据判断是否执行 |
	| -f | 强制执行，而不去判断时间记录文件时间戳 |
	| -n | 立马执行为执行的任务，而不延迟（delay）等待时间 |
	| -u | 仅更新时间记录文件的时间戳，不执行任何任务 | 
	|job | 有/etc/anacrontab定义的各项任务名称 |
```

#第十六章 进程管理与SELinux
```
一. 什么是进程（process）
    说明
    1. 一个程序被加载到内存当中运行，那么内存中的那个数据就被称为进程（process）
    2. 触发任何一个事件时，系统都会将他定义成为一个进程，并且给予这个进程一个ID，称为PID

二. Linux的多人多任务环境
    功能
    1. 多人环境
    2. 多任务操作
    3. 多重登陆环境的七个基本终端界面（ [Alt] + [F1]...[F7]切换 ）
    4. 特殊的进程操作
    5. bash环境下的任务管理（job control）

三. 任务管理（job control）
1.  直接将命令丢到后台中【执行】的 &
    语法
    [command] & <==&符号在最后面
2.  查询目前后台任务状态：jobs
    语法
    jobs [-lrs]
    选项与参数
    | -l | 除了列出job number与命令串之外，同时列出PID的号码 |
    | -r | 仅列出正在后台run的任务 |
    | -s | 仅列出正在后台当中暂停（stop）的任务 |
3.	将后台任务拿到前台来处理：fg
    语法
    fg %jobnumber
    选项与参数
    | %jobnumber | jobnumber为任务号码（数字），%可有可无 |
4.  让任务在后台下的状态变成运行中：bg
    语法
    bg %jobnumber
5.  管理后台中的任务：kill
    语法
    kill -signal %jobnumber
    kill -l
    选项与参数
    | -l     | 这个是I的小写，列出目前kill能够使用的信号(signal)有哪些 |
    | sigmal | 代表给予后面接的那个任务什么样的指示，用man 7 signal可知。|
    | -1     | 重新读取一次参数的配置文件(类似reload)。|
    | -2     | 代表由键盘输入[ctrl]-c同样的操作。|
    | -9     | 立刻强制删除一个任务。|
    | -15    | 以正常的进程方式终止一项任务，与-9是不一样的。|

四. 脱机管理问题
    说明
    任务管理的后台依旧与终端有关，如远程连接方式到你的linux主机，用&放到后台，在任务未结束脱机，任务会被中断。
    语法
    nohub [命令与参数]   <==在终端前台中任务
    nohub [命令与参数] & <==在终端后台中任务

五.	进程管理
    查看进程
1.  ps：将某个事件点的进程运行情况截取下来
    语法
    ps aux  <==查看系统所有的进程
    ps -lA  <==也是能查看所有系统的进程
    ps axjf <==连同部分进程数状态
    选项与参数
    | -A | 所有进程均显示出来，与-e具有同样效果 |
    | -a | 不显示与终端有关的所有进程 |
    | -u | 有效使用者（effective user）相关进程 |
    | x  | 通常与a这个参数一起用，可列出完整信息 |
    输出格式规则
    | l  | 较长，较详细的将该PID的信息输出 |
    | j  | 任务的格式（jobs format）
    | -f | 做一个更为完整的输出 |
2.  仅查看自己的bash相关进程：ps -l
    说明
    S：代表这个进程的状态（STAT）
3.  查看系统所有进程：ps aux
    说明
4.  top：动态查看进程的变化
    语法
    top [-d 数字] | top [-bnp]
    选项与参数:
    | -d | 后面可以接秒数，就是整个进程界面更新的秒数。默认是5秒。|
    | -b | 以批量的方式执行top,还有更多的参数可以使用，通常会搭配数据流重定向来将批量的结果输出为文件。|
    | -n | 与-b搭配，意义是，需要执行几次top的输出结果。|
    | -p | 指定某些个PID来执行查看监测而已。|
    在top执行过程当中可以使用的按键命令:
        ?: 显示在top当中可以输入的按键命令。
        p: 以CPU的使用排序显示。
        M: 以Memory的使用排序显示。
        N: 以PID来排序。
        T: 由该进程使用的CPU时间累积(TIME+)排序。
        k: 给予某个PID一个信号(signal)
        r: 给予某个PID重新制订一个nice值。
        q: 退出top的按键。
5.  pstree
    语法
    pstree [-A|U] [-up]
    选项与参数
    | -A | 各进程树之间的连接以ASCII字符来连接。|
    | -U | 各进程树之间的连接以Unicode的字符来连接，在某些终端界面下可能会有错误。|
    | -p | 并同时列出每个进程的PID。|
    | -u | 并同时列出每个进程的所属账号名称。|

6.  killall -signal 命令名称
    语法
    killall [-iIe] [command name]
    选项与参数
    | -i | interactive及互动的意思，若需要删除时，会出现提示字符给使用者 |
    | -e | exact的意思、表示【后面接的command name要一致】，整个命令不能超过15个字符 |
    | -I | 命令名称（可能含参数）忽略大小写 |
    列
    killall -9 httpd
    killall -i -9 httpd
    # 进程给与一个SIGHUP信号
    killall -l rsyslogd 
	
六. 关于进程的执行顺序
1.  Priority与Nice值
    说明
    1. 我们Linux 给予进程一一个所谓的[优先级(priority, PRI)], 这个PRI值越低代表越优先的意思。不过这个PRI值是由内核动态调整的，用户无法直接调整PRI值。
    2. 虽然nice值可以影响PRI，但最终的PRI仍是经过系统分析才会决定的。
    3. PRI越小则越早被执行，当nice值为负数时，那么该进程就会降低PRI值，即会变得较有限被处理。
    nice就是NI，PRI与NI相关性：
    PRI(new) = PRI(old) + nice
    nice设置留意：
    1. nice值可调整的范围为-20~19;
    2. root可随意调整自己或它人进程的nice值，且范围为-20~19;
    3. 一般用户仅可调整自己进程的nice值，且范围仅为0~19(避免- 般用户抢占系统资源);
    4. 般用户仅可将nice值越调越高， 例如本来nice为5,则未来仅能调整到大于5。
2.  nice：新执行的命令给予新的nice值	
    语法
    nice [-n 数字] command
    选项与参数
    | -n | 后面接一个数值，数值范围-20~19 |
    列
    nice -n -5 vim &
3.  renice：已存在进程nice重新调整
    renice [number] PID
    列
    renice -5 14836

七. 查看系统资源信息
1.  free：查看内存使用情况
    语法
    free [-b|-k|-m|-g|-h] [-t] [-s N -c N]
    选项与参数
    | -b | 直接输入free时，显示的单位是KBytes,我们可以使用b (Bytes)、m (MBytes)、k (KBytes)
        及g (GBytes)来显示单位，也可以直接让系统自己指定单位(-h)。|
    | -t | 在输出的最终结果，显示物理内存与swap的总量。|
    | -s | 可以让系统不断刷新显示数据，对于系统查看挺有效。|
    | -c | 与-s同时处理，让free列出几次的意思。|
    说明
    1. Mem哪一行显示的是物理内存量
    2. Swap则是内存交换分区量
    3. total是总量
    4. used是已被使用量
    5. free则是剩余可用的量
    6. shared、buffers、cached是已被使用量中，用作为缓冲及缓存的，因此后面有一个available（可用的）数值
2.  uname：查看系统与内核相关信息
    语法
    uname [-asrmpi]
    选项与参数
    | -a | 所有系统相关的信息，包括下面的数据都会被列出来 |
    | -s | 系统内核名称 |
    | -r | 内核的版本 |
    | -m | 本系统的硬件架构，例如i686或x86-64 | 
    | -p | CPU的类型，与-m类似，只是显示的是CPU的类型 |
    | -i | 硬件的平台（x86）|
3.  uptime：查看系统启动时间与任务负载
4.  netstat：追踪网络或socket文件【用于网络监控方面】
    语法
    选项与参数
    | -a | 将目前系统上所有的连接、监听、socket信息都列出来。|
    | -t | 列出tcp网络封包的信息。|
    | -u | 列出udp网络封包的信息。|
    | -n | 不以进程的服务名称，以端口号(port number)来显示。|
    | -l | 列出目前正在网络监听(listen)的服务。|
    | -p | 列出该网络服务的进程PID。|
    列
    # 找出目前系统已在监听的网络连接及其PID
    netstat -tulnp
5.  dmesg: 分析内核产生的信息
    列
    # 输出所有的内核启动时信息
    dmesg | more
    # 查找启动的时候，硬盘相关信息是什么
    dmesg | grep -i vda
6.  vmstat：检测系统资源变化
    语法
    vmstat [-a] [延迟[总计检测次数]] <==CPU/内存等信息
    vmstat [-fs]				   <==内存相关
    vmstat [-S单位]				   <==设置显示数据的单位
    vmstat [-d] 				   <==与磁盘有关
    vmstat [-p分区]				   <==与磁盘有关
    选项与参数
    | -a | 使用inactive/active(活动与否)替换buffer/cache内存输出信息 |
    | -f | 开机到目前为止，系统复制(fork)的进程数。|
    | -s | 将一些事件 (启动至目前为止)导致的内存变化情况列表说明。|
    | -S | 后面可以接单位，让显示的数据有单位，例如K/M替换Bytes的容量。|
    | -d | 列出磁盘的读写总量统计表。|
    | -p | 后面列出分区，可显示该分区的读写总量统计表。|
    列
    # 统计目前主机cpu状态，每秒一次，共计3次。
    vmstat 1 3

八. 特殊文件与进程
1. /proc/*代表的意义
    说明
    1. 进程都是在内存当中，而内存当中的数据又都是写入到/proc/*这个目录下
    目录下文件意思
    省略

九. 查询已使用文件或已执行进程使用的文件
1.  fuser：借由文件（或文件系统）找出正在使用该文件的进程
    语法		
    fuser [-umv] [-k [i] [-signal]] file/dir
    选项与参数
    | -u | 除了进程的PID之外，同时列出该进程的拥有者。|
    | -m | 后面接的那个文件名会主动地上提到该文件系统的最顶层，对umount不成功很有效 |
    | -v | 可以列出每个文件与进程还有命令的完整相关性。|
    | -k | 找出使用该文件/目录的PID,并试图以SIGKILL(-9)这个信号给予该PID |
    | -i | 必须与-k配合，在删除PID之前会先询问使用者意愿。|
    列
    # 找出目前所在目录的使用PID/所属账号/权限是什么?
    fuser -uv .
    说明
    ACCESS项目代表意义
    1. c: 此进程在当前的目录下（非子目录）
    2. e: 可被出发为执行状态
    3. f: 是一个被开启的文件
    4. r: 代表顶层目录（root directory）
    5. F: 该文件被使用了，不过在等待响应中
    6. m: 可能为共享的动态函数库

2.  lsof：列出被进程所使用的文件名称
    语法
    lsof [-aUu] [+d]
    选项与参数
    | -a | 多项数据需要【同时成立】才显示出结果时 |
    | -U | 仅列出UNIX-like系统的socket文件类型 |
    | -u | 后面接username，列出该使用者相关进程所使用的文件 |
    | +d | 后面接目录，亦即找出某个目录下面已经被使用的文件 |
    列
    # 列出系统所有已经被开启的文件与设备
    lsof
    # 仅列出关于root的所有进程所使用的socket文件
    lsof -u root -a -U

3.  pidof：找出某个正在执行的进程的PID
    语法
    pidof [-sx] program_name
    | -s | 仅列出一个PID而不列出所有PID |
    | -x | 同时列出该program_name可能的PPID那个进程PID |
    列
    pidof systemd rsyslogd

十. SELinux初探
1.  什么是SElinux
    说明
    1. 当初设计的目标,避免资源的误用
    2. SELinux是整合到内核的一个模块
    3. 强制访问控制（MAC）
省略-后面记录
```

#第十七章 认识系统服务（daemon）
```
一. 什么是daemon与服务（service）
	说明
	1. 系统为了某些功能必须提供一些服务（不论是系统本身还是网络方面），这个服务就称为service
	2. daemon是一个程序执行后的进程

二. 通过systemctl管理服务
1.	systemctl管理单一服务（service unit）的启动/开机启动与查看状态
	语法
	systemctl [command] [unit]
	command主要有：
	| start     | 立刻启动后面接的unit。|
	| stop      | 立刻关闭后面接的unit。|
	| restart   | 立刻重新启动后面接的unit，亦即执行stop再start的意思。|
	| reload    | 不关闭后面接的unit的情況下，重新加载配置文件，让设置生效。|
	| enable    | 设置下次开机时，后面接的unit会被启动。|
	| disable   | 设置下次开机时，后面接的unit不会被启动。|
	| status    | 目前后面接的这个unit的状态，会列出有没有正在执行、开机默认执行与否、登录等信息等。|
	| is-active | 目前有没有正在运行中。|
	| is-enable | 开机时有没有默认要启用这个unit。|
	列
	# 查看atd服务状态
	systemctl status atd.service
	常见状态信息：
	目前状态信息：
	acive (runing):正有一个或多个进程正在系统中运行的意思，举例来说，正在运行中的vsftpd就是这种模式。
	active (exited): 
		仅执行一次就正常结束的服务，目前并没有任何进程在系统中执行。
		举例来说，开机或是挂载时才会执行一一次的quotaon功能，就是这种模式。quotaon不需一直运行，只需执行一次之后，就交给文件系统去自行处理。
		通常用bash shell写的小型服务，大多是属于这种类型(无须常驻内存)。
	active (waiting): 
		正在运行当中，不过还需等待其他的事件发生才能继续运行。
		举例来说，打印的队列相关服务就是这种状态。虽然正在启动中，
		不过，也需要真的有队列进来(打印作业)这样它才会继续唤醒打印机服务来进行下一步的打印功能。
	inactive: 这个服务目前没有运行的意思。
	默认状态信息：
	enabled:; 这个daemon将在开机时被运行。
	disabled: 这个daemon在开机时不会被运行。
	static:这个daemon不可以自己启动(不可enable)，不过可能会被其他的enabled的服务来唤醒(依赖属性的服务)。
	mask:这个daemon无论如何都无法被启动，因为已经被强制注销(非删除)。可通过systemctl unmask方式改回默认状态。

2.  强迫服务注销（mask）的练习
	说明
	1. mask注销操作，只是让启动的脚本成空的设备而已
	2. 非正规关闭服务
	3. 应用于：关闭了一个服务，但是关闭这个下的依赖服务可能会启动。
	列
	systemctl stop cups.servcie
	systemctl mask cups.service
	systemctl status cups.service
	systemctl ummask cups.service

3.	通过systemctl查看系统上所有的服务
	语法
	systemctl [command] [--type=TYPE] [--all]
	command:
		list-units		: 依据unit显示目前有启动unit，若加上--all才会列出没启动的。
		list-unit-files : 依据/usr/lib/systemd/system/内的文件，将所有文件列表说明
	--type=TYPE：unit类型，主要有service，socket，target等
	列
	# 列出系统上面有启动的unit
	systemctl
	systemctl list-units --type=service --all

4.	通过systemctl管理不同的操作环境
	说明
	操作界面性相关性比较高的target：
	1. graphical.target: 命令加图形界面
	2. multi-user.target: 纯命令行模式
	语法
	systemctl [command] [unit.target]
	选项与参数：
	command:
		get-default: 取得目前target
		set-default：设置后面接的target成为默认的操作模式
		isolate	   ：切换到后面接的模式
	列
	systemctl get-default
	systemctl set-default multi-user.target	
	列：其他
	systemctl poweroff  系统关机。
	systemctl reboot    重新开机。
	systemctl suspend   进入挂起模式。
	systemctl hibernate 进入休眠模式。
	systemctl rescue    强制进入恢复模式。
	systemctl emergency 强制进入紧急恢复模式。

5.  通过systemctl分析各服务之间的依赖性
	语法
	systemctl list-dependencies [unit] [--reverse]
	选项于参数：
	| --reverse | 反向追踪谁使用这个unit意思 |
	列
	systemctl list-dependencies default.target
	
6.	与systemd的daemon运行过程相关的目录简介
	列
	# socket服务文件放置哪里
	systemctl list-sockets
	# 查看服务与端口对应
	cat /etc/services

三. systemctl针对service类型的配置文件
四. systemctl针对timeer的配置文件
五. CentOs7.x默认启动的服务概要
```

#第十八章 认识与分析日志文件
```
一. 日志文件内容的一般格式
	说明
	每条信息记录几个重要内容：
	1. 时间发生的日期与时间
	2. 发生此事件的主机名
	3. 启动此事件的服务名称（如systemd、crond等）或命令与函数名称（如su、login...）
	4. 该信息的实际内容

二. rsyslog.service: 记录日志文件的服务
	列
	ps aux | rsyslog.service

三. rsyslog.service的配置文件：/etc/rsyslog.conf
	说明
	1. 什么服务
	2. 什么等级信息
	3. 需要被记录在哪里
1.  等级信息

四. 日志文件的安全性设置
	说明
	1. rsyslog的日志文件只要【被编辑过】就无法继续记录。
	2. 我们用chatt使用a这个属性
	列
	chattr +a /var/log/admin.log
	lsattr /var/log/admin.log
	chattr -a /var/log/admin.log

五. 日志文件服务器的设置
	说明
	1. 默认的端口是UDP或TCP的514端口
	2. 服务器会启动监听的端口，客户端则将日志文件再传出一份到服务器
	列
   【服务端】
	vim /etc/rsyslog.conf
	# 找到下面这几行
	# Provides UDP syslog reception
	#SModLoad imudp
	#SUDPServerRun 514
	# Provides TCР syslog reception
	#SModLoad imtcp
	#$InputTCPServerRun 514
	# 上面是UDP端口，下面是TCP端口。
	# 如果网络状态很稳定，就用UDP即可
	# 想要数据比较稳定传输，建议用TCP
	# 修改下面两行
	SModLoad imtcp
	$InputTCPServerRun 514
	# 重新启动与查看rsyslogd
	systemctl restart rsyslog.service
	netstat -ltnp | grep syslog
   【客户端】	
	vim /etc/rsyslog.conf
	*.* @@192.168.1.100 # 指向服务端IP
	#*.* @192.168.1.100 # 若用UDP传输，设置要变这样
	
六. 日志文件的轮询（logrotate）
	省略

七. systemd-journald.service简介
	说明
	1. systemd-journald使用内存的日志文件记录方式，重启后，日志信息不会被记录
	2. 开机启动过程中的信息，包括启动服务与服务若启动失败的情况等，会被记录到systemd-journald
	语法
	journalctl [-nrpf] [--since TIME] [--until TIME] _optional
	选择与参数
	|    | 默认会显示全部log内容，从就得输出到最新得信息 |
	| -n | 显示最近几行得意思，找最新得信息想到有用 |
	| -r | 反向输出，从最新得输出到最旧的信息 |
	| -p | 显示后面所接的信息重要性排序，参考rsyslogd信息 |
	| -f | 类似tail -f的功能，持续显示journal日志的内容（及时监控）|
	--since --until: 设置开始与结束时间，在规定时期输出
	_SYSTEMD_UNIT=unit.service : 只输出unit.service的信息而已。
	_COOM=bash : 只输出与bash有关的信息。
	_PID=pid   : 只输出此PID号的信息。
	_UID=uid   : 只输出此UID的信息。
	SYSLOG_FACILITY=[0-23] : 使用syslog.h规范的服务相对序号来调用出正确的数据。
	列
	journalctl --since "2015-08-18 00:00:00" --until "2015-08-19 00:00:00"
	journalctl --since today
	journalctl --since yesterday --until today

八. logger命令的应用
	说明
	1. 让你的数据存储到日志文件当中
	语法
	logger [-p 服务名称.等级] "信息"
	选项与参数
	| 服务名称.等级 | 前一节【信息等级】|
	列
	logger -p user.info "I will check logger command"

九. 分析日志文件
1.  CenOS默认提供的logwatch
  
```
#第十九章 启动流程、模块管理与Loader【省略】

#第二十章 基础系统设置与备份策略
```
一. 网络设置（手动设置DHCP自动获取）
	语法
	nmcli connection show [网卡名称]
	nmcli connection show
	意思
	# NAME   连接代号，通常在后面网卡DEVICE会一样
	# UUID   这个是特殊的设备代码，保留九号不要理他
	# TYPE   就是网卡的类型，通常就是以太网卡
	# DEVICE 网卡名称
	列
	# 设置手动参数【省略】
	# 设置自动获取IP参数
	nmcli connection modify eth0 \
	> connection.autoconnect yes \
	> ipv4.method auto
	nmcli connection up eth0
	nmcli connection show eth0

二. 修改主机名称
	语法
	hostnamectl [set-hostname 你的主机名称]
	列
	#显示主机名
	hostnamectl
	#修改主机名
	hostnamectl set-hostname www.wxq.com

三. 日期时间设置
	1. 时间的显示与设置
	说明
	1. 中国地区时间九比格林威治多了8个小时
	语法
	timedatectl [command]
	选项与参数
	| list-timezones | 列出系统上所有支持的时区名称 |
	| set-timezone   | 设置时区位置 |
	| set-tme        | 设置时间 | 
	| set-ntp        | 设置网络校时系统 |
	列
	#显示目前的时区与时间信息
	timedatectl
	timedatectl list-timezones | grep -i new
	2. 时间调整
	列
	timedatectl set-name "2015-10-10 12:02:00"
	3. 用ntpdate手动网络校时
	列
	ntpdate s2m.time.edu.cn <==北京大学提供的服务器
	
四. 语系设置localectl【省略】
五. 防火墙简易设置【省略:图文的】
1.  教程：https://www.cnblogs.com/xxoome/p/7115614.html

六. 服务器硬件数据的收集
1.  使用dmidecode查看硬件设备
	语法
	dmidecode -t type
	选项与参数
	详细type选项man dmidecode查询	
	| 1 | 详细的系统信息，含主板型号与硬件的基础信息等 |
	| 4 | CPU的相关信息，包含倍频、外频、内核数、内核线程等。|
	| 9 | 系统的相关插槽格式，包含PCI、PCI-E等的插槽规格说明。|
	|17 | 每一个内存插槽的规格，若有内存，则列出该内存的容量与型号。|

2.  lspci：【查看PIC接口设备】
	语法
	lspci [-vvn]
	选项与参数
	| -v  | 显示更多的PCI设备的详细信息 |
	| -vv | 比-v还要更详细的详细信息 | 
	| -n  | 直接查看PCI的ID而不是厂商名称 |
	更新
	update-pciids

3.  lsusb：【查询多少个USB设备】
	语法
	lsusb [-t]
	选项与参数
	| -t | 使用类似树状目录来显示各个USB端口的相关性 |

4.  iostat：【查询整台机器的存储设备】
	说明
	1. 要安装 yum install sysstat
	语法
	iostat [-c|-d] [-k|-m] [-t] [间隔秒数] 【检测次数】
	选项与参数
	| -c | 仅显示CPU的状态 |
	| -d | 仅显示存储设备的状态，不可与-c一起用 |
	| -k | 默认显示的是block，这里可以改KBytes的大小显示 |
	| -m | 与-k类似，只是以MB的单位来显示结果 |
	| -t | 显示日期出来 |

5.  了解磁盘的监控状态
	列
	smartctl -a /dev/sda
	smartctl -t short /dev/sda

七. 备份要点
1.  必要备份的文件
	1. /etc/ 整个目录
	2. /home/ 整个目录
	3. /var/spool/mail/
	4. /var/spoll/{at | cron}
	5. /boot/
	6. /root/
	7. 自己的创建的目录文件，比如 /data
	其他省略	
```
#第二十一章 软件安装：源代码与Tarball
#第二十二章 软件安装RPM、SRPM、与YUM
#第二十三章 X Window设置介绍
#第二十四章 Linux内核编译与管理

# linux设置
```
一. CentOS7设置笔记本合盖不休眠
	说明
	1. CentOS7下对应配置文件，目录为：/etc/systemd/logind.conf
	意思
	HandlePowerKey：     按下电源键后的行为，默认power off
	HandleSleepKey：     按下挂起键后的行为，默认suspend
	HandleHibernateKey： 按下休眠键后的行为，默认hibernate
	HandleLidSwitch：    合上笔记本盖后的行为，默认suspend
	值
		ignore    忽略，跳过
		power off 关机
		eboot     重启
		halt      挂起
		lock      仅锁屏，计算机继续工作
	列
	# 查看系统配置文件
	grep -r HandleLidSwitch /etc/systemd/logind.conf
	# 更改配置并重启服务
	sed -i 's/#HandleLidSwitch=suspend/HandleLidSwitch=lock/g' /etc/systemd/logind.conf
	systemctl restart systemd-logind.service

二. 如何查看linux系统的体系结构
	语法
	arch
	意思
	aarch64 <==ARM架构
	x86_64  <==X86架构 

三. Linux启动ssh服务
	https://www.cnblogs.com/furzoom/p/7710288.html
```

# 安装常用软件
```
一. 上传下载
	yum -y install lrzsz
	语法
	rz filename
	sz filename

二. rsnapshot和增量备份
	https://ubuntuqa.com/article/8173.html
	远程文件系统备份工具 Rsnapshot
	https://my.oschina.net/HankCN/blog/186953
	10个方法助你轻松完成 Linux 系统恢复
	https://linux.cn/article-7205-1.html

三. Shell脚本加密与解密
	https://cloud.tencent.com/developer/article/1158567
```


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

#问题
	无法启动网络管理器在线等待
		引导至恢复模式，打开终端并使用： sudo systemctl disable NetworkManager-wait-online.service
	连接wifi
		https://blog.csdn.net/armybase11/article/details/52725058
		https://blog.csdn.net/wannahaha/article/details/108124294
	xshell的快捷键（非常实用）
		https://blog.csdn.net/qq_39785418/article/details/93322530
	linux命令查询: https://man.linuxde.net/
	netstat -ntlp
	netstat -tulpn
    lsof -i tcp:80
	systemctl restart network