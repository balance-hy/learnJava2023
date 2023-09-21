## 泛型 ~广泛引用类型
**传统方法有哪些问题**：  
1. 不能对加入到集合(例如ArrayList)中的数据类型进行约束(不安全),即不能规定加入的类型  
2. 遍历的时候，需要进行类型转换，如果集合中的数据量较大，对效率有影响，默认是Object类型  

**使用泛型的好处**：
1. 编译时，检查添加元素的类型，提高了安全性
2. 减少了类型转换的次数，提高了效率
```java
//规定为List中只能为Dog类型,或者为Dog子类
ArrayList<Dog> dogs = new ArrayList<Dog>();
```
### 泛型作用：
可以在类声明时通过一个标识表示类中某个属性的类型，或者是某个方法的返回值的类型，或者是参数类型  
```java
class Person<E>{
    E s;//用E来代替类型，该数据类型在实例化Person对象时候指定，即编译时
    public Person(E s) {//可以是参数类型
        this.s = s;
    }
    public E f(){//可以是方法的返回值的类型
        return s;
    }
}
//具体使用
Person<String> stringPerson = new Person<String>("name");
```
### 泛型声明：  
```java
interface 接口<T>{} 和 class 类<K,V>{}
```
**其中，T,K,V不代表值，而是表示类型，任意字母其实都是可以的，只不过常用这些,给泛型指定类型时只能是引用类型不能是基本数据类型**  
泛型使用：  
```java
ArrayList<String> strings = new ArrayList<>();//一般简写，即只写左边

//泛型默认是Object 下面等价于ArrayList<Object> objects = new ArrayList<>();
ArrayList arrayList = new ArrayList(); 
```
### 自定义泛型
#### 自定义泛型类
```java
class 类名<T,R...>{//...表示可以有多个泛型
    
}
```
注意事项:  
1. 普通成员可以使用泛型（属性、方法）
2. 类中使用泛型的数组，不能初始化 **因为数组在new时，不能确定类型，不知道开多大的空间适合**
3. 静态属性、方法中不能使用类的泛型 **静态和类相关，在类加载时，对象还没有创建**
4. 泛型类的类型，是在创建对象时确定的
5. 如果在创建对象未指定类型，默认为Object  

#### 自定义泛型接口
```java
class 类名<T,R...>{
    
}
```
注意事项:  
1. 接口中，静态成员也不能使用泛型，原因和类一样
2. 泛型接口的类型，在继承接口或者实现接口时确定
3. 没有指定类型，默认为Object 

#### 自定义泛型方法
```java
修饰符 <T,R...> 返回类型 方法名(参数){
    
}
//泛型标识符可以直接提供给方法名中参数使用
public <T,R> void fly(T t,R r){
    
}
//编译器自动确认类型
A a = new A();
a.<Dog,Integer>fly(new Dog(),1);//无需写<Dog,Integer>
class A{
    public <T,R> void fly(T t,R r){
        System.out.println(t);
        System.out.println(r);
    }
}
class Dog{

}
```
注意事项：  
1. 泛型方法可以定义在普通类中，也可以定义在泛型类中  
2. 当泛型方法被调用时，类型会确定  
```java
public void eat<E,e>{
    
}
```
**上面代码中修饰符后没有<T,R...>,所以eat方法不是泛型方法，而是使用了泛型**  
### 泛型继承和通配符
泛型不具备继承性
```java
List<Object> list=new ArrayList<String>();//String是Object子类,但会报错
```
1. <?>:支持任意泛型类型
2. <? extends A>:支持A类以及A类的子类
3. <? super A>:支持A类和A类的父类，不限于直接父类
### Junit
在方法上加`@Test`注解，引入依赖后，左侧边栏会出现三角执行按钮，就可以单独执行来测试了。

## 线程基础
### 进程
![process](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309211520556.PNG)

### 线程
![thread](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309211526939.PNG)
