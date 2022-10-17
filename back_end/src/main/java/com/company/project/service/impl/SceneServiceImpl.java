package com.company.project.service.impl;

import com.company.project.dao.SceneMapper;
import com.company.project.model.Scene;
import com.company.project.service.SceneService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2022/05/02.
 */
@Service
@Transactional
public class SceneServiceImpl extends AbstractService<Scene> implements SceneService {
    @Resource
    private SceneMapper sceneMapper;

}
