server 使用 TrueLicense 生成License
client 验证 License

TrueLicense的LicenseManager类自带的verify方法只校验了我们颁发的许可文件的生效和过期时间
我们还需要校验应用部署的服务器的硬件信息(Mac地址、CPU序列号、主板序列号等)

license授权机制原理:
1.授权者保留私钥，使用私钥对包含授权信息（如使用截止日期，MAC地址等）的license进行数字签名
2.公钥给使用者（放在验证的代码中使用），用于验证license是否符合使用条件


RDD 时序数据库

环形、大小固定、无需运维、绘图

