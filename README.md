# QQ_Luo
这是一个用java编写的模拟QQ的project



实现的功能有：

1. 注册（输入用户名和密码，ID由系统分配）
   1. 验证两次密码输入一致
   2. 通过访问数据库主动为用户分配ID
2. 登录（输入ID和密码）
   1. 记住密码
   2. 可明文显示密码
   3. 登录时错误提示：服务器未打开、密码不正确、ID不存在、重复登录
3. 显示好友列表（动态更新好友状态）
4. 同一聊天窗口只能打开一个
5. 公聊
6. 私聊
7. 有人发起聊天而本账号的相应对话框未打开时，可自动弹出对话框并显示消息。
8. P2P发送接收文件



仍需要继续改进的地方：

1. 有的用户功能实现是直接调取了数据库，非常不合理，应改为向服务器发消息获得相应，所有数据库操作都应由服务器完成。
2. 忘记密码（需设置密保问题）
3. 自动登录（大概是sleep一段时间后自动执行登录）
4. 更改头像
5. 更改用户名
6. server213异常离线（断开连接）：如果有别的消息阻塞，则离线消息无法收到
7. 密码和信息传输加密
8. 文件传输（私发，群发）
9. 可以向离线用户传送消息，服务器代为存储
10. 把聊天消息改为UDP
11. 有新消息时可以选择是使好友列表变色或自动弹出窗口
12. 各种异常处理
13. 可以查找、添加、删除好友（将所有好友信息存在数据库中，较小的ID放在第一列）
14. 可以组建、解散群聊（区分公聊（广播）和群聊（指定的一批用户））
15. 用户可以跨互联网也可以本机开启多个（IP和Port同时区分，要获取本机Ip）

