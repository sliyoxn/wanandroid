package com.example.sorena.wanandroidapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 导航左边列表的实体类
 */

public class NavListItem implements Serializable
{
    private String name;
    private boolean select;
    private List<NavFlowItem> flowItems;

    public NavListItem(String name, boolean select, List<NavFlowItem> flowItems) {
        this.name = name;
        this.select = select;
        this.flowItems = flowItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NavListItem{" +
                "name='" + name + '\'' +
                ", select=" + select +
                ", flowItems=" + flowItems +
                '}';
    }

    public List<NavFlowItem> getFlowItems() {
        return flowItems;
    }

    public void setFlowItems(List<NavFlowItem> flowItems) {
        this.flowItems = flowItems;
    }

}
