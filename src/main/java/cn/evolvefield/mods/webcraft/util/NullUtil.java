package cn.evolvefield.mods.webcraft.util;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 0:09
 * Description:
 */
public class NullUtil<T> {

    private T mainMethod;


    public NullUtil(T method) {
        this.mainMethod = method;

    }

//    public T notN(){
//        if (mainMethod != null) return mainMethod;
//        else {InfoUtil.error("");}
//    }

}
