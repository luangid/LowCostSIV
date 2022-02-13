package com.luan_g.drawerlowsiv.entity;

import java.util.ArrayList;
import java.util.List;

public class FileEntity {
    private int rootId = -1;
    private int id;
    private int level;
    private String name;
    private List<FileEntity> listChildren;

    public FileEntity(int id, String name) {
        this.id = id;
        this.name = name;
        this.level = 0;
        this.listChildren = new ArrayList<>();
    }
    public FileEntity(int id, String name, int rootId, int level) {
        this.id = id;
        this.name = name;
        this.rootId = rootId;
        this.level = level;
    }

    public void addChildren(FileEntity file) {
        this.listChildren.add(file);
    }
    public void setList(List<FileEntity> list){
        this.listChildren = list;

    }


    public String getName() {
        return this.name;
    }

    public List<FileEntity> getListChildren() {
        return this.listChildren;
    }

    public int getRootId() {
        return this.rootId;
    }

    public int getId() {
        return this.id;
    }

    public int getLevel() {
        return level;
    }
}
