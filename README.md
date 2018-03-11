# FOFI 客户端源码



![](logo2.png)
## FOFI v1.0.1


FOFI是一款24小时话题 + 匿名聊天评论的社交类APP。利用此应用程序，用户可以发布、阅读他人发出的话题室，并参与其中，同时也可以通过各大社交平台转发自己发布或正在阅读的话题，邀请其他人参与话题讨论。在这里，话题的生命周期只有24小时，用户更可以畅所欲言，尽情享受这不同话题、不同参与者所带来的独特氛围的24小时美好时光。


## 历史分支

| 序号 | 版本号 | 说明 |
|---|--------|-----------|
| 1 | v1.0.1 | 接入MobIM |
| 2 | v1.0.0 | 登录注册页面 |



## 最新版开发环境

1. Android Studio >= 3.0.1
2. Gradel Version: 4.1
3. Gradle Plugin Version: 3.0.1
4. SDK Tool >= 27.0.3

## 相关依赖

- **com.android.support**：Google官方适配包，用于提供卡片、列表、主题等基础模块
- **okhttp**：网络框架
- **retrofit**：网络框架
- **butterknife**：注解库，用于简化findView和onClick操作
- **com.github.bumptech.glide**：所有的图片请求管理库
- **com.google.code.gson**：Json-Model解析库

## 系统设计

- App与App后台通信模式：HTTP/HTTPS
- Android注解框架：ButterKnife
- HTTP网络框架：okhttp、retrofit
- XMPP服务：mobIM、openfire
- SMS短信验证服务：mob SMSSDK
- 服务器：云服务器（阿里云CES）
- 服务器端：Spring Boot
- 数据库：MySQL5.7
- JDK版本：1.8



