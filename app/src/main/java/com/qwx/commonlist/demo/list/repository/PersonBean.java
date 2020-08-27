package com.qwx.commonlist.demo.list.repository;

import com.qwx.commonlist.lib.ViewBean;

/**
 * Created by qqin on 2020/8/27
 * <p>
 * email qqin@finbtc.net
 */
public class PersonBean implements ViewBean {
    public int seq;
    public String name;
    public String hobby;

    public PersonBean() {
    }

    public PersonBean(String name, String hobby) {
        this.name = name;
        this.hobby = hobby;
    }

    public PersonBean(int seq, String name, String hobby) {
        this.seq = seq;
        this.name = name;
        this.hobby = hobby;
    }
}
