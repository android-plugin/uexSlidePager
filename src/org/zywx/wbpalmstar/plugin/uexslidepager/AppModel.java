package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.io.Serializable;

public class AppModel implements Serializable{

    private static final long serialVersionUID = -8600248750197456362L;
    private long id;
    private String introduction;
    private String detail;
    private String bgColor;
    private String iconUrl;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getBgColor() {
        return bgColor;
    }
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }   
}
