package com.jqscp.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/29.
 */
public class SystemVersion implements Serializable {
    String Id;//版本id
    String version;//版本号
    String url;//更新地址
    int force;//是否必须更新 1 强制更新, 0不强制


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVersionNnumber() {
        return version;
    }

    public void setVersionNnumber(String versionNnumber) {
        version = versionNnumber;
    }

    public String getUpdateAddress() {
        return url;
    }

    public void setUpdateAddress(String updateAddress) {
        url = updateAddress;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }
}
