package com.jun.control;

import com.jun.bean.Department;
import com.jun.bean.Employee;
import com.jun.bean.Msg;
import com.jun.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: ssm-CRUD
 * @description:
 * @author: Jun
 * @create: 2022-03-01 15:19
 **/
@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 返回所有的部门信息
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts(){
        //查出所有的部门信息
        List<Department> list =  departmentService.getDepts();
        return Msg.success().add("depts",list);
    }
}
