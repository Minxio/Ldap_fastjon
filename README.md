# JNDIExploit-Ldap-Fastjson

 `JNDI注入` 利用的工具，fastjson全版本在原生反序列化利用链。

把 [y4tacker](https://github.com/y4tacker) 大佬的项目[FastJson与原生反序列化-二](https://y4tacker.github.io/2023/04/26/year/2023/4/FastJson%E4%B8%8E%E5%8E%9F%E7%94%9F%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96-%E4%BA%8C/ ) 编译成了工具。

------

## 免责声明

该工具仅用于安全自查检测

由于传播、利用此工具所提供的信息而造成的任何直接或者间接的后果及损失，均由使用者本人负责，作者不为此承担任何责任。

本人拥有对此工具的修改和解释权。未经网络安全部门及相关部门允许，不得善自使用本工具进行任何攻击活动，不得以任何方式将其用于商业目的。



## 使用说明

使用 `java -jar Ldap_Fastjson_Gadget-1.0.0.jar -h` 查看参数说明



使用 `java -jar Ldap_Fastjson_Gadget-1.0.0.jar -C "calc" -A "0.0.0.0"` 开启 LDAP 服务，其中 `-A` 为可选参数
