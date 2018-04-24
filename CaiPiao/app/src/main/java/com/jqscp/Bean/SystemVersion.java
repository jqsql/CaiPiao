package com.jqscp.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/29.
 */
public class SystemVersion implements Serializable {
    String Id;//版本id
    String Version;//版本号
    String Address;//更新地址
    boolean IsForce;//是否必须更新


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVersionNnumber() {
        return Version;
    }

    public void setVersionNnumber(String versionNnumber) {
        Version = versionNnumber;
    }

    public String getUpdateAddress() {
        return Address;
    }

    public void setUpdateAddress(String updateAddress) {
        Address = updateAddress;
    }

    public boolean isNessary() {
        return IsForce;
    }

    public void setNessary(boolean nessary) {
        IsForce = nessary;
    }
}
