# model2table
## Mybatis 使用 model 自动创建表/更新表结构/动态建表
首先，下面的代码都是基于 [孙琛斌这位大兄弟的‘Mybatis自动创建表/更新表结构/动态建表’](http://blog.csdn.net/sun5769675/article/details/51757867)这篇文章进化而来的，这里非常感谢该博主，受该博主的启发，使我的技术提升了不少，非常感谢！

下面进入正题：
一，说说能做什么

 1. 能使用 model 自动创建表
 2. 可以自动初始化一部分数据到数据库中
 3. 使用 model 进行基本的增删改查

二、先来说说不同的地方（好多啊，被我改的面目全非了(*/ω╲*)）

 1. 直接干掉非 Mysql 数据库部分的代码了
 2. Table.java 和 Column.java 注解都增加了 comment 属性，也就是表和字段注释
 3. Column.java 增加了 unsigned 属性，也就是数字字段无符号属性，可以增加数字字段的容量
 4. 增加 InitData.java 方法注解，用于注解执行完数据表初始化结束后需要初始化数据的方法
 5. 增加 ConfigLoder 类，用于加载配置文件 model2table.properties。原来的是通过 spring 注解的方式加载的。
 6. MySqlTypeConstant.java 增加 tinyint 类型，也就是 MySQL 的布尔类型。
 7. ClassTools.java 也做了部分修改，添加扫描用 @InitData注解的类的方法。
 8. 扫描的 model 的路径添加通配符支持，不过只能是 ** 两个星号。
 9. 然后还有更多小细节就比一一详述了，基本每个类都有动过Ψ(￣∀￣)Ψ.
 
三、使用方法
 
 
1.在 resources 文件夹下建立 model2table.properties
 

```
# mybatis.table.auto=create | update | none
mybatis.table.auto=update  
# mybatis.table.initData=none | insert
mybatis.table.initData=insert
# 支持通配符，不过只支持 ** 两个星号
mybatis.model.pack=com.wb.**.model
# 因为类型和生成的 sql 是 MySQL 版本的，暂时只支持 MySQL
mybatis.database.type=mysql
```

   2.在 配置文件中添加下面的
 

```
	<bean id="baseDaoSupport" class="com.wb.plugins.model2table.command.BaseDaoSupportImpl">
		<property name="sqlSessionFactory" ref="sqlSessionFactory" />
	</bean>
```
3.按照原来的做法就是自己想办法扫描到 mapper 文件了，我用的是通配符。

```
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSourceLog4j" />
		<property name="mapperLocations">
			<array>
				<value>classpath*:**/*-mapper.xml</value>
			</array>
		</property>
		<!-- 加载mybatis全局配置文件 -->
		<property name="configLocation" value="classpath:mybatis-config.xml"></property>
	</bean>
```
4.接下来就是编写一个 model 测试运行了

```
package com.wb.sys.model;

import java.io.Serializable;
import com.wb.plugins.model2table.annotation.Column;
import com.wb.plugins.model2table.annotation.Table;
import com.wb.plugins.model2table.constants.MySqlTypeConstant;

/**
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月25日 下午7:33:29
 */
@Table(name = "sys_dict2", comment = "字典表")
public class SysDict implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Column(name = "id", isUnsigned = true, type = MySqlTypeConstant.BIGINT, length = 64, isKey = true, isNull = false, comment = "字典表ID")
	private Long id;
    
    @Column(name = "parentId", isUnsigned = true, type = MySqlTypeConstant.BIGINT, length = 64, isNull = true, comment = "父级ID")
    private Long parentId;
    
    @Column(name = "path", type = MySqlTypeConstant.VARCHAR, length = 100, isUnique = true, comment = "树路径")
    private String path;
    
    @Column(name = "dictName", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "字典名称")
    private String dictName;
    
    @Column(name = "code", type = MySqlTypeConstant.VARCHAR, length = 20, comment = "字典编码")
    private String code;
    
    @Column(name = "orderNo", type = MySqlTypeConstant.INT, length = 5, comment = "排序号")
	private Integer	orderNo;
    
    @Column(name = "hasChildren", type = MySqlTypeConstant.TINYINT, length = 1, comment = "有子节点，0否，1是")
    private boolean	hasChildren;

    //省略 getter 和 setter 方法了。。。。
    
}

```
5.编写初始化数据的类，后面的就应该可以自己写了，去吧，皮卡丘

```
package com.wb.sys.model;

import com.wb.plugins.model2table.annotation.InitData;

/**
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月26日 下午6:34:41
 */
public class TestOne {

	@InitData
	public void initData() {
		System.out.println("\n\n\n===================TestOne");
	}

}

```

最后说两句吧：修改的原因是为了配合现在的开发，原来的和现在的系统不太兼容所以就修改了，同时也为了迎合业务需求吧，方便项目的部署与初始化
