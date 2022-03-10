package com.jun.test;

import com.jun.bean.Employee;
import com.jun.dao.DepartmentMapper;
import com.jun.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;


/**
 * 调试dao层的工作
 * @author ZJ
 * 推荐Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要的组件
 * 1、导入SpringTest的依赖
 * 2、@ContextConfiguration指定Spring配置文件的位置
 * 3、直接@Autowired要使用的组件即可
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;

    /**
     * 测试DepartmentMapper
     */
    @Test
    public void testCRUD() {
        /*
         * //1、创建SpringIOC容器
         * ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
         * //2、从容器中获取mapper
         * DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*/
        System.out.print(departmentMapper);

        //插入几个部门
        //		departmentMapper.insertSelective(new Department(null,"开发部"));
        //		departmentMapper.insertSelective(new Department(null,"测试部"));

        //		employeeMapper.insertSelective(new Employee(null,"Marry","M","Marry@jun.com",1));

        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for(int i = 0; i<500; i++) {
            String uSex;
            int uDept;
            if(i % 3 == 0) {
                uSex = "M";
                uDept = 1;
            }else {
                uSex = "F";
                uDept = 2;
            }
            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
            mapper.insertSelective(new Employee(null,uid,uSex,uid+"@jun.com",uDept));
        }

        System.out.println("批量完成！");

    }

}