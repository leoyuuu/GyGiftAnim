package com.leoyuu.gygiftanim;

import android.content.Context;

import com.leoyuu.gygiftanim.anim.AnimItemViewFactory;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GyViewFactory implements AnimItemViewFactory<GyAnimItemView, GyAnimInfo> {
    @Override
    public GyAnimItemView genAnimView(Context context) {
        return new GyAnimItemView(context);
    }

    @Override
    public void update(GyAnimItemView view, GyAnimInfo animInfo) {
        view.update(animInfo);
    }

    @Override
    public void showEnter(GyAnimItemView view, GyAnimInfo animInfo) {
        view.enter(animInfo);
    }
}
