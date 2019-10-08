/**
 * 该包用于改进原有的excel相关的接口,相对于以前,主要优化以下几点:
 * 	1.以前,需要各自生成一个Config类继承一个ExcelConfig类,现在使用ExcelConfigMap来保存所有的ExcelConfig
 * 	2.以前,需要为每一个ExcelConfig类指定其properties文件位置,现在,可以使用package指定某个文件夹下的properties的位置
 *  3.以前,每一个Controller类中,只有一个Service,以此来保证一个特定的url对应一个excel配置
 *  	现在,使用 url和service的对应Map,并将该引用链添加到controllerHandler中,以实现一个Controller中
 *  	对应多个service,并保证每个service与url的一一对应
 * 如下几点并没有变化:
 * 	1.对每一个生成excel的实体类,仍然需要相关的Service<>子类 (很难避免,因为难以有一个统一的方式去得到数据集合)
 * 		// 返回list的方法返回值可以修改为 Map(LinkedHashMap) 以提高通用性;泛型仅作为标志该Service的手段
 *  2.对每一个生成excel的实体类,仍然需要有相关的配置文件,并需要在总体的配置文件中引入
 *  	// 在以后的版本中,可能会用数据库表的形式代替这些配置文件
 * 目前,对某个类型,如果其想要增加excel下载的功能,则在后端,需要如下操作:
 * 	1.继承/实现ExcelController接口,实现 getConfig() 方法,返回一个ExcelConfig
 *  2.在某个路径下,
 * @author wangp
 *
 */
package com.git.template.general.excel;