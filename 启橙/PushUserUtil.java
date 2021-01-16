package com.qc.yb.util;

import com.qc.dframework.exception.NonePrintException;
import com.qc.dframework.util.SpringWebContextUtil;
import com.qc.dframework.util.ent.BaseUtil;
import com.qc.dframework.util.ent.RedisUtil;
import com.qc.im.entity.c2c.MsgBody;
import com.qc.im.entity.c2c.MsgContent;
import com.qc.yb.dto.im.C2CMessage;
import com.qc.yb.entity.platform.DisturbInfo;
import com.qc.yb.entity.user.AppUser;
import com.qc.yb.enums.DisturbInfoSexEnum;
import com.qc.yb.enums.ImMsgTypeEnum;
import com.qc.yb.enums.PushUserTimeEnum;
import com.qc.yb.enums.SexType;
import com.qc.yb.service.platform.ConfigService;
import com.qc.yb.service.platform.DisturbInfoService;
import com.qc.yb.service.user.AppUserService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
public class PushUserUtil {

    private AppUserService appUserService;
    private DisturbInfoService disturbInfoService;
    private ConfigService configService;
    Random rand = new Random();

    /**
     * 登陆的打扰
     */
    public void userLoginLog() {
        DisturbInfo disturbInfo = getDisturbInfoService().findBySex(DisturbInfoSexEnum.GIRL.getCode()); //打扰信息
        int girlInt = getConfigService().getPlatformConfig().getGirlPushBoy(); //接收女生数目

        List<AppUser> userLogList = new ArrayList<>(); //刚登陆
        for (AppUser userLog : userLogList) {
            C2CMessage c2CMessage = getRequestParam("480383c948914ad9b453ff2974209e17", userLog.getId(),
                    2, disturbInfo.getContent());
            try {
                net.sf.json.JSONObject jsonObject = ImApiUtil.sendC2CMsg(c2CMessage);
            } catch (Exception e) {
                throw new NonePrintException("登陆后推送消息失败");
            }
        }
    }

    //新用户收到女的打扰信息
    //新注册用户-还要设置天数
    public void pushUserFromUser() {
        int girlInt = getConfigService().getPlatformConfig().getGirlPushBoy() / 3; //男生接收除于3
        DisturbInfo disturbInfo = getDisturbInfoService().findBySex(DisturbInfoSexEnum.MAN.getCode()); //打扰信息
        Date date = new Date();
        int hour = date.getHours();
        if (1==1) {
//        if (PushUserTimeEnum.NINE_HOUR.getCode() <= hour && hour <= PushUserTimeEnum.ELEVEN_HOUR.getCode()) {
            List<AppUser> appUserRegisteredList = getAppUserService().getUserRegistered(3); //新注册用户-还要设置天数 -接收
            int randFrom = rand.nextInt(5);
            if (1==1) {
//            if (0 == randFrom && appUserRegisteredList.size() != 0) {
                List<AppUser> getRandomUserFush = getAppUserService().getRandomUserFush(SexType.GIRL.getCode(), girlInt); //发送
                for (AppUser userTo : appUserRegisteredList) {
                    List<AppUser> isUserRedis = RedisUtil.getValueList(userTo.getId()+"pushUser", AppUser.class);
                    if (isUserRedis == null || isUserRedis.size() == 0) {
                        RedisUtil.putValue(userTo.getId()+"pushUser",getRandomUserFush);
                    }else {
                        List<AppUser> userRedis = RedisUtil.getValueList(userTo.getId()+"pushUser", AppUser.class);
                        for (AppUser userForm : userRedis) {
//                            sendMsgPush(userForm.getId(), userTo.getId(), 2, disturbInfo.getContent());
//                            RedisUtil.getValueList(userTo.getId()+"pushUser", AppUser.class).remove(userForm);
                            userRedis.remove(userForm);
                            RedisUtil.delete(userTo.getId()+"pushUser");
                            RedisUtil.putValue(userTo.getId()+"pushUser",userRedis);
                            break;
                        }
                    }
                }
            }
        } else if (PushUserTimeEnum.FIFTEEN_HOUR.getCode() <= hour && hour <= PushUserTimeEnum.SEVENTEEN_HOUR.getCode()) {

        } else if (PushUserTimeEnum.TWENTY_HOUR.getCode() <= hour && hour <= PushUserTimeEnum.TWENTY_TWO_HOUR.getCode()) {

        }

    }

    //发送信息
    public void sendMsgPush(String formUserId, String toUserId, int typeSave, String content) {
        C2CMessage c2CMessage = getRequestParam(formUserId, toUserId,
                typeSave, content);
        try {
            net.sf.json.JSONObject jsonObject = ImApiUtil.sendC2CMsg(c2CMessage);
        } catch (Exception e) {
            throw new NonePrintException("登陆后推送消息失败");
        }
    }

    private static C2CMessage getRequestParam(String formUserId, String toUserId, Integer SyncOtherMachine, String text) {
        C2CMessage c2CMessage = new C2CMessage();
        List<MsgBody> msgBodyList = new ArrayList<>();
        MsgBody msgBody = new MsgBody();
        MsgContent msgContent = new MsgContent();
        c2CMessage.setFrom_Account(formUserId);
        c2CMessage.setTo_Account(toUserId);
        c2CMessage.setMsgRandom(new Random().nextInt(1000000) + 1);
        c2CMessage.setSyncOtherMachine(SyncOtherMachine);
        msgContent.setText(text);
        msgBody.setMsgType(ImMsgTypeEnum.TIMTextElem.name());
        msgBody.setMsgContent(msgContent);
        msgBodyList.add(msgBody);
        c2CMessage.setMsgBody(msgBodyList);
        return c2CMessage;
    }

    public AppUserService getAppUserService() {
        if (BaseUtil.isEmpty(appUserService)) {
            appUserService = SpringWebContextUtil.getApplicationContext().getBean("AppUserService", AppUserService.class);
        }
        return appUserService;
    }

    public DisturbInfoService getDisturbInfoService() {
        if (BaseUtil.isEmpty(disturbInfoService)) {
            disturbInfoService = SpringWebContextUtil.getApplicationContext().getBean("DisturbInfoService", DisturbInfoService.class);
        }
        return disturbInfoService;
    }

    public ConfigService getConfigService() {
        if (BaseUtil.isEmpty(configService)) {
            configService = SpringWebContextUtil.getApplicationContext().getBean("ConfigService", ConfigService.class);
        }
        return configService;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
                int hour = rand.nextInt(11)%(11-8+1) + 8;
                System.out.println(hour);
                Date date = new Date();
                date.setHours(hour);
                date.setMinutes(rand.nextInt(59));
                System.out.println(date.toString());
        }

    }

}
