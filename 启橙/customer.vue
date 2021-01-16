<!-- 客服 -->
<template>
  <div v-loading="loading" class="qc-container">
    <div v-if="isLogin">
      <el-container class="im-box">
        <el-aside width="260px" class="im-user-box">
          <div class="sessionItem">最近联系人</div>
          <div @click="onSelSess(user.To_Account)"  v-for="user in userList" class="user-box clearfix" :class="{action:user.To_Account==selToID}">
            <div class="header_pic">
              <img :src="user.C2cImage+'?imageView2/2/w/50/h/50'" @error="errorAvator"  />
            </div>
            <div class="message-box">
              <div class="text">
                <div class="nickName">{{user.C2cNick}}</div>
                <div class="lastMsg">{{user.MsgShow }}</div>
              </div>
              <div class="tools">
                <div class="time">&nbsp;</div>
                <div v-if="user.UnreadMsgCount" class="num">{{user.UnreadMsgCount>99?"99+":user.UnreadMsgCount}}</div>
              </div>
            </div>
          </div>
        </el-aside>
        <el-container>
          <el-header class="right-header" height="40px">
            <div class="message-title">
              <i class="iconfont el-icon-erp-message mr20"></i>
              <span v-if="selToID">{{userList[userMap[selToID]].C2cNick}}</span>
            </div>
          </el-header>
          <el-main :disabled="!selToID" class="main-box">
            <div class="msgBox">
              <div v-for="msg in msgList">
                <div class="onemsg">
                  <div class="msghead" >
                    <p :class="msg.isSelfSend?'green':'blue'">
                      <img class='headurlClass' :src="msg.headerPic"  @error="errorAvator" />&nbsp;&nbsp;{{msg.nickName}}&nbsp;&nbsp;{{msg.time}}
                    </p>
                  </div>
                  <div class="msgbody">
                    <div v-html="msg.content"></div>
                    <div v-html="msg.spinner" v-if="msg.spinner"></div>
                  </div>
                </div>
              </div>
            </div>
          </el-main>
          <el-footer height="135px" class="footer">
            <div class="f-tools">
              <el-popover
                placement="top"
                width="400"
                trigger="click">
                <el-carousel :autoplay="false" indicator-position="outside" trigger="click" height="260px">
                  <el-carousel-item  class="picker-wrapper" v-for="(item,key) in pickerEmoji" :key="key">
                    <div class="emoji-wrapper">
                      <div @click="appendMsg(emoji[0],filePath+emoji[1])" class="emoji-item" v-for="emoji in item">
                        <img  class="emoji-gif"  :src="filePath+emoji[1]" />
                      </div>
                    </div>
                  </el-carousel-item>
                </el-carousel>
                <el-button slot="reference" class="p7 mr10 iconfont el-icon-erp-emoji cp" id="emojiId" ></el-button>
              </el-popover>
              <el-button class="p7 iconfont el-icon-erp-xingzhuang-tupian cp" @click="pictureVisit = true" ></el-button>
              <el-button class="p7 iconfont el-icon-erp-wenjianjia cp" @click="fileVisit = true" ></el-button>
            </div>
            <div class="f-info">
              <div contentEditable="true" @paste.prevent="handlePaste"
                   @keyup.native.enter="onSendMsg" class="msgedit" cols="82"
                   rows="5"  id="messageText" @input="onInput" ></div>
              <div class="sendbar">
                <button class="closebtn" @click="delChar()" type="button">删除会话</button>
                <button class="sendbtn" @click="onSendMsg()" type="button">发送</button>
              </div>
            </div>
          </el-footer>
        </el-container>
      </el-container>
      <div class="btn-box">
        <el-button class="btnOut" type="primary" @click="updateVisit = true">设置个人资料</el-button>
        <el-button class="btnOut" type="primary" @click="loginOut">退出登陆</el-button>

      </div>
    </div>
    <div v-else="">
      <div class="btn-box">
        <el-button class="btn" type="primary" @click="login">登陆客服</el-button>
      </div>
    </div>
    <!-- 图片上传 -->
    <el-dialog title="发送图片" :visible.sync="pictureVisit" width="50%" :show-close="false">
      <el-form ref="form" :modal="sendPicVO" label-width="80px">
        <el-form-item label="选择">
          <input id="upd_pic" @change="fileOnChange($event)" type="file">
        </el-form-item>
        <el-form-item label="预览">
          <img v-if="sendPicVO.previewUrl" class="previewImage" :src="sendPicVO.previewUrl"  :alt="sendPicVO.previewAlt"/>
        </el-form-item>
        <el-form-item label="进度">
          <el-progress :percentage="sendPicVO.percentage"></el-progress>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="pictureVisit = false">取 消</el-button>
        <el-button type="primary" :disabled="!sendPicVO.previewUrl" @click="uploadPic">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 发送文件 -->
    <el-dialog title="发送文件" :visible.sync="fileVisit" width="50%" :show-close="false">
      <el-form ref="form" :modal="sendFileVO" label-width="80px">
        <el-form-item label="选择">
          <input id="upd_file"  type="file">
        </el-form-item>
        <el-form-item label="进度">
          <el-progress :percentage="sendFileVO.percentage"></el-progress>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="fileVisit = false">取 消</el-button>
        <el-button type="primary"  @click="uploadFile">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 设置用户资料 -->
    <el-dialog title="发送文件" :visible.sync="updateVisit" width="50%" :show-close="false">
      <el-form ref="form" :modal="updateVO" label-width="80px">
        <el-form-item label="头像">
          <upload-img v-model="updateVO.headPic"></upload-img>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input  class="w300" v-model="updateVO.nickName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateVisit = false">取 消</el-button>
        <el-button type="primary"  @click="uploadInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import '../../../static/im/json2'
  import webim from '../../../static/im/webim'
  import UploadImgButton from '../../components/upload/UploadImgButton'
  import UploadImg from '../../components/upload/UploadImg'
  import emoji from '../../../static/im/emotions'


  var sdkAppID = '1400200009',
    accountType = '36862'
  var loginInfo = {
    'sdkAppID': sdkAppID, //用户所属应用id,必填
    'accountType': accountType, //用户所属应用帐号类型，必填
    'identifier': '', //当前用户ID,必须是否字符串类型，必填
    'userSig': '',
    //当前用户身份凭证，必须是字符串类型，必填
    'identifierNick': '', //当前用户昵称，不用填写，登录接口会返回用户的昵称，如果没有设置，则返回用户的id
    'headurl': '' //当前用户默认头像，选填，如果设置过头像，则可以通过拉取个人资料接口来得到头像信息
  }
  var selSess = null // 当前聊天会话对象
  var reqMsgCount = 15;//一次拉取消息数
  var getPrePageC2CHistroyMsgInfoMap = {}; //保留下一次拉取好友历史消息的信息

  export default {
    name: 'customer',
    components: {UploadImg, UploadImgButton},
    data () {
      return {
        loading: false,
        selType:webim.webim.SESSION_TYPE.C2C, // 当前聊天类型
        userList:[],
        userMap:{},
        msgList:[],
        selToID:null,// 当前选中聊天id（当聊天类型为私聊时，该值为好友帐号，否则为群号）
        selSess:null,// 当前聊天对象
        pictureVisit:false,
        sendPicVO:{percentage:0,previewUrl:'',previewAlt:''},
        fileVisit:false,
        sendFileVO:{percentage:0},
        activeName:'base',//当前表情框选中
        emotionMap:{},
        messageText:'',
        isLogin:false,
        updateVisit:false,
        updateVO:{headPic:'',nickName:''},
        pickerEmoji:emoji.pickerEmoji
      }
    },
    mixins: [tableBase],
    methods: {
      clearMessageText(){
        this.messageText = ''
        $("#messageText").html("");
      },
      onInput(event){
        this.messageText = $(event.srcElement).html();
      },
      handlePaste(e){},
      //IE9(含)以下浏览器用到的 jsonp 回调函数
      jsonpCallback (rspData) {
        //设置 jsonp 返回的
        webim.webim.setJsonpLastRspData(rspData)
      },
      //监听新消息事件 newMsgList 为新消息数组，结构为[Msg]
      onMsgNotify (newMsgList) {
        var that = this;
        var sess, newMsg
        // 获取所有聊天会话
        //var sessMap = webim.webim.MsgStore.sessMap()
        /*if (newMsgList.length != 0) {
          this.initRecentContactList()
        }*/

        this.$nextTick(()=>{
          setTimeout(()=>{
            for (var j in newMsgList) { // 遍历新消息
              newMsg = newMsgList[j];
              if(typeof newMsg=='object'){
                let selSess = newMsg.getSession()
                if (that.selToID&&selSess.id() == that.selToID) { // 为当前聊天对象的消息
                  that.selSess = selSess
                  that.addMsg(newMsg)
                  // 消息已读上报，以及设置会话自动已读标记
                  webim.webim.setAutoRead(that.selSess, true, true);
                }else{
                  if(that.userMap[selSess.id()]||that.userMap[selSess.id()]>=0){//用户会话窗口在
                    that.userList[that.userMap[selSess.id()]].UnreadMsgCount = selSess.unread();
                  }else{
                    that.addSess(selSess.type(), selSess.id(), selSess.name(), selSess.unread());
                  }
                }
              }
            }
          },300)
        })
      },
      //初始化聊天界面最近会话列表
      initRecentContactList () {
        this.selToID = null;
        this.userList = [];
        this.userMap = {};
        let that = this
        webim.webim.getRecentContactList({//获取会话列表的方法
          'Count': 50 //最近的会话数 ,最大为 100
        }, function (resp) {
          if (resp.SessionItem) {
            that.userList = resp.SessionItem;
            resp.SessionItem.forEach((obj,index)=>{
              that.$set(that.userMap,obj.To_Account,index);
            })
            webim.webim.syncMsgs(that.initUnreadMsgCount) // 初始化最近会话的消息未读数
            //初始化第一个
            //that.onSelSess(resp.SessionItem[0].To_Account);
          }
        }, function (resp) {
          //错误回调
        })
      },
      //初始化最近会话的消息未读数
      initUnreadMsgCount () {
        let that = this
        var sess;
        var sessMap = webim.webim.MsgStore.sessMap()
        for (var i in sessMap) {
          sess = sessMap[i];
          if(that.userMap[sess.id()]||that.userMap[sess.id()]>=0){
            that.userList[that.userMap[sess.id()]].UnreadMsgCount = sess.unread();
          }else{
            console.log(sess.id())
          }
        }
      },
      //切换用户
      onSelSess(to_id){
        var that = this;
        //当前消息清空
        this.msgList = [];

        //设置当前聊天对象
        this.selToID = to_id;
        this.$nextTick(()=>{
          this.bindScrollHistoryEventReset();
          let selSess = webim.webim.MsgStore.sessByTypeId(this.selType, to_id);
          this.selSess = selSess;
          //设置消息已读
          webim.webim.setAutoRead(selSess, true, false);
          //设置消息已读
          this.userList[this.userMap[this.selToID]].UnreadMsgCount = 0 ;
          var sessMap = webim.webim.MsgStore.sessMap(); //获取到之前已经保存的消息
          var sessCS = this.selType + this.selToID;
          if (sessMap && sessMap[sessCS]) { //判断之前是否保存过消息
            that.bindScrollHistoryEventInit();
            function compare(property) {
              return function(a, b) {
                var value1 = a[property];
                var value2 = b[property];
                return value1 - value2;
              }
            }
            var sessMapOld = sessMap[sessCS]._impl.msgs.sort(compare('time'));

            for (var i = 0; i < sessMapOld.length; i++) {
              that.addMsg(sessMapOld[i]);
            }
          } else {
            /*var sess = webim.webim.MsgStore.sessByTypeId(this.selType, this.selToID);
            if (sess && sess.msgs() && sess.msgs().length > 0) {
              that.getHistoryMsgCallback(sess.msgs());
            } else {
              that.getLastC2CHistoryMsgs(function(msgList) {
                that.getHistoryMsgCallback(msgList);
                that.bindScrollHistoryEventInit();
              }, function(err) {
                alert(err.ErrorInfo);
              });
            }*/
            //拉取漫游消息
            that.getLastC2CHistoryMsgs(function(msgList) {
              that.getHistoryMsgCallback(msgList);
              //绑定滚动操作
              that.bindScrollHistoryEventInit();
            }, function(err) {
              alert(err.ErrorInfo);
            });
          }
          setTimeout(function() {
            $('.msgBox').scrollTop($('.msgBox')[0].scrollHeight);
          }, 300);
        })
      },
      //发送消息
      addMsg(msg,prepend){
        let that = this;
        var isSelfSend, fromAccount, fromAccountNick, fromAccountImage, msgTime, subType,content;
        isSelfSend = msg.getIsSend();
        fromAccount = msg.getFromAccount();
        if (!fromAccount) {
          return;
        }
        if (isSelfSend) { //如果是自己发的消息
          if (loginInfo.identifierNick) {
            fromAccountNick = loginInfo.identifierNick;
          } else {
            fromAccountNick = fromAccount;
          }
          //获取头像
          if (loginInfo.headurl) {
            fromAccountImage = loginInfo.headurl;
          }
        } else { //如果别人发的消息
          var info = that.userList[that.userMap[fromAccount]];
          if (info && info.C2cNick) {
            fromAccountNick = info.C2cNick;
          } else if (msg.getFromAccountNick()) {
            fromAccountNick = msg.getFromAccountNick();
          } else {
            fromAccountNick = fromAccount;
          }
          //获取头像
          if (info && info.C2cImage) {
            fromAccountImage = info.C2cImage;
          } else if (msg.fromAccountHeadurl) {
            fromAccountImage = msg.fromAccountHeadurl;
          }
        }
        var spinner;
        if (msg.sending) {
          spinner = "<div class=\"bounce1\"></div> <div class=\"bounce2\"></div> <div class=\"bounce3\"></div>";
        } else {
          spinner = "";
        }
        msgTime = webim.webim.Tool.formatTimeStamp(msg.getTime());
        subType = msg.getSubType();
        switch (subType) {
          case webim.webim.GROUP_MSG_SUB_TYPE.COMMON: //群普通消息
            content = that.convertMsgtoHtml(msg);
            break;
          case webim.webim.GROUP_MSG_SUB_TYPE.REDPACKET: //群红包消息
            content = "[群红包消息]" + that.convertMsgtoHtml(msg);
            break;
          case webim.webim.GROUP_MSG_SUB_TYPE.LOVEMSG: //群点赞消息
            //业务自己可以增加逻辑，比如展示点赞动画效果
            content = "[群点赞消息]" + that.convertMsgtoHtml(msg);
            //展示点赞动画
            //showLoveMsgAnimation();
            break;
          case webim.webim.GROUP_MSG_SUB_TYPE.TIP: //群提示消息
            content = "[群提示消息]" + that.convertMsgtoHtml(msg);
            break;
        }
        var obj = {
          id:msg.random,
          isSelfSend:isSelfSend,
          nickName:fromAccountNick,
          headerPic:fromAccountImage,
          time:msgTime,
          content:content,
          spinner:spinner
        };
        if(prepend){
          this.msgList.splice(0,0,obj);
        }else{
          this.msgList.push(obj);
          setTimeout(function() {
            $('.msgBox').scrollTop($('.msgBox')[0].scrollHeight);
          }, 300);
        }
        if(this.selToID&&this.userMap&&this.userList&&this.userMap[this.selToID]&&this.userList[this.userMap[this.selToID]]){
          this.userList[this.userMap[this.selToID]].LastMsg.MsgBody[0].MsgContent.Text = content;
        }



      },
      //点击按钮发送文本
      onSendMsg(){
          if(!this.selToID){
            this.messageError("你还没有选中好友或者群组，暂不能聊天");
            return;
          }
          let msg = this.messageText;
          if(!msg&&msg.length< 1){
            this.messageError("发送的消息不能为空!");
            this.clearMessageText();
            return;
          }
          if(msg.length>webim.webim.MSG_MAX_LENGTH.C2C){
            this.messageError("消息长度超出限制(最多" + Math.round(webim.webim.MSG_MAX_LENGTH.C2C / 3) + "汉字);");
            return;
          }
         this.handleMsgSend(msg);
      },
      //新增一个会话
      addSess(sess_type, to_id, name, unread_msg_count){
        var that = this;
        if (that.userMap[to_id]||that.userMap[to_id]>=0) { //先判断是否存在会话DIV，已经存在，则不需要增加
          return;
        }
        let options =  {
          "To_Account":[to_id],
          "TagList":
            [
              "Tag_Profile_IM_Nick",
              "Tag_Profile_IM_Image"
            ]
        }
        webim.webim.getProfilePortrait(options,function(resp){
          that.userList.push(
            {
              To_Account: to_id,
              C2cNick:resp.UserProfileItem[0].ProfileItem?resp.UserProfileItem[0].ProfileItem[0].Value:to_id,
              C2cImage:resp.UserProfileItem[0].ProfileItem?resp.UserProfileItem[0].ProfileItem[1].Value:'',
              LastMsg:{MsgBody:[{MsgContent:{Text:''}}]},
              UnreadMsgCount:unread_msg_count
            }
          )
          that.$set(that.userMap,to_id,(that.userList.length-1));
        }, function(err) {
          alert(err.ErrorInfo);
        });

      },
      //删除会话
      delChar(){
        var data = {
          'To_Account': this.selToID,
          'chatType': this.selType
        }
        console.log(webim.webim.MsgStore.sessMap());
        webim.webim.deleteChat(
          data,
          (resp)=> {
            this.userList.splice(this.userMap[this.selToID],1);
            this.userMap = {};
            this.userList.forEach((obj,index)=>{
              this.$set(this.userMap,obj.To_Account,index)
            })
            webim.webim.MsgStore.delSessByTypeId(this.selType,this.selToID);
            this.msgList= [];
            this.selToID = "";
          }
        );
      },
      //滚动
      bindScrollHistoryEventInit(){
        var that = this;
        var msgflow = $(".msgBox");
        msgflow.scroll(function () {
          if (msgflow.scrollTop() == 0) {
            msgflow.scrollTop(10);
            if (that.selType == webim.webim.SESSION_TYPE.C2C) {
              that.getPrePageC2CHistoryMsgs();
            }
          }
        });

      },
      bindScrollHistoryEventReset(){
        $(".msgBox").unbind('scroll');
      },
      //------------------------------处理消息文本----------------------------------------------------
      convertMsgtoHtml: function convertMsgtoHtml (msg) {
        var that = this;
        var html = '',
          elems, elem, type, content
        elems = msg.getElems() //获取消息包含的元素数组
        var count = elems.length
        for (var i = 0; i < count; i++) {
          elem = elems[i]
          type = elem.getType() //获取元素类型
          content = elem.getContent() //获取元素对象
          if(!content){
            continue;
          }
          switch (type) {
            case webim.webim.MSG_ELEMENT_TYPE.TEXT:
              /*var eleHtml = that.convertTextMsgToHtml(content)
              //转义，防XSS
              html += webim.webim.Tool.formatText2Html(eleHtml)*/
              html += that.convertTextMsgToHtml(content)
              break
            case webim.webim.MSG_ELEMENT_TYPE.FACE:
              html += that.convertFaceMsgToHtml(content)
              break
            case webim.webim.MSG_ELEMENT_TYPE.IMAGE:
              /*if (i <= count - 2) {
                var customMsgElem = elems[i + 1] //获取保存图片名称的自定义消息elem
                console.log(customMsgElem,"customMsgElem")
                var imgName = customMsgElem.getContent().getData() //业务可以自定义保存字段，demo中采用data字段保存图片文件名
                html += that.convertImageMsgToHtml(content, imgName)
                i++ //下标向后移一位
              } else {

              }*/
              html += that.convertImageMsgToHtml(content);
              break
            case webim.webim.MSG_ELEMENT_TYPE.SOUND:
              // html += convertSoundMsgToHtml(content);
              html += that.convertSoundMsgToAMRPlayer(content)
              break
            case webim.webim.MSG_ELEMENT_TYPE.FILE:
              html += that.convertFileMsgToHtml(content)
              break
            case webim.webim.MSG_ELEMENT_TYPE.LOCATION:
              html += that.convertLocationMsgToHtml(content)
              break
            case webim.webim.MSG_ELEMENT_TYPE.CUSTOM:
              var eleHtml = that.convertCustomMsgToHtml(content)
              //转义，防XSS
              html += webim.webim.Tool.formatText2Html(eleHtml)
              break
            case webim.webim.MSG_ELEMENT_TYPE.GROUP_TIP:
              var eleHtml = that.convertGroupTipMsgToHtml(content)
              //转义，防XSS
              html += webim.webim.Tool.formatText2Html(eleHtml)
              break
            default:
              webim.webim.Log.error('未知消息元素类型: elemType=' + type)
              break
          }
        }
        return html
      },
      //解析文本消息元素
      convertTextMsgToHtml: function convertTextMsgToHtml (content) {
        if("web端暂不支持undefined消息" == content.getText()){
          return "";
        }
        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = content.getText().match(expr);
        console.log(emotions)
        if (!emotions || emotions.length < 1) {
          return content.getText();
        } else {
          var msgContent = content.getText();
          let tmsg,restMsgIndex,result="";
          for (var i = 0; i < emotions.length; i++) {
            tmsg = msgContent.substring(0, msgContent.indexOf(emotions[i]));
            if (tmsg) {
              result += tmsg;
            }
            var index = webim.webim.EmotionDataIndexs[emotions[i]];
            var emotion = webim.webim.Emotions[index];
            if (emotion && emotion[1]) {
              result += "<img src='" + filePath+emotion[1] + "?imageView/2/w/30/h/30'/>";
            }else{
              result += emotions[i];
            }
            restMsgIndex = msgContent.indexOf(emotions[i]) + emotions[i].length;
            msgContent = msgContent.substring(restMsgIndex);
          }
          if(msgContent){
            result += msgContent;
          }
          return result;
        }
      },
      //解析表情消息元素
      convertFaceMsgToHtml: function convertFaceMsgToHtml (content) {
        var faceUrl = null
        var data = content.getData()
        var index = webim.webim.EmotionDataIndexs[data]

        var emotion = webim.webim.Emotions[index]
        if (emotion && emotion[1]) {
          faceUrl = emotion[1]
        }
        if (faceUrl) {
          return "<img class='' src='" + filePath+emotion[1] + "?imageView/2/w/30/h/30'/>";
        } else {
          return data
        }
      },
      //解析图片消息元素
      convertImageMsgToHtml: function convertImageMsgToHtml (content, imageName) {
        var smallImage = content.getImage(webim.webim.IMAGE_TYPE.SMALL) //小图
        var bigImage = content.getImage(webim.webim.IMAGE_TYPE.LARGE) //大图
        var oriImage = content.getImage(webim.webim.IMAGE_TYPE.ORIGIN) //原图
        if (!bigImage) {
          bigImage = smallImage
        }
        if (!oriImage) {
          oriImage = smallImage
        }
        return '<img name=\'' + imageName + '\' src=\'' + smallImage.getUrl() + '#' + bigImage.getUrl() + '#' + oriImage.getUrl() + '\' style=\'CURSOR: hand\' id=\'' + content.getImageId() + '\' bigImgUrl=\'' + bigImage.getUrl() + '\' onclick=\'imageClick(this)\' />'
      },
      //解析语音消息元素
      convertSoundMsgToHtml: function convertSoundMsgToHtml (content) {
        var second = content.getSecond() //获取语音时长
        var downUrl = content.getDownUrl()
        if (webim.webim.BROWSER_INFO.type == 'ie' && parseInt(webim.webim.BROWSER_INFO.ver) <= 8) {
          return '[这是一条语音消息]demo暂不支持ie8(含)以下浏览器播放语音,语音URL:' + downUrl
        }
        return '<audio id="uuid_' + content.uuid + '" src="' + downUrl + '" controls="controls" onplay="onChangePlayAudio(this)" preload="none"></audio>'
      },
      /**
       * @uses amr音频信息转使用amr.js播放
       * @param {Object} content
       */
      convertSoundMsgToAMRPlayer: function convertSoundMsgToAMRPlayer (content) {
        //因为BenzAMRRecorder文件比较大， 加载时间比较长， 这里重试三次。防止失败
        if (typeof BenzAMRRecorder == 'undefined') {
          if (convertSoundMsgToAMRPlayerLoadBenzAMRRecorderRetryCount <= 3) {
            stouTimeout(function () {
              convertSoundMsgToAMRPlayerLoadBenzAMRRecorderRetryCount++
              convertSoundMsgToAMRPlayer(content)
            }, 500)
            return
          }
        }
        var iconStartChar = '&nbsp;&nbsp;&#9658;&nbsp;&nbsp;'
        var btnid = ['amrplay_btn_', content.uuid, '-', Math.round(Math.random() * 1000000)].join('')
        var aElmentString = ['<button id="', btnid, '" style="font-size:1.5em;" data-url="', content.downUrl, '">', iconStartChar, '</button>'].join('')
        setTimeout(function () {
          convertSoundMsgToAMRPlayerLoadBenzAMRRecorderRetryCount = 0
          var btelm = document.getElementById(btnid)
          btelm.onclick = function (event) {
            var amr = new BenzAMRRecorder()
            var seed = null
            amr.onPlay(function () {
              let arr = ['&#9744;', '&#9744;', '&#9744;', '&#9744;', '&#9744;']
              var count = 0
              seed = setInterval(function () {
                arr = ['&#9744;', '&#9744;', '&#9744;', '&#9744;', '&#9744;']
                arr[count % arr.length] = '&#9635;'
                event.target.innerHTML = arr.join('')
                count++
              }, 90)
            })
            amr.onStop(function () {
              clearInterval(seed)
              event.target.innerHTML = iconStartChar
            })
            amr.initWithUrl(content.downUrl).then(function () {
              amr.play()
            })
          }
        }, 0)
        return aElmentString
      },
      //解析文件消息元素
      convertFileMsgToHtml: function convertFileMsgToHtml (content) {
        var fileSize, unitStr
        fileSize = content.getSize()
        unitStr = 'Byte'
        if (fileSize >= 1024) {
          fileSize = Math.round(fileSize / 1024)
          unitStr = 'KB'
        }
        // return '<a href="' + content.getDownUrl() + '" title="点击下载文件" ><i class="glyphicon glyphicon-file">&nbsp;' + content.getName() + '(' + fileSize + unitStr + ')</i></a>';

        // return '<a href="javascript:;" onclick=\'webim.webim.onDownFile("' + content.uuid + '")\' title="点击下载文件" ><i class="glyphicon glyphicon-file">&nbsp;' + content.name + '(' + fileSize + unitStr + ')</i></a>';
        return '<a href="' + content.downUrl + '" target="' + content.name + '" title="点击下载文件" ><i class="glyphicon glyphicon-file">&nbsp;' + content.name + '(' + fileSize + unitStr + ')</i></a>'
      },
      //解析位置消息元素
      convertLocationMsgToHtml: function convertLocationMsgToHtml (content) {
        return '经度=' + content.getLongitude() + ',纬度=' + content.getLatitude() + ',描述=' + content.getDesc()
      },
      //解析自定义消息元素
      convertCustomMsgToHtml: function convertCustomMsgToHtml (content) {
        var data = content.getData() //自定义数据
        var desc = content.getDesc() //描述信息
        var ext = content.getExt() //扩展信息
        return 'data=' + data + ', desc=' + desc + ', ext=' + ext
      },
      //解析群提示消息元素
      convertGroupTipMsgToHtml: function convertGroupTipMsgToHtml (content) {
        var WEB_IM_GROUP_TIP_MAX_USER_COUNT = 10
        var text = ''
        var maxIndex = WEB_IM_GROUP_TIP_MAX_USER_COUNT - 1
        var opType, opUserId, userIdList
        var groupMemberNum
        opType = content.getOpType() //群提示消息类型（操作类型）
        opUserId = content.getOpUserId() //操作人id
        switch (opType) {
          case webim.webim.GROUP_TIP_TYPE.JOIN: //加入群
            userIdList = content.getUserIdList()
            //text += opUserId + "邀请了";
            for (var m in userIdList) {
              text += userIdList[m] + ','
              if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                text += '等' + userIdList.length + '人'
                break
              }
            }
            text = text.substring(0, text.length - 1)
            text += '加入该群，当前群成员数：' + content.getGroupMemberNum()
            break
          case webim.webim.GROUP_TIP_TYPE.QUIT: //退出群
            text += opUserId + '离开该群，当前群成员数：' + content.getGroupMemberNum()
            break
          case webim.webim.GROUP_TIP_TYPE.KICK: //踢出群
            text += opUserId + '将'
            userIdList = content.getUserIdList()
            for (var m in userIdList) {
              text += userIdList[m] + ','
              if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                text += '等' + userIdList.length + '人'
                break
              }
            }
            text += '踢出该群'
            break
          case webim.webim.GROUP_TIP_TYPE.SET_ADMIN: //设置管理员
            text += opUserId + '将'
            userIdList = content.getUserIdList()
            for (var m in userIdList) {
              text += userIdList[m] + ','
              if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                text += '等' + userIdList.length + '人'
                break
              }
            }
            text += '设为管理员'
            break
          case webim.webim.GROUP_TIP_TYPE.CANCEL_ADMIN: //取消管理员
            text += opUserId + '取消'
            userIdList = content.getUserIdList()
            for (var m in userIdList) {
              text += userIdList[m] + ','
              if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                text += '等' + userIdList.length + '人'
                break
              }
            }
            text += '的管理员资格'
            break

          case webim.webim.GROUP_TIP_TYPE.MODIFY_GROUP_INFO: //群资料变更
            text += opUserId + '修改了群资料：'
            var groupInfoList = content.getGroupInfoList()
            var type, value
            for (var m in groupInfoList) {
              type = groupInfoList[m].getType()
              value = groupInfoList[m].getValue()
              switch (type) {
                case webim.webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.FACE_URL:
                  text += '群头像为' + value + '; '
                  break
                case webim.webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.NAME:
                  text += '群名称为' + value + '; '
                  break
                case webim.webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.OWNER:
                  text += '群主为' + value + '; '
                  break
                case webim.webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.NOTIFICATION:
                  text += '群公告为' + value + '; '
                  break
                case webim.webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.INTRODUCTION:
                  text += '群简介为' + value + '; '
                  break
                default:
                  text += '未知信息为:type=' + type + ',value=' + value + '; '
                  break
              }
            }
            break

          case webim.webim.GROUP_TIP_TYPE.MODIFY_MEMBER_INFO: //群成员资料变更(禁言时间)
            text += opUserId + '修改了群成员资料:'
            var memberInfoList = content.getMemberInfoList()
            var userId, shutupTime
            for (var m in memberInfoList) {
              userId = memberInfoList[m].getUserId()
              shutupTime = memberInfoList[m].getShutupTime()
              text += userId + ': '
              if (shutupTime != null && shutupTime !== undefined) {
                if (shutupTime == 0) {
                  text += '取消禁言; '
                } else {
                  text += '禁言' + shutupTime + '秒; '
                }
              } else {
                text += ' shutupTime为空'
              }
              if (memberInfoList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                text += '等' + memberInfoList.length + '人'
                break
              }
            }
            break
          default:
            text += '未知群提示消息类型：type=' + opType
            break
        }
        return text
      },
      //---------------------------------发送消息处理---------------------------------------------------
      handleMsgSend(msgContent){
        var that = this;
        if (!this.selSess) {
          this.selSess = new webim.webim.Session(this.selType, this.selToID, this.selToID, '', Math.round(new Date().getTime() / 1000));
        }
        var isSend = true; //是否为自己发送
        var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
        var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
        var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
        var msg = new webim.webim.Msg(this.selSess, isSend, seq, random, msgTime, loginInfo.identifier, webim.webim.C2C_MSG_SUB_TYPE.COMMON, loginInfo.identifierNick);
        var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
        //解析文本和表情
        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = msgContent.match(expr);
        if (!emotions || emotions.length < 1) {
          that.handleTextImage(msg,msgContent);
        } else {
          for (var i = 0; i < emotions.length; i++) {
            tmsg = msgContent.substring(0, msgContent.indexOf(emotions[i]));
            if (tmsg) {
              that.handleTextImage(msg,tmsg);
            }
            emotionIndex = webim.webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.webim.Emotions[emotionIndex];

            if (emotion) {
              face_obj = new webim.webim.Msg.Elem.Face(emotionIndex, emotions[i]);
              msg.addFace(face_obj);
            } else {
              text_obj = new webim.webim.Msg.Elem.Text(emotions[i]);
              msg.addText(text_obj);
            }
            restMsgIndex = msgContent.indexOf(emotions[i]) + emotions[i].length;
            msgContent = msgContent.substring(restMsgIndex);
          }
          if (msgContent) {
            that.handleTextImage(msg,msgContent);
          }
        }
        msg.PushInfoBoolean = false; //是否开启离线推送push同步
        msg.sending = 1;
        msg.originContent = msgContent;
        this.addMsg(msg);
        this.clearMessageText();
        //关闭表情窗口
        webim.webim.sendMsg(msg, function(resp){
          //发送成功，把sending清理
          that.msgList.forEach(obj=>{
            if(obj.id === msg.random){
              obj.spinner = '';
            }
          })
          webim.webim.Tool.setCookie("tmpmsg_" + that.selToID, '', 0);
        }, function(err) {
          //alert(err.ErrorInfo);
          //提示重发
          that.showReSend(msg);
        });
      },
      getBase64Image:function getBase64Image(img) {
        var canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        var ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, img.width, img.height);
        var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
        var dataURL = canvas.toDataURL("image/"+ext);
        return dataURL;
      },
      base64toFile:function base64toFile(base64, filename) {//将base64转换为文件
        console.log(base64);
        var arr = base64.split(','), mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while(n--){
          u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {type:mime});
      },
      handleTextImage(msg,msgContent) {
        let text_obj = new webim.webim.Msg.Elem.Text(msgContent);
        msg.addText(text_obj);
        /*var that = this;
        var expr = /<img.*?src="(.*?)".*?\/?>/ig;
        var imgList = msgContent.match(expr);
        let img_obj,tmsg,restMsgIndex ;
        for (let key in imgList) {
          let img  = imgList[key];
          if(typeof img == 'string'){
            tmsg = msgContent.substring(0, msgContent.indexOf(imgList[key]));
            if (tmsg) {
              let text_obj = new webim.webim.Msg.Elem.Text(tmsg);
              msg.addText(text_obj);
            }
            let src = $(img).attr("src");
            that.getImagesObjByUrl(src).then(obj=>{
              console.log(obj,"---------------------------------------------------------获取结束")
              img_obj = obj;
              msg.addImage(img_obj);
            })
          }
          restMsgIndex = msgContent.indexOf(imgList[key]) + imgList[key].length;
          msgContent = msgContent.substring(restMsgIndex);
        }
        if (msgContent) {
          let text_obj = new webim.webim.Msg.Elem.Text(msgContent);
          msg.addText(text_obj);
        }*/
      },
      //根据url获取到图片对象
      getImagesObjByUrl(src){
        var that = this;
        var image = new Image();
        image.crossOrigin='';
        image.src = src;
        var images_obj;
        var isRun = true;
        image.onload  = function(){
          console.log("----------------------------加载成功"+image.complete)
          var base64 = that.getBase64Image(image);
          console.log("----------------------------转换base64成功")
          let url = src.split("?")[0];
          var filename = url.substring(url.lastIndexOf('/')+1);
          var file = that.base64toFile(base64,filename);
          var opt = {
            'file': file, //图片对象
            'To_Account': that.selToID, //接收者
            'businessType': webim.webim.UPLOAD_PIC_BUSSINESS_TYPE.C2C_MSG //向好友发图片
          };
          //上传图片
          console.log("----------------------------上传准备",filename,file,opt);
          webim.webim.uploadPic(opt,
            function(images) {
              console.log("----------------------------上传成功",images);
              //上传成功发送图片
              images_obj = new webim.webim.Msg.Elem.Images(images.File_UUID, filename.split(".")[1]);
              for (var i in images.URL_INFO) {
                var img = images.URL_INFO[i];
                var newImg;
                var type;
                switch (img.PIC_TYPE) {
                  case 1: //原图
                    type = 1; //原图
                    break;
                  case 2: //小图（缩略图）
                    type = 3; //小图
                    break;
                  case 4: //大图
                    type = 2; //大图
                    break;
                }
                newImg = new webim.webim.Msg.Elem.Images.Image(type, img.PIC_Size, img.PIC_Width, img.PIC_Height, img.DownUrl);
                images_obj.addImage(newImg);
                isRun = false;
              }
            },
            function(err) {
              alert(err.ErrorInfo);
            }
          )
          return Promise.resolve();
        }
        return Promise.resolve(images_obj);
      },
      showReSend:function showReSend(msg) {
        var that = this;
          var resendBtn = $('<a href="javascript:;">重发</a>');
          //绑定重发事件
          resendBtn.click(function() {
            //发消息处理
            that.handleMsgSend(msg.originContent);
          });
        that.msgList.forEach(obj=> {
          if (obj.id === msg.random) {
            obj.spinner =resendBtn;
          }
        });
      },
      //------------------------------------历史消息-----------------------------------------------------
      getHistoryMsgCallback:function getHistoryMsgCallback(msgList, prepage) {
        var that = this
        var msg
        prepage = prepage || false
        //如果是加载前几页的消息，消息体需要prepend，所以先倒排一下
        if (prepage) {
          msgList.reverse()
        }
        for (var j in msgList) { //遍历新消息
          msg = msgList[j];
          if(typeof msg=='object'){
            let selSess = msg.getSession()
            if (selSess.id() == that.selToID) { //为当前聊天对象的消息
              that.selSess = selSess;
              //在聊天窗体中新增一条消息
              that.addMsg(msg, prepage)
            }
          }
        }
        if(msgList&&msgList.length>0){
          setTimeout(function() {
            $('.msgBox').scrollTop(78*msgList.length);
          }, 300);
        }

        //消息已读上报，并将当前会话的消息设置成自动已读
        webim.webim.setAutoRead(selSess, true, true)
        },
      //获取最新的c2c历史消息,用于切换好友聊天，重新拉取好友的聊天消息
      getLastC2CHistoryMsgs:function(cbOk, cbError) {
        var that = this;
        if (this.selType == webim.webim.SESSION_TYPE.GROUP) {
          alert('当前的聊天类型为群聊天，不能进行拉取好友历史消息操作');
          return;
        }
        if (!this.selToID ||this.selToID == '@TIM#SYSTEM') {
          alert('当前的聊天id非法，selToID=' + this.selToID);
          return;
        }
        var lastMsgTime = 0; //第一次拉取好友历史消息时，必须传0
        var msgKey = '';
        var options = {
          'Peer_Account': this.selToID, //好友帐号
          'MaxCnt': reqMsgCount, //拉取消息条数
          'LastMsgTime': lastMsgTime, //最近的消息时间，即从这个时间点向前拉取历史消息
          'MsgKey': msgKey
        };
        selSess = null;
        webim.webim.MsgStore.delSessByTypeId(this.selType, this.selToID);
        webim.webim.getC2CHistoryMsgs(
          options,
          function(resp) {
            if (resp.MsgList.length == 0) { //是否还有历史消息可以拉取，1-表示没有，0-表示有
              that.bindScrollHistoryEventReset();
              return;
            }
            getPrePageC2CHistroyMsgInfoMap[that.selToID] = { //保留服务器返回的最近消息时间和消息Key,用于下次向前拉取历史消息
              'LastMsgTime': resp.LastMsgTime,
              'MsgKey': resp.MsgKey
            };
            //清空聊天界面
            that.msgList = [];
            if (cbOk)
              cbOk(resp.MsgList);
          },
          cbError
        );
      },
      getPrePageC2CHistoryMsgs: function(cbOk, cbError) {
        var that = this;
        if (this.selType == webim.webim.SESSION_TYPE.GROUP) {
          alert('当前的聊天类型为群聊天，不能进行拉取好友历史消息操作');
          return;
        }
        var tempInfo = getPrePageC2CHistroyMsgInfoMap[that.selToID]; //获取下一次拉取的c2c消息时间和消息Key
        var lastMsgTime;
        var msgKey;
        if (tempInfo) {
          lastMsgTime = tempInfo.LastMsgTime;
          msgKey = tempInfo.MsgKey;
        } else {
          return;
        }
        var options = {
          'Peer_Account': that.selToID, //好友帐号
          'MaxCnt': reqMsgCount, //拉取消息条数
          'LastMsgTime': lastMsgTime, //最近的消息时间，即从这个时间点向前拉取历史消息
          'MsgKey': msgKey
        };
        webim.webim.getC2CHistoryMsgs(
          options,
          function(resp) {
            if (resp.MsgList.length == 0) {
              that.bindScrollHistoryEventReset();
              that.message("已没有历史消息");
              webim.webim.Log.warn("没有历史消息了:data=" + JSON.stringify(options));
              return;
            }
            getPrePageC2CHistroyMsgInfoMap[that.selToID] = { //保留服务器返回的最近消息时间和消息Key,用于下次向前拉取历史消息
              'LastMsgTime': resp.LastMsgTime,
              'MsgKey': resp.MsgKey
            };
            if (cbOk) {
              cbOk(resp.MsgList);
            } else {
              that.getHistoryMsgCallback(resp.MsgList, true);
            }
          },
          cbError
        );
      },
      //------------------------------------图片上传-------------------------------------
      fileOnChange(e){
        let uploadFile = e.target;
        var that = this;
        var file = uploadFile.files[0];
        var fileSize = file.size;
        //先检查图片类型和大小
        if (!$.checkPic(uploadFile, fileSize)) {
          return;
        }
        //预览图片
        var reader = new FileReader();
        reader.onload = (function(file) {
          return function(e) {
            that.sendPicVO.previewUrl = this.result;
            that.sendPicVO.previewAlt=  file.name;
          };
        })(file);
        reader.readAsDataURL(file);
      },
      //进度条回调
      onProgressCallBack(loadedSize, totalSize){
        this.sendPicVO.percentage = (loadedSize / totalSize) * 100;
      },
      //上传图片
      uploadPic(){
        if(!this.sendPicVO.previewUrl){
          this.messageError("请先选择图片");
          return;
        }
        var that = this;
        var uploadFiles = document.getElementById('upd_pic');
        var file = uploadFiles.files[0];

        //封装上传图片请求
        var opt = {
          'file': file, //图片对象
          'onProgressCallBack': this.onProgressCallBack, //上传图片进度条回调函数
          //'abortButton': document.getElementById('upd_abort'), //停止上传图片按钮
          'To_Account': this.selToID, //接收者
          'businessType': webim.webim.UPLOAD_PIC_BUSSINESS_TYPE.C2C_MSG //向好友发图片
        };
        //上传图片
        webim.webim.uploadPic(opt,
          function(resp) {
            //上传成功发送图片
            that.sendPic(resp, file.name);
            that.pictureVisit = false;
          },
          function(err) {
            alert(err.ErrorInfo);
          }
        );
      },
      //发送图片
      sendPic(images, imgName){
        var that =this;
        if (!this.selToID) {
          this.messageError("您还没有选择好友，暂不能聊天");
          return;
        }
        if (!this.selSess) {
          this.selSess = new webim.webim.Session(this.selType, this.selToID,this.selToID,'', Math.round(new Date().getTime() / 1000));
        }
        var msg = new webim.webim.Msg(this.selSess, true, -1, -1, -1, loginInfo.identifier, 0, loginInfo.identifierNick);
        var images_obj = new webim.webim.Msg.Elem.Images(images.File_UUID, imgName.split(".")[1]);
        for (var i in images.URL_INFO) {
          var img = images.URL_INFO[i];
          var newImg;
          var type;
          switch (img.PIC_TYPE) {
            case 1: //原图
              type = 1; //原图
              break;
            case 2: //小图（缩略图）
              type = 3; //小图
              break;
            case 4: //大图
              type = 2; //大图
              break;
          }
          newImg = new webim.webim.Msg.Elem.Images.Image(type, img.PIC_Size, img.PIC_Width, img.PIC_Height, img.DownUrl);
          images_obj.addImage(newImg);
        }
        msg.addImage(images_obj);
        //调用发送图片消息接口
        webim.webim.sendMsg(msg, function(resp) {
          if (that.selType == webim.webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            that.addMsg(msg);
          }
        }, function(err) {
          alert(err.ErrorInfo);
        });
      },
      //------------------------------------文件上传-------------------------------------
      //发送文件
      uploadFile(){
        var that =this;
        var uploadFiles = document.getElementById('upd_file');
        var file = uploadFiles.files[0];
        //先检查图片类型和大小
        if (!$.checkFile(file)) {
          return;
        }

        //封装上传文件请求
        var opt = {
          'file': file, //文件对象
          'onProgressCallBack': that.onFileProgressCallBack, //上传文件进度条回调函数
          //'abortButton': document.getElementById('upd_abort'), //停止上传文件按钮
          'To_Account': that.selToID, //接收者
          'businessType': webim.webim.UPLOAD_PIC_BUSSINESS_TYPE.C2C_MSG,//业务类型
          'fileType': webim.webim.UPLOAD_RES_TYPE.FILE//表示上传文件
        };
        //上传文件
        webim.webim.uploadFile(opt,
          function (resp) {
            //上传成功发送文件
            that.sendFile(resp,file.name);
            that.fileVisit = false;
            $("#upd_file").val('');
            that.sendFileVO.percentage=0;
          },
          function (err) {
            alert(err.ErrorInfo);
          }
        );
      },
      //进度条回调
      onFileProgressCallBack(loadedSize, totalSize) {
        this.sendFileVO.percentage = (loadedSize / totalSize) * 100;
      },
      //上传发送
      sendFile(file,fileName){
        var that =this;
        if (!this.selToID) {
          this.messageError("您还没有选择好友，暂不能聊天");
          return;
        }
        if (!this.selSess) {
          this.selSess = new webim.webim.Session(this.selType, this.selToID,this.selToID,'', Math.round(new Date().getTime() / 1000));
        }
        var msg = new webim.webim.Msg(this.selSess, true, -1, -1, -1, loginInfo.identifier, 0, loginInfo.identifierNick);

        var uuid=file.File_UUID;//文件UUID
        var fileSize=file.File_Size;//文件大小
        var senderId=loginInfo.identifier;
        var downloadFlag=file.Download_Flag;
        if(!fileName){
          var random=Math.round(Math.random() * 4294967296);
          fileName=random.toString();
        }
        var fileObj=new webim.webim.Msg.Elem.File(uuid,fileName, fileSize, senderId, this.selToID, downloadFlag, this.selType);
        msg.addFile(fileObj);
        //调用发送文件消息接口
        webim.webim.sendMsg(msg, function (resp) {
          if (that.selType == webim.webim.SESSION_TYPE.C2C) {//私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            that.addMsg(msg);
          }
        }, function (err) {
          alert(err.ErrorInfo);
        });
      },
      //-----------------------------------登陆----------------------------------
      login(){
        this.getIMInfo()
        this.initIM();
        this.isLogin = true;
      },
      getIMInfo () {
        this.simpleGet("/api/mgr/yb/im/getIMUserSig",null,result=>{
          loginInfo.identifier = result.data.userSig.identity;
          loginInfo.userSig = result.data.userSig.userSign;
          loginInfo.identifierNick = result.data.user.nickName;
          loginInfo.headurl = filePath + result.data.user.headPic;
        },false);
      },
      initIM () {
        let that = this
        // 监听连接状态回调变化事件
        var onConnNotify = function (resp) {
          var info
          switch (resp.ErrorCode) {
            case webim.webim.CONNECTION_STATUS.ON:
              webim.webim.Log.warn('建立连接成功: ' + resp.ErrorInfo)
              break
            case webim.webim.CONNECTION_STATUS.OFF:
              info = '连接已断开，无法收到新消息，请检查下您的网络是否正常: ' + resp.ErrorInfo
              alert(info)
              webim.webim.Log.warn(info)
              break
            case webim.webim.CONNECTION_STATUS.RECONNECT:
              info = '连接状态恢复正常: ' + resp.ErrorInfo
              alert(info)
              webim.webim.Log.warn(info)
              break
            default:
              webim.webim.Log.error('未知连接状态: =' + resp.ErrorInfo)
              break
          }
        }
        //监听 C2C 消息通道的处理，方法在 receive_new_msg.js 文件中
        var onC2cEventNotifys = {
          '92': this.onMsgReadedNotify,//消息已读通知
          "96": this.onMultipleDeviceKickedOut
        }
        //监听事件
        var listeners = {
          'onConnNotify': onConnNotify//监听连接状态回调变化事件,必填
          , 'jsonpCallback': this.jsonpCallback//IE9(含)以下浏览器用到的 jsonp 回调函数，
          , 'onMsgNotify': this.onMsgNotify//监听新消息(私聊，普通群(非直播聊天室)消息，全员推送消息)事件，必填
          , 'onC2cEventNotifys': onC2cEventNotifys,//监听 C2C 系统消息通道
          "onKickedEventCall": this.onKickedEventCall //被其他登录实例踢下线
        }
        //初始化时，其他对象，选填
        var options = {
          'isAccessFormalEnv': true, 	// 是否访问正式环境，默认访问正式，选填
          'isLogOn': true,  	// 是否开启控制台打印日志,默认开启，选填
          'emotions':emoji.emotions,
          'emotionDataIndexs':emoji.emotionDataIndexs
        }
        if (webim.webim.checkLogin()) {	// 检查是否登录返回true和false,不登录则重新登录
          that.initRecentContactList()  // 获取信息列表
        } else {
          webim.webim.login(
            loginInfo, listeners, options,
            function (resp) {
              if(resp.identifierNick!=loginInfo.identifierNick){
                loginInfo.identifierNick = resp.identifierNick//设置当前用户昵称
                loginInfo.headurl = resp.headurl
              }

              that.initRecentContactList()
            },
            function (err) {
              alert(err.ErrorInfo)
            }
          )
        }
      },
      //-----------------------------------登出-----------------------------------
      loginOut(){
        var that = this;
        if (loginInfo.identifier) {
          //sdk登出
          webim.logout(
            function (resp) {
              loginInfo.identifier = null;
              loginInfo.userSig = null;
              this.message("退出成功");
              that.isLogin = false;
            }
          );
        } else {
          this.messageError('未登录');
        }
      },
      //被新实例踢下线的回调处理
      onKickedEventCall:function onKickedEventCall(){
        this.msgAlert("其他地方登录了客服,你已被踢下线",()=>{
          this.isLogin = false;
        })
      },
       //多终端登录被T
      onMultipleDeviceKickedOut:function onMultipleDeviceKickedOut() {
        this.msgAlert("其他地方登录了客服,你已被踢下线",()=>{
          this.isLogin = false;
        })
      },
      //消息已读通知
      onMsgReadedNotify(notify) {
        var sess = webim.webim.MsgStore.sessMap()[webim.webim.SESSION_TYPE.C2C + notify.From_Account];
        if (sess) {
          this.userList[this.userMap[sess.id()]].UnreadMsgCount = 0;
        }
      },
      uploadInfo(){
        var that = this;
        if(!that.updateVO.nickName){
          this.messageError("请输入昵称");
          return ;
        }
        var profile_item = [{
          "Tag": "Tag_Profile_IM_Nick",
          "Value": that.updateVO.nickName
        }, ];
        if (this.updateVO.headPic) { //如果设置了头像URL
          profile_item.push({
            "Tag": "Tag_Profile_IM_Image",
            "Value": filePath +that.updateVO.headPic
          })
        }
        var options = {
          'ProfileItem': profile_item
        };

        webim.webim.setProfilePortrait(
          options,
          function(resp) {
            that.updateVisit = false;
            loginInfo.identifierNick = that.updateVO.nickName; //更新昵称
            if (this.updateVO.headPic) {
              loginInfo.headurl = that.updateVO.headPic
            }
            that.message('设置个人资料成功');
          },
          function(err) {
            alert(err.ErrorInfo);
          }
        );
      },
      appendMsg(code,emoji){
        this.messageText += code;
        $("#messageText").append("<img src='"+emoji+"?imageView/2/w/30/h/30' />");
      }
    },
    mounted () {
      /*
            var text = "<img src=\"http://p.qpic.cn/opensdk_im/0/6748592331764440807_1_156F7AAC7249F9BDE5F44A66F22AE728/198#http://p.qpic.cn/opensdk_im/0/6748592331764440807_1_156F7AAC7249F9BDE5F44A66F22AE728/720#http://p.qpic.cn/opensdk_im/0/6748592331764440807_1_156F7AAC7249F9BDE5F44A66F22AE728/0\">小鬼";
            var expr = /<img.*?src="(.*?)".*?\/?>/ig;
            var imgList = text.match(expr);
            for (let key in imgList) {
              let img  = imgList[key];
              if(typeof img == 'string'){
                let src = $(img).attr("src");
                var image = new Image();
                image.src = src;
                image.onload = function(){
                  console.log(image.sizes,"-----------------------------------------------")
                };
              }
            }*/
      /*this.$nextTick(()=>{
        let options =  {
          "To_Account":["3"],
          "TagList":
            [
              "Tag_Profile_IM_Nick",
              "Tag_Profile_IM_Image"
            ]
        }
        webim.webim.getProfilePortrait(options,function(resp){
          console.log("------------------------------------------------------------",resp)
        }, function(err) {
          alert(err.ErrorInfo);
        });
      })*/

    }
  }
</script>


<style lang="less" scoped>
  .im-box{
    margin-bottom: 30px;
    padding-left: 60px;
    padding-top: 50px;
    min-height: 650px;
    width: 1200px;
  .im-user-box{
    border: 1px solid #d0d0d0;
    border-top: none;
    .sessionItem{
      text-align: center;
      background-color: #0F6DB5;
      color: #fff;
      font-size: 14px;
      height: 30px;
      padding-top: 10px;
    }
    .user-box{
      background-color: rgba(0, 0, 0, 0.025);
      cursor: pointer;
      border-bottom: 1px solid #e9e9eb;
      &.action{
        background-color: #d0d0d0;
      }
      .header_pic{
        width: 60px;
        float: left;
        img{
          margin: 5px;
          width: 46px;
          height: 46px;
          //border-radius:50%;
        }
      }
      .message-box{
        font-size: 14px;
        float: right;
        width: 190px;
        .text{
          padding-top: 10px;
          display: inline-block;
          width: 150px;
          .nickName{
            font-weight: bolder;
          }
          .lastMsg{
            padding-top: 5px;
            color: #8c939d;
          }
        }
        .tools{
          padding: 0;
          display: inline-block;
          font-size: 10px;
          width: 20px;
          .time{
            margin-bottom: 10px;
          }
          .num{
            text-align: center;
            background-color: red;
            width: 16px;
            border-radius:50%;
            color: #fff;
          }
        }
      }

    }
  }
}
  .right-header{
    padding: 0;
    .message-title{
      background-color: #0F6DB5;
      color: #fff;
      font-size: 14px;
      height: 30px;
      padding-top: 10px;
      padding-left: 10px;
    }
  }

  .main-box{
    padding: 0;
    .msgBox{
      height: 420px;
      padding: 20px 6px 20px 6px;
      background: #f5f5f5;
      overflow: auto;
       .onemsg{
         position:relative;
         .msghead {
           margin: 0 0 10px;
           line-height: 12px;
           font-size: 12px;
           .headurlClass{
             height: 30px;
             width: 30px;
             border-radius: 50%;
           }
           .blue{
             color: blue;
           }
           .green{
             color: green;
           }
         }
         .msgbody {
           margin: 0 0 0 18px;
           line-height: 13px;
           font-size: 14px;
           display: block;
           padding: 9.5px;
           color: #333333;
           word-break: break-all;
           word-wrap: break-word;
           background-color: #f5f5f5;
           border: 1px solid #cccccc;
           border-radius: 4px;
         }
         img {
           max-width: 500px;
         }
       }
    }
  }
  .footer{
    padding: 0;
    .f-tools{
      border: 1px solid #d0d0d0;
      background-color: rgba(0, 0, 0, 0.025);
      height: 30px;
      i{
        padding: 6px;
      }
    }
    .f-info{
      height: 100px;
      border: 1px solid #e9e9eb;
    }
  }
  .msgedit {
    overflow:auto;
    font-size: 12px;
    width: 99%;
    height: 90px;
    border: 1px rgb(181, 178, 178) solid;
    background: #f5f5f5;
    padding: 6px 6px;
    line-height: 1.5;
  }
  .sendbar {
    width: 526px;
    height: 30px;
  }
  .sendbtn {
    float: right;
    width: 80px;
    margin-right: 10px;
    font: 10px/1.5 "微软雅黑";
  }
  .closebtn {
    float: right;
    width: 80px;
    margin-right: 10px;
    font: 10px/1.5 "微软雅黑";
  }
  .spinner {
    width: 60px;
    text-align: center;
    position: absolute;
    right: 10px;
    top: 32px;
    text-align: right;
  }
  .spinner > div {
    width: 6px;
    height: 6px;
    background-color: #333;

    border-radius: 100%;
    display: inline-block;
    -webkit-animation: sk-bouncedelay 1.4s infinite ease-in-out both;
    animation: sk-bouncedelay 1.4s infinite ease-in-out both;
  }
  .spinner .bounce1 {
    -webkit-animation-delay: -0.32s;
    animation-delay: -0.32s;
  }
  .spinner .bounce2 {
    -webkit-animation-delay: -0.16s;
    animation-delay: -0.16s;
  }
  .previewImage{
    display: block;
    width: 400px;
    height: auto;
  }
  .wl_faces_main {
    ul {
      margin: 12px 12px;
      padding: 0px;
      overflow: hidden;
      border-top: 1px #CCC solid;
      border-left: 1px #CCC solid;
      list-style: none;
      width: 393px;
      li {
        float: left;
        border-right: 1px #CCC solid;
        border-bottom: 1px #CCC solid;
        height: 28px;
        width: 28px;
        margin: 0px 0px 0px 0px;
        padding: 4px 2px;
        text-align: center;
        img {
          width: 24px;
          height: 24px;
        }
      }
    }
  }
  .p7{
    padding: 7px;
  }
  .btn-box{
    text-align: center;
    .btn{
      margin-top: 260px;
      margin-bottom: 130px;
      width: 200px;
      color: #fff;
      background-color: #409eff;
      border-color: #409eff
    }
    .btnOut{
      margin-top: 30px;
      margin-bottom: 30px;
      color: #fff;
      background-color: #409eff;
      border-color: #409eff
    }
  }
  .picker-wrapper{
    position: 198px;
    width: 100%;
    height: 100%;
    transform: translate(0%, 0px) translateZ(0px);
  }
  .emoji-wrapper{
    display:flex;
    flex-direction:row;
    flex-wrap:wrap;
    justify-content:flex-start;
    background:rgb(245, 245, 245);
    border-top:1px solid rgb(216, 216, 216);
  }
  .emoji-item {
    width:16.66%;
    padding:5px 0;
    text-align:center;
    font-size:40px;
  }
  .emoji-gif{
    height:30px;
    width:30px;
    display:inline-block;
    background-size: 100% 100%;
    vertical-align:middle;
  }
</style>
