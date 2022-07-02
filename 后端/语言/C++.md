CLion ： https://www.jetbrains.com/help/clion/2019.3/quick-tutorial-on-configuring-clion-on-windows.html#MinGW
Window10上CLion极简配置教程
	https://www.jianshu.com/p/1aa989808e15

C++命令无法编译，idea可以
枚举如何用
extern的原理很简单，就是告诉编译器：“你现在编译的文件中，有一个标识符虽然没有在本文件中定义，但是它是在别的文件中定义的全局变量，你要放行”
https://blog.csdn.net/qq_38916259/article/details/88060597
4月5号 到11号
整数是以原码的形式存储的，而负数是以补码的形式存储的



```
1. 在环境变量PATH 添加系统 MinGW 的实际安装位置,如: D:\Program Files\MinGW 或者比如本文中演示的C:\MinGW\bin。在PATH里加入D:\mingw64\bin（具体路径请根据你的MinGW选择）。如果PATH里面还有其他的变量，记得要加个英文半角分号。一般 PATH 中的变量会非常的多,不同变量之间使用;分隔。
2. 新建LIBRARY_PATH变量，如果有的话，在值中加入D:\mingw64\lib，这是标准库的位置。
3. 新建C_INCLUDEDE_PATH变量，值设为D:\mingw64\include。
4. 检查变量设置：Win+R输入: cmd ,在命令行中输入: g++ -v ,有内容输入证明环境变量配置正确.如果出现 'g++' 不是内部或外部命令，也不是可运行的程序或批处理文件。 检查上一步配置.
```

命名空间可以帮助我们避免不经意的名字定义冲突，以及使用库中相
同名字导致的冲突。标准库定义的所有名字都在命名空间std中。