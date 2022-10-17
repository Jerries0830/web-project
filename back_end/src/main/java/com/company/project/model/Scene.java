package com.company.project.model;

import javax.persistence.*;

public class Scene {
    @Id
    @Column(name = "scene_id")
    private Integer sceneId;

    private String name;

    private String path;

    /**
     * @return scene_id
     */
    public Integer getSceneId() {
        return sceneId;
    }

    /**
     * @param sceneId
     */
    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }
}