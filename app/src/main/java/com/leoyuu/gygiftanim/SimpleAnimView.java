package com.leoyuu.gygiftanim;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Queue;

/**
 * date 2020/9/15
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class SimpleAnimView extends FrameLayout {
    private Handler handler = new Handler();
    private Queue<GyAnimInfo> pendingAnimInfo = new LinkedList<>();
    private ShowingItem showingItem = null;
    private GyAnimItemView itemView;
    public SimpleAnimView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        itemView = new GyAnimItemView(context);
        addView(itemView);
        itemView.setVisibility(GONE);
        itemView.setYellow();
    }

    private Runnable refreshRunner = new Runnable() {
        @Override
        public void run() {
            checkRefresh();
        }
    };

    private void checkRefresh() {
        if (showingItem != null) {
            GyAnimInfo next = pendingAnimInfo.peek();
            if (next != null && next.mergeId() == showingItem.animInfo.mergeId()) {
                pendingAnimInfo.poll();
                showingItem.animInfo.merge(next);
                itemView.update(showingItem.animInfo);
                itemView.setVisibility(VISIBLE);
                handler.removeCallbacks(refreshRunner);
                showingItem.hideTime = SystemClock.elapsedRealtime() + showingItem.animInfo.animTimeInMill();
                handler.postDelayed(refreshRunner, showingItem.animInfo.animTimeInMill());
            } else if (showingItem.hideTime < SystemClock.elapsedRealtime()) {
                itemView.setVisibility(GONE);
                showingItem = null;
                handler.removeCallbacks(refreshRunner);
                handler.post(refreshRunner);
            }
        } else {
            GyAnimInfo next = pendingAnimInfo.poll();
            if (next != null) {
                showingItem = new ShowingItem(next);
                showingItem.hideTime = SystemClock.elapsedRealtime() + next.animTimeInMill();
                itemView.setVisibility(VISIBLE);
                itemView.enter(next);
                handler.removeCallbacks(refreshRunner);
                handler.postDelayed(refreshRunner, next.animTimeInMill());
            }
        }
    }

    public void send(GyAnimInfo gyAnimInfo) {
        pendingAnimInfo.offer(gyAnimInfo);
        handler.post(refreshRunner);
    }

    static class ShowingItem {
        GyAnimInfo animInfo;
        long hideTime = 0;

        public ShowingItem(GyAnimInfo animInfo) {
            this.animInfo = animInfo;
        }
    }
}
