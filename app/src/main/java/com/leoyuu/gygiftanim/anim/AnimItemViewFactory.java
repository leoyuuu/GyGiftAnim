package com.leoyuu.gygiftanim.anim;

import android.content.Context;
import android.view.View;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public interface AnimItemViewFactory<T extends View, V extends AnimInfo<V>> {
    T genAnimView(Context context);
    void update(T view, V animInfo);
    void showEnter(T view, V animInfo);
}
