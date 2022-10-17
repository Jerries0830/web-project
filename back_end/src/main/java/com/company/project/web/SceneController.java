package com.company.project.web;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Scene;
import com.company.project.service.SceneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2022/05/02.
 */
@RestController
@RequestMapping("/scene")
public class SceneController {
    @Resource
    private SceneService sceneService;

    @PostMapping("/add")
    public Result add(Scene scene) {
        sceneService.save(scene);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Integer id) {
        sceneService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Scene scene) {
        sceneService.update(scene);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestBody Integer id) {
        Scene scene = sceneService.findById(id);
        return ResultGenerator.genSuccessResult(scene);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Integer page, @RequestBody Integer size) {
        PageHelper.startPage(page, size);
        List<Scene> list = sceneService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("")
    public Result getAllScenes() {
        JSONObject data = new JSONObject();
        List<Scene> list = sceneService.findAll();
        data.put("success", true);
        data.put("sceneList", list);
        return ResultGenerator.genSuccessResult(data);
    }

    @GetMapping("/{sceneID}")
    public Result getScene(@PathVariable int sceneID) {
        JSONObject data = new JSONObject();
        Scene scene = sceneService.findById(sceneID);
        data.put("success", true);
        data.put("scene", scene);
        return ResultGenerator.genSuccessResult(data);
    }
}
