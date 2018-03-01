# FOFI
FOFI login v1.0.0

by 张逸扬 2018年2月4日00:23:12

##相关术语

**XMPP：** 可扩展通讯和表示协议 (XMPP) 可用于服务类实时通讯、表示和需求响应服务中的XML数据元流式传输。XMPP以Jabber协议为基础，而Jabber是即时通讯中常用的开放式协议。

**openfire：** 是开源的、基于可拓展通讯和表示协议(XMPP)、采用Java编程语言开发的实时协作服务器。 Openfire安装和使用都非常简单，并利用Web进行管理。单台服务器可支持上万并发用户。

**Smack：** 是一个开源，易于使用的XMPP（jabber）客户端类库。

**Redis：** 是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。

**MongoDB：** 是一个基于分布式文件存储的数据库。由C++语言编写。旨在为WEB应用提供可扩展的高性能数据存储解决方案。

**RESTful：** 一种软件架构风格、设计风格，而不是标准，只是提供了一组设计原则和约束条件。它主要用于客户端和服务器交互类的软件。REST（英文：Representational State Transfer，简称REST）描述了一个架构样式的网络系统，比如 web 应用程序。

**JSON：** JSON(JavaScript Object Notation, JS 对象标记) 是一种轻量级的数据交换格式。它基于 ECMAScript (w3c制定的js规范)的一个子集，采用完全独立于编程语言的文本格式来存储和表示数据。

**install4j：** install4j是一个功能强大的，多平台Java安装文件生成工具，用于生成Java应用程序本地化的安装及应用程序发布。

**Telnet：** Telnet协议是TCP/IP协议族中的一员，是Internet远程登陆服务的标准协议和主要方式。它为用户提供了在本地计算机上完成远程主机工作的能力。

**//Xshell：** 是一个强大的安全终端模拟软件，它支持SSH1, SSH2, 以及Microsoft Windows 平台的TELNET 协议。

**//Xftp：** 是一个基于 MS windows 平台的功能强大的SFTP、FTP 文件传输软件。使用了 Xftp 以后，MS windows 用户能安全地在 UNIX/Linux 和 Windows PC 之间传输文件

**Putty：** 是一个免费的、Windows x86平台下的Telnet、SSH和rlogin客户端，但是功能丝毫不逊色于商业的Telnet类工具。

**Retrofit：** 是当下最热的一个网络请求库。

	compile 'com.squareup.retrofit2:retrofit:2.3.0'

**okhttp：** 一个处理网络请求的开源项目,是安卓端最火热的轻量级框架,由移动支付Square公司贡献。

	compile 'com.squareup.okhttp3:okhttp:3.9.1'

**Node.js：** Node.js是一个Javascript运行环境(runtime)，发布于2009年5月，由Ryan Dahl开发，实质是对Chrome V8引擎进行了封装。Node.js对一些特殊用例进行优化，提供替代的API，使得V8在非浏览器环境下运行得更好。

**Glide：** 是一个快速高效的Android图片加载库，注重于平滑的滚动。Glide提供了易用的API，高性能、可扩展的图片解码管道（decode pipeline），以及自动的资源池技术。

	implementation 'com.github.bumptech.glide:glide:4.6.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.6.1'

**Gson:** Gson 是google解析Json的一个开源框架,同类的框架fastJson,JackJson等等.

	compile 'com.google.code.gson:gson:2.8.2'

**Rxjava:** JVM响应式扩展Reactive Extensions 用于使用Java VM的可观察序列编写异步和基于事件的程序的库。

##系统设计

App与App后台通信模式：HTTP（socket与websocket是长连接通信）

HTTP框架：okhttp

XMPP服务：openfire

SMS服务：mob SMSMSDK

服务器：云服务器（阿里云CES） 

服务器端：Spring Boot

数据库：MySQL5.7

JDK版本：1.8

##app结构

**登录页面:** LoginActivity

**注册页面one:** RegisterStepOneActivity

**注册页面two:** RegisterStepTwoActivity

**忘记密码页面one:** RetrieveActivity

**忘记密码页面two:** ResetPwdActivity
