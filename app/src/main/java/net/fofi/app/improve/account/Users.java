package net.fofi.app.improve.account;

/**
 * Created by ZYY on 2018/2/28.
 */

public class Users {
    private Integer uid;
    private String utelephone;
    private String upassword;
    public Integer getId() {
        return uid;
    }
    public void setId(Integer id) {
        this.uid = id;
    }
    public String getUtelephone() {
        return utelephone;
    }
    public void setUtelephone(String utelephone) {
        this.utelephone = utelephone;
    }
    public String getUpassword() {
        return upassword;
    }
    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }
}
