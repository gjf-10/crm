package com.yjxxt.crm.treeDto;

public class TreeDto {
    private Integer id;
    private String moduleName;
    private Integer pId;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TreeDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", pId=" + pId +
                ", selected=" + selected +
                '}';
    }
}
