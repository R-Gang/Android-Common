package com.gang.library.common.utils.version;

import com.vector.update_app.UpdateAppBean;

public class UpdateBean extends UpdateAppBean {

    //新版本号
    private String new_version_code;

    public String getNewVersionCode() {
        return new_version_code;
    }

    public void setNewVersionCode(String new_version_code) {
        this.new_version_code = new_version_code;
    }
}
