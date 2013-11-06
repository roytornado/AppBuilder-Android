package com.midland.base.model;

import com.midland.base.util.MD5;
import com.midland.base.util.SimpleCrypto;

public class User {

    public String user_id;
    public String company_id;
    public String current_team_id;
    public String password;
    public String emp_id;
    public String name;
    public String short_name;
    public String email;
    public String dept_id;
    public String dept_code;
    public String dept_name;
    public String jid;
    public String seq;
    public String bm;

    public String getPassForApi() {
        return MD5.getMD5(SimpleCrypto.decrypt(password));
    }
}
