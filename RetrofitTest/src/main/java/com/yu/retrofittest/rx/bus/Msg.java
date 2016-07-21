package com.yu.retrofittest.rx.bus;

/**
 * Created by Android on 2016/6/15.
 */
public class Msg {
    public int code;
    public Object object;

    public Msg(int code, Object object){
        this.code = code;
        this.object = object;
    }

}
