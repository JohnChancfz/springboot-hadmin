package com.sparrow.hadmin.controller.admin.system;

import com.sparrow.hadmin.common.JsonResult;
import com.sparrow.hadmin.common.utils.MD5Utils;
import com.sparrow.hadmin.controller.BaseController;
import com.sparrow.hadmin.entity.Role;
import com.sparrow.hadmin.entity.User;
import com.sparrow.hadmin.service.IRoleService;
import com.sparrow.hadmin.service.IUserService;
import com.sparrow.hadmin.service.specification.SimpleSpecificationBuilder;
import com.sparrow.hadmin.service.specification.SpecificationOperator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 贤云
 * @deprecated 用户管理
 **/
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    /**
     * 用户管理初始化页面
     *
     * @return
     */
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "admin/user/index";
    }

    /**
     * 查询集合
     *
     * @return
     */
    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Page<User> list() {
        SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<User>();
        String searchText = request.getParameter("searchText");
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("nickName", Operator.likeAll.name(), searchText);
        }
        Page<User> page = userService.findAll(builder.generateSpecification(), getPageRequest());
        return page;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        return "admin/user/form";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        User user = userService.find(id);
        map.put("user", user);
        return "admin/user/form";
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(User user, ModelMap map) {
        try {
            userService.saveOrUpdate(user);
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id, ModelMap map) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
    public String grant(@PathVariable Integer id, ModelMap map) {
        User user = userService.find(id);
        map.put("user", user);

        Set<Role> set = user.getRoles();
        List<Integer> roleIds = new ArrayList<Integer>();
        for (Role role : set) {
            roleIds.add(role.getId());
        }
        map.put("roleIds", roleIds);

        List<Role> roles = roleService.findAll();
        map.put("roles", roles);
        return "admin/user/grant";
    }

    @ResponseBody
    @RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
    public JsonResult grant(@PathVariable Integer id, String[] roleIds, ModelMap map) {
        try {
            userService.grant(id, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @RequestMapping(value = "/password/{id}", method = RequestMethod.GET)
    public String password(@PathVariable Integer id, ModelMap map) {

        User user = userService.find(id);

        map.put("user", user);

        return "admin/user/password";
    }

    @ResponseBody
    @RequestMapping(value = "/save_password/{id}", method = RequestMethod.POST)
    public JsonResult savePassword(@PathVariable Integer id, ModelMap map) {
        try {
            String oldPassword = request.getParameter("oldPassword");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (!StringUtils.isNotBlank(oldPassword)) {
                return JsonResult.failure("请输入旧密码！");
            }

            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword)) {
                if (!password.equals(confirmPassword)) {
                    return JsonResult.failure("两次密码不一致");
                }
            } else {
                return JsonResult.failure("请输入新密码/确认密码");
            }

            User user = userService.find(id);
            String userPassword = user.getPassword();
            if (userPassword.equals(MD5Utils.md5(oldPassword))) {
                user.setPassword(MD5Utils.md5(password));
                userService.saveOrUpdate(user);
                return JsonResult.success("密码修改成功！");
            } else {
                return JsonResult.failure("旧密码错误！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        //return JsonResult.success();
    }
}
