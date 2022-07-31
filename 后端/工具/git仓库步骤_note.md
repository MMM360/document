一. 创建密匙  
	cd ~/.ssh
	ssh-keygen
二. 创建证书登录  
	mkdir /home/git/.ssh
	touch 700 /home/git/.ssh/authorized_keys
	把client端生成的密匙对公匙黏贴,1个公钥1行
	systemctl restart sshd.service
三. 创建账号  
	groupadd git
	useradd git -g git
四. 创建git服务器仓库  
	git init --bare tlblog.git
	chown -R git:git tlblog.git

	git clone ssh://git@121.37.238.185:221/data/project_warehouse/after/tlblog/tlblog.git

五. ssh免密码  
	vim /etc/ssh/sshd_config
	修改为PubkeyAuthentication yes（如果前面有#，就去掉#）
	
六. 创建钩子  
	cd /data/project_warehouse/after/tlblog/tlblog.git/hooks
	touch 700 post-receive
	vim post-receive
		#!/bin/sh 
		GIT_WORK_TREE=/data/project_warehouse/after/tlblog/tlblog git checkout -f
	chown -R git:git tlblog
七. 服务器pull  
	将服务器的~/.ssh/id_rsa.pub的值复制到/home/git/.ssh/authorized_keys

参考  
	搭建Git服务器  
	https://www.liaoxuefeng.com/wiki/896043488029600/899998870925664

