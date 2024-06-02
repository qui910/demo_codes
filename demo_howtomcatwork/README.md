[toc]
>《How Tomcat Works 中文版》阅读笔记

针对原书代码过旧的问题，暂时将原书代码替换为最新的代码。

# 1 一个简单的Web服务器
实现一个简单的Http服务器，支持从浏览器请求html页面，并支持gif、icon图像等静态资源。
目录：`org.example.ex01`

浏览器测试：http://localhost:8080/index.html

# 2 一个简单的Servlet容器
实现一个简单的Servlet容器，支持从浏览器请求html页面，并支持gif、icon图像等静态资源。
同时也支持Servlet的动态加载。

目录：`org.example.ex02`

浏览器静态资源测试：http://localhost:8080/index.html
浏览器Servlet资源测试：http://localhost:8080/servlet/PrimitiveServlet
