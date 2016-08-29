package org.zywx.wbpalmstar.plugin.uexslidepager.vo;

import java.io.Serializable;

public class OpenOptionVO implements Serializable{
    private static final long serialVersionUID = 1538674582255554017L;
    private boolean isShowIcon = true;
    private boolean isShowContent = true;

    public boolean isShowIcon() {
        return isShowIcon;
    }

    public void setIsShowIcon(boolean isShowIcon) {
        this.isShowIcon = isShowIcon;
    }

    public boolean isShowContent() {
        return isShowContent;
    }

    public void setIsShowContent(boolean isShowContent) {
        this.isShowContent = isShowContent;
    }
}
