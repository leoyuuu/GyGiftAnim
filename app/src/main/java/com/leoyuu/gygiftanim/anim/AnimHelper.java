package com.leoyuu.gygiftanim.anim;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class AnimHelper<T extends View, V extends AnimInfo<V>> {
    private AnimAdapter<T, V> adapter = new AnimAdapter<>();

    public void setFactory(AnimItemViewFactory<T, V> factory) {
        adapter.setFactory(factory);
    }

    public void attachRv(RecyclerView rv) {
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext(), RecyclerView.VERTICAL, true));
    }

    public void send(V animInfo) {
        adapter.send(animInfo);
    }
}
