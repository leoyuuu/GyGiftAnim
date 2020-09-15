package com.leoyuu.gygiftanim.anim;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class AnimAdapter<T extends View, V extends AnimInfo<V>> extends RecyclerView.Adapter<AnimHolder<T, V>> {
    private SparseArray<Queue<V>> queueSparse = new SparseArray<>();
    private List<Item<V>> showingAnimList = new ArrayList<>();
    private AnimItemViewFactory<T, V> factory;
    private Handler handler = new Handler();

    public void setFactory(AnimItemViewFactory<T, V> factory) {
        this.factory = factory;
    }

    public void send(V animInfo) {
        int queueId = animInfo.queueId();
        Queue<V> queue = queueSparse.get(queueId);
        if (queue == null) {
            queue = new LinkedList<>();
            queueSparse.append(queueId, queue);
        }
        queue.offer(animInfo);
        checkRefreshShow();
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            checkRefreshShow();
        }
    };

    private void checkRefreshShow() {
        boolean noUpdate = checkUpdateCurrent();
        if (noUpdate) {
            noUpdate = checkAdd();
        }
        if (noUpdate) {
            checkRemove();
        }
    }

    private boolean checkUpdateCurrent() {
        int count = showingAnimList.size();
        for (int i = 0; i < count; i++) {
            Item<V> showingItem = showingAnimList.get(i);
            int queueId = showingItem.animInfo.queueId();
            Queue<V> queue = queueSparse.get(queueId);
            if (!queue.isEmpty()) {
                V nextItem = queue.peek();
                if (nextItem != null && nextItem.mergeId() == showingItem.animInfo.mergeId()) {
                    showingItem.animInfo.merge(queue.poll());
                    showingItem.endTime += nextItem.animTimeInMill();
                    showingAnimList.remove(i);
                    showingAnimList.add(0, showingItem);
                    notifyItemMoved(i, 0);
                    notifyItemChanged(0, 1);
                    handler.removeCallbacks(refreshRunnable);
                    handler.post(refreshRunnable);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkAdd() {
        int size = queueSparse.size();
        for (int i = 0; i < size; i++) {
            Queue<V> queue = queueSparse.valueAt(i);
            V animItem = queue.peek();
            if (animItem != null) {
                boolean hasShow = false;
                for (Item<V> item:showingAnimList) {
                    if (item.animInfo.queueId() == animItem.queueId()) {
                        hasShow = true;
                        break;
                    }
                }
                if (hasShow) {
                    continue;
                }
                queue.poll();
                Item<V> item = new Item<>();
                item.animInfo = animItem;
                item.endTime = SystemClock.elapsedRealtime() + animItem.animTimeInMill();
                showingAnimList.add(0, item);
                notifyItemInserted(0);
                handler.removeCallbacks(refreshRunnable);
                handler.post(refreshRunnable);
                return false;
            }
        }
        return true;
    }

    private void checkRemove() {
        long now = SystemClock.elapsedRealtime();
        int size = showingAnimList.size();
        long refreshTime = Long.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            Item<V> item = showingAnimList.get(i);
            if (item.endTime < now) {
                showingAnimList.remove(i);
                notifyItemRemoved(i);
                handler.removeCallbacks(refreshRunnable);
                handler.post(refreshRunnable);
                return;
            }
            if (item.endTime < refreshTime) {
                refreshTime = item.endTime;
            }
        }
        if (refreshTime != Long.MAX_VALUE) {
            long delayTime = refreshTime - now;
            handler.removeCallbacks(refreshRunnable);
            handler.postDelayed(refreshRunnable, delayTime);
        }
    }


    @NonNull
    @Override
    public AnimHolder<T, V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (factory == null) {
            throw new IllegalStateException("需要先初始化 factory 才能使用");
        }
        return new AnimHolder<>(factory, factory.genAnimView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimHolder<T, V> holder, int position) {
        holder.bindUpdate(showingAnimList.get(position).animInfo);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimHolder<T, V> holder, int position, @NonNull List<Object> payloads) {
        Item<V> item = showingAnimList.get(position);
        if (payloads.isEmpty()) {
            holder.bindEnter(item.animInfo);
        } else {
            holder.bindUpdate(item.animInfo);
        }
    }

    @Override
    public int getItemCount() {
        return showingAnimList.size();
    }

    static class Item<T> {
        T animInfo;
        long endTime = 0;
    }
}
