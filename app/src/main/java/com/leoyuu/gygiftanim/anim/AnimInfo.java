package com.leoyuu.gygiftanim.anim;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public interface AnimInfo<T extends AnimInfo<T>> {
    int queueId();
    int mergeId();
    void merge(T animInfo);
    int animTimeInMill();
}
