##导出映射代码 http://localhost:8080/api/generator/code?tableName=yb_notice&module=yb&smallModule=notice&buildVue=false
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
	
userName
要修改的
用户的字段不能随意修改-不能及时更新
explain
##别人业务
	1. 同城交友-炯阳
	2. 解封
	3. 充值总数字段-炯阳
	4. 礼物之前回掉-袁浩
	5. 签到-
	6. 等级功能-袁浩
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
		VALUES(REPLACE(UUID(),"-",""),'UserGiftDayTask','yb','每天几分几时用户发送礼物','com.qc.yb.task.UserGiftDayTask',1,'0 */3 * * * ?','每天几分几时用户发送礼物',NOW(),NULL);

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
	动态列表
	duration

推送功能
拿特殊账号ID-每天给10个人发消息(随机打扰消息) - 2女8男
新注册男性用户3天内-每天收到10个女生的消息

收到的时间段
登陆的时候-一条
早上9点-11点-随机时间发送
下午3点-五点
晚上8-10点

查询是否存在推送


两个值 一个值扣

480383c948914ad9b453ff2974209e17
769123c787304652a8b89876eb4350c4