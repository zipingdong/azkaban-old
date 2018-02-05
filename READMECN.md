## 基于 3.40.0-1bb277d
## 用户管理
用户信息使用数据库存储，不再使用 azkaban-users.xml

用户登录信息同上

## 项目
隐藏项目页面中的“Upload”、“Download”按钮

项目页面中添加“Create Job”、“Refresh Project”按钮

项目中上传作业默认在线填写，并存储到数据库中

项目中只允许一个工作流；工作流名字叫 leaf_job

项目中刷新项目不再使用上传压缩包的形式，改为从数据库读取作业信息，然后生成工作流

## 作业
添加短信报警功能，并自动读取作业创建者的手机号，发送短信

工作流作业展开列表中添加在线 “Edit Job”、“Delete Job”功能

需要在系统环境变量添加下面的变量:
```
export AZKABAN_HOME=你的web、exec的根目录
export AZKABAN_WEB=$AZKABAN_HOME/azkaban-web-server
export AZKABAN_EXEC=$AZKABAN_HOME/azkaban-exec-server
export SMS_API_DOMAIN="短信接口的域名，如 www.xxx.com"
export SMS_API_PORT="短信接口的端口，如 80"
export SMS_API_PATH="短信接口的路径，如 /api/sms"
export SMS_API_QUERY="短信接口的key等额外信息，如 AppID=123&AppSecret=456&"
```
