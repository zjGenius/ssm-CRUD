package com.jun.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jun.bean.Employee;
import com.jun.bean.Msg;
import com.jun.service.EmployeeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;

/**
 * 处理员工CRUD请求
 *
 * @author ZJ
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    /**
     * 检查用户名是否可用
     *
     * @param empName
     * @return
     */
    @RequestMapping(value = "/checkuser", method = RequestMethod.POST)
    @ResponseBody
    public Msg checkuser(@RequestParam("empName") String empName) {
        //先判断用户名是否可用
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "后:用户名必须是6-16位数字或者2-5位的字母组合");
        }
        boolean b = employeeService.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail().add("va_msg", "后:用户名重复");
        }
    }

    /**
     * 员工保存
     *
     * @param employee
     * @return 新增支持JSR303校验 需要导入hibernate validator
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的错误信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误的字段名:" + fieldError.getField());
                System.out.println("错误信息:" + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    /**
     * 想要@ResponseBody生效，需要导入jackson包
     *
     * @param pageNumber
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        //	这不是一个分页查询
        //	引入PageHelper分页插件
        //	在查询之前只需要调用，传入页码，以及每页大小
        PageHelper.startPage(pageNumber, 5);
        //	startPage后面紧跟的查询就是分页查询
        List<Employee> emps = employeeService.getAll();
        //	使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
        //	封装了详细的分页信息，包括有我们查询出来的数据
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 员工更新方法
     *
     * 解决方案：
     * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
     * 1、配置上HttpPutFormContentFilter
     * 2、它的作用：将请求体中的数据解析包装成一个map
     * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取出
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(Employee employee){
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 员工删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/emp/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("ids") String ids){
        //批量删除
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String string : str_ids) {
                del_ids.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_ids);
        } else {
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(){
        Employee employee = null;
        try {
            employee = employeeService.test();
        } catch (Exception e) {
            return e.toString();
        }
        return employee.toString();
    }

    /**
     * 	查询员工数据（分页查询）
     * @return
     */
//    @RequestMapping("/emps")
//    public String getEmps(@RequestParam(value="pageNumber",defaultValue="1") Integer pageNumber,
//                          Model model) {
//
//        model.addAttribute("pageInfo",page);
//
//        return "list";
//    }

}
