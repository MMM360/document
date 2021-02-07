Windows安装git和环境变量配置
	https://blog.csdn.net/dietime1943/article/details/71751007
初次使用git配置以及git如何使用ssh密钥（将ssh密钥添加到github）
	https://www.cnblogs.com/superGG1990/p/6844952.html
删除远程库中一个文件(夹)，本地库不删除
	https://blog.csdn.net/J080624/article/details/83346991
基础教程
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
#如何克服解决 Git 冲突的恐惧症？
	https://www.zhihu.com/question/27507789
#git书籍
	http://iissnan.com/progit/