package com.leoyuu.gygiftanim.anim;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class AnimHolder<T extends View, V extends AnimInfo<V>> extends RecyclerView.ViewHolder {
    private AnimItemViewFactory<T, V> factory;
    private T view;
    public AnimHolder(AnimItemViewFactory<T, V> factory, @NonNull T itemView) {
        super(itemView);
        this.factory = factory;
        this.view = itemView;
    }

    void bindUpdate(V animInfo) {
        factory.update(view, animInfo);
    }

    void bindEnter(V animInfo) {
        factory.showEnter(view, animInfo);
    }
}
