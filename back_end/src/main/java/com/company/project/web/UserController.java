package com.company.project.web;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dto.UserDetail;
import com.company.project.jwt.JwtTokenUtil;
import com.company.project.model.User;
import com.company.project.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by CodeGenerator on 2022/05/02.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @PostMapping("/add")
    public Result add(User user) {
        userService.save(user);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Integer id) {
        userService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(User user) {
        userService.update(user);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestBody Integer id) {
        User user = userService.findById(id);
        return ResultGenerator.genSuccessResult(user);
    }

//    @PostMapping("/list")
//    public Result list(@RequestBody Integer page, @RequestBody Integer size) {
//        PageHelper.startPage(page, size);
//        List<User> list = userService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }

    @GetMapping("/list")
    public Result list() {
        List<User> list = userService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/register")
    public Result register(@RequestBody String request) {
        JSONObject data = new JSONObject();
        JSONObject json = JSONObject.parseObject(request);
        String username = json.getString("username");
        String password = json.getString("password");
        String rePassword = json.getString("rePassword");

        if (username == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "请填写用户名");
        }
        if (password == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "请填写密码");
        }
        if (!password.equals(rePassword)) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "两次输入的密码不一致");
        }

        User user = userService.findBy("username", username);
        if (user != null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "用户名已存在");
        }

        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRegisterTime(new Date());
        userService.save(user);
        data.put("success", true);
        return ResultGenerator.genSuccessResult(data, "注册成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody String request) {
        JSONObject data = new JSONObject();
        JSONObject json = JSONObject.parseObject(request);
        String username = json.getString("username");
        String password = json.getString("password");

        if (username == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "请填写用户名");
        }
        if (password == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "请填写密码");
        }

        User user = userService.findBy("username", username);
        if (user == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "用户不存在");
        }
        if (!password.equals(user.getPassword())) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "密码错误");
        }

        String token = jwtTokenUtil.generateToken(user);
        data.put("success", true);
        data.put("token", token);
        return ResultGenerator.genSuccessResult(data, "登录成功");
    }

    @PostMapping("/get_user")
    public Result getUser(@RequestHeader("token") String token) {
        JSONObject data = new JSONObject();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username == null) {
            data.put("success", false);
            return ResultGenerator.genSuccessResult(data, "token不合法");
        }
        User user = userService.findBy("username", username);
        data.put("success", true);
        data.put("userDetail", new UserDetail(user));
        return ResultGenerator.genSuccessResult(data);
    }
}
