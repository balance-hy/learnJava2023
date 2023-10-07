## ==和equals对比
==:
1. 既可以判断基本类型，又可以判断引用类型
2. 如果判断基本类型，判断其值是否相等
3. 如果判断引用类型，判断的是地址是否相等，即判定是否为同一对象  

只要有基本数据类型，判断的便是值是否相等

equals:
1. 是Object类的方法,只能判断引用类型
2. 默认判断的是地址是否相等，子类往往重写该方法，用于判断内容是否相等，比如Integer，String

## comparator和comparable
> https://cloud.tencent.com/developer/article/1918856