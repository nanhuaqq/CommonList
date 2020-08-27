package com.qwx.commonlist.demo.list.repository;

import com.qwx.commonlist.demo.list.repository.PersonBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by qqin on 2020/8/27
 * <p>
 * email qqin@finbtc.net
 */
public enum  PersonRepository {
    INSTANCE;

    private int mockSeq = 1;

    PersonRepository(){}


    public Observable<List<PersonBean>> mockRequestFromServer(boolean isRefresh){
        if (isRefresh) {
            mockSeq = 1;
        }
        List<PersonBean> personBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PersonBean personBean = new PersonBean(mockSeq, "yaoming", "sport");
            personBeans.add(personBean);
            mockSeq++;
        }

        return Observable.just(personBeans);
    }

    public Observable<List<PersonBean>> mockLoadMore(){
        return mockRequestFromServer(false);
    }

}
