## 一 安装  

```
#2.6.2版本
yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel
tar -xvf git-2.6.2.tar.gz -C /data/soft/git_tmp_v2.6.2 --strip-components 1
/data/soft/git_v2.6.2
./configure --prefix=/usr/local

#2.9.5版本
yum -y install curl-devel expat-devel gettext-devel openssl-devel zlib-devel perl-ExtUtils-MakeMaker
wget https://mirrors.edge.kernel.org/pub/software/scm/git/git-2.9.5.tar.gz
mkdir -p /data/soft/git_v2.9.5
tar -xvf git-2.9.5.tar.gz -C /data/soft/git_v2.9.5 --strip-components 1
cd /data/soft/git_v2.9.5
make prefix=/data/soft/git_v2.9.5 all
sudo make prefix=/data/soft/git_v2.9.5 install

#拉取仓库
git clone --bare tlblog tlblog.git
/data/project_warehouse/front/tlblog
git clone git@121.37.238.185:/data/project_warehouse/front/tlblog/tlblog.git
ln -s /data/soft/git_v2.6.2/bin/git-upload-pack /usr/bin/git-upload-pack
ln -s /data/soft/git_v2.6.2/bin/git-receive-pack /usr/bin/git-receive-pack
chown -R git:git tlblog.git
```

## 二 基础教程  

```
	cd ~/.ssh
	ls
	ssh-keygen -t rsa -C "1149697630@qq.com" //填写你的邮箱
	cat ~/.ssh/id_rsa.pub
	ssh -T git@gitee.com
	git init
	git remote add origin https://github.com/MMM360/document.git
	git remote add origin https://gitee.com/honor6/tlblog.git
	git remote -v
	命令git pull origin master (把分支上的文件拉下来)
	把要上传的项目文件拖入刚创建的文件夹内
	命令git add . 或 git add + 被拖入的项目名 （保存到缓存区）
	命令git commit -m "要编辑的内容" (推送到本地库中)
	命令git push origin master (推送到远端仓库也就是码云上)
	
	删除
	git remote rm origin
	
	注意：
	如果一个的时候只能默认生成密匙，不能另外起个名字，除非新建文件夹config,
	git pull --rebase origin master failed to push some refs to git => github中的README.md文件不在本地代码目录中
```

[Windows安装git和环境变量配置](https://blog.csdn.net/dietime1943/article/details/71751007)  
	
[初次使用git配置以及git如何使用ssh密钥（将ssh密钥添加到github）](https://www.cnblogs.com/superGG1990/p/6844952.html)  
	
[删除远程库中一个文件(夹)，本地库不删除](https://blog.csdn.net/J080624/article/details/83346991)    

[如何克服解决 Git 冲突的恐惧症？](https://www.zhihu.com/question/27507789)  
	
[git书籍](http://iissnan.com/progit/)  
	
[几款Git GUI客户端工具](https://blog.csdn.net/xiaoguanyusb/article/details/79611647)  
	
[问题]  
	Pull is not possible because you have unmerged files  
		https://blog.csdn.net/kingle123/article/details/83443506  
	使用 ssh 协议  
		https://todebug.com/tips/  
	git bash中文显示为数字
		https://www.cnblogs.com/songzhenhua/p/9312713.html  

