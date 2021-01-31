##导出映射代码 http://localhost:8080/api/generator/code?tableName=yb_config_prompt&module=yb&smallModule=platform&buildVue=false
##吕哥SQL优化：https://blog.csdn.net/orecle_littleboy/article/details/88534160
##业务
	1. 根据用户ID查询兴趣列表 /api/mp/yb/my/classify/getInterestByUserId
	2. 获取用户动态评论/添加评论	/api/mp/yb/dynamicArticle/
		paranId 评论的ID
		rUser   回复用戶的Id
		加字段 
	3. 悬赏任务
		int intIndex = strOrig.indexOf("悬赏");
	4. 魅力/话痨/萌新/文章发布排行榜
	5. 兴趣列表接口/用户兴趣	/api/mp/yb/my/classify/getInterest
	6. 定时任务-女性特殊权限账号申请
		直接在线申请，管理员审核，目前申请条件：注册满7天，同10个人聊天过,申请通过后,聊天赚取好奇点,拥有提现权限等,条件是女的
	7. 动态通知
		0评论 1回复 2打赏 3动态通知 4点赞 5召唤
		用户评论之后 0评论 1回复同一个添加接口
		自己操作不用通知-评论-点赞/打赏 
		1. 评论与回复--addComment
		2. 打赏与点赞 -- giveReward
		3. 召唤--addSummon
		4. 动态发布-通知选中的人-addDynamic
		5. 评论打赏
		6. 左边显示评论内容或者评论图片-右边都显示动态第一张图片或者评论
		7. 视频拿第一帧
        //在缓存 通知 + 1
        //查询缓存通知的值
        //清除noticeCount

	8. 定时连续天数发送礼物 2020-12-26 17:00
	9. 感兴趣的小姐姐接口永远失效 2020-12-30
	10. 注册后女直接改为权限用户，连续登陆七天的女生 提现type 1 默认 0 注册女生就为权限用户,增加提现字段
	11. 在线客服 2020-1-4 17:30
		1.表情包替换，发送解析。用数据库的。 
	12. 推送
		1. 初版直接给符合特殊账号的女性用户每天推送10人，2女8男，随机匹配，男性用户推送是给新注册3天内的用户每天推送10个女生
		2. 根据图片
	13. 分享页面
问题
	1. 用户登陆app每天唯一条数据没数据-yb_app_user_login 没有数据-定时任务没有开启/还没对比数据-完成
		1. userIDList扣钱日志会不对-还没测试
	2. tomcat定时任务发送两条-数据库需要做配置
	3. app版本上传错误-未知-先上传到七牛云-手动-完成
	4. 动态通知图片用第一张没改好-完成
	5. 后台用户兴趣显示为空-完成
	6. 充值套餐后台没加负数验证
	7. 礼物-发送女的收不到钱-2021-01-18-完成
	8. 女性用户聊天不收费
	9. 升级为权限用户刷新缓存
	10. 通知在评论通知动态-完成
	11. 开始速配
	12. 分享动态没用户名称
FFMPEG
explain
##别人业务
	1. 同城交友-炯阳
	2. 解封
	3. 充值总数字段-炯阳
	4. 礼物之前回掉-袁浩
	5. 签到-
	6. 等级功能-袁浩-等级修改-炯阳
	7. 聊天扣钱-吕哥
	8. 修改密码
		1. token+手机号
		2. token30分钟
		3. 用完token删除
		4. 验证码正确返回token
	9. 首页动态列表排序逻辑更改:以每两小时为区间,点赞数+打赏数+评论数最大的3条排列最前,剩下的按时间排序-图片没有排序
	10. 验收当前用户手机号输入是否正确-炯阳
	11. 发送消息够30条扣钱-男扣-女不扣
		男发女-双方都扣钱
		男发特殊用户-双方都不扣钱
##SQL
		SELECT count(*) AS articleCount,A.userId,B.userName,B.avatarUrl
        FROM yb_user_dynamic_article AS A LEFT JOIN yb_app_user AS B ON A.userId = B.id
        WHERE A.userId in (SELECT distinct userId FROM yb_user_dynamic_article ) GROUP BY A.userId ORDER BY articleCount DESC LIMIT 20

		SELECT A.* FROM yb_classify AS A LEFT JOIN yb_user_classify AS B ON A.id = B.classifyId
		WHERE A.type=2 AND B.userId = 'e0e1e9b2e2114f6e97c96a9df3f392' ORDER BY A.createTime DESC  LIMIT 20

		SELECT A.*,B.userName FROM yb_user_dynamic_article_comment AS A LEFT JOIN yb_app_user AS B ON  A.userId = B.id
		WHERE  dynamicArticleId = #{param.dynamicArticleId} and parentId is NULL ORDER BY A.createTime DESC

        SELECT count(*) AS articleCount,uda.userId,u.userName,u.avatarUrl
        FROM yb_user_dynamic_article AS uda LEFT JOIN yb_app_user AS u ON uda.userId = u.id
        WHERE uda.userId in (SELECT distinct userId FROM yb_user_dynamic_article ) AND 
		DATE_FORMAT(uda.createTime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')  
		GROUP BY uda.userId ORDER BY articleCount DESC LIMIT 20
		
		INSERT INTO `qc_task_schedule` 
		VALUES(REPLACE(UUID(),"-",""),'UserDayCharmTask','yb','当天魅力值排行榜','com.qc.yb.task.UserDayCharmTask',0,'0 0 6 * * ?','当天魅力值排行榜',NOW(),NULL);

		INSERT INTO `qc_task_schedule` 
		VALUES(REPLACE(UUID(),"-",""),'UserArticleRankingTask','yb','当天文章发布数量排行榜','com.qc.yb.task.UserArticleRankingTask',0,'0 0 6 * * ?','当天文章发布数量排行榜',NOW(),NULL);
		
萌新		 SELECT * FROM yb_app_user WHERE to_days(now()) - to_days(createTime)+1 <= 3  ORDER BY daySendMessage DESC LIMIT 20
话痨		 SELECT * FROM yb_app_user WHERE to_days(now()) - to_days(createTime) >= 3  ORDER BY daySendMessage DESC LIMIT 20
		
		SELECT aul.*,(COUNT(*) = 7) = 1
		from yb_app_user_login aul LEFT JOIN yb_app_user au ON aul.userId = au.id 
		WHERE 
		au.userType = 0
		AND
		DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 7 DAY),'%Y-%m-%d')  < DATE_FORMAT(aul.date,'%Y-%m-%d') 
		AND 
		DATE_FORMAT(aul.date,'%Y-%m-%d')  <= DATE_FORMAT(NOW(),'%Y-%m-%d')
		GROUP BY aul.userId

		SElECT count(distinct fromUserId) AS countUser,toUserId FROM yb_user_message_log WHERE fromUserId IN (
		select fromUserId from yb_user_message_log  WHERE 
		fromUserId IN 
		(select toUserId from yb_user_message_log  WHERE fromUserId IN('264c1c613f8411eb9cac00e04c41d6d4'))
		)
		AND toUserId IN('264c1c613f8411eb9cac00e04c41d6d4') 

		select count(distinct fromUserId) AS countUser,toUserId from yb_user_message_log  WHERE 
		fromUserId IN 
		(select toUserId from yb_user_message_log  WHERE fromUserId IN('8b4d103fd57e49ab91f4583b3d2e0803','3956e212ac58438d8176925c02bcafba'))
		AND toUserId IN('8b4d103fd57e49ab91f4583b3d2e0803','3956e212ac58438d8176925c02bcafba')  
		GROUP BY toUserId

		INSERT INTO `yb_app_user_login` 
		VALUES(REPLACE(UUID(),"-",""),'e3d8a132ae4149ad84daa5190f17f68e','fdsf','fdsf',null,NOW());
		
		(REPLACE(UUID(),"-",""),#{item.userId},#{item.classifyId})

		INSERT INTO `qc_task_schedule` 
		VALUES(REPLACE(UUID(),"-",""),'UserPushTask','yb','推送-权限用户发送打扰信息','com.qc.yb.task.UserPushTask',1,'0 */10 * * * ?','推送-权限用户发送打扰信息',NOW(),NULL);

	SELECT yau.* FROM yb_app_user yau WHERE yau.id NOT IN(
		SELECT yun.noInterestUserId FROM yb_user_nointerest yun 
			WHERE yun.userId = 'b31ec92d322f4837bc79ba03d6b55271'
	)and yau.gender = 0
	ORDER BY RAND() LIMIT 3
		
##Cron表达式
	0 15 10 * * ?     每天上午10:15触发
	0 */3 * * * ?
	0 0 6 * * ?
	0 */1 * * * ? == 秒 分 时 天 周 年
	0 28 11 * * ? *
	REPLACE(UUID(),"-","")
	SEC_TO_TIME(createTime)
	BETWEEN

推送功能
	女生-每天给按配置个人发消息(随机打扰消息) - x女x男
	新注册男性用户x天内-每天收到x个女生的消息
	收到的时间段
	登陆的时候-一条
	早上9点-11点-随机时间发送
	下午3点-五点
	晚上8-10点
	两个值 一个值扣
	新注册的男生够 数目就不发

1，连续在线30分钟
2，连续在线120分钟
3，21点后连续在线240分钟 
4，60分钟内与5人对话 （60分钟内改每个小时去做计算的，奖励不叠加）
5，21点后120分钟内与5人对话 （120分钟改21点后两个小时算一次，奖励不叠加）
6,24小时内双方每互动30句话
  都是每天早上6点的定时计算前一天奖励


b7792831712b4fb08373e739b07f5811
769123c787304652a8b89876eb4350c4

