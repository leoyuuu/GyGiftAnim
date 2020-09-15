package com.leoyuu.gygiftanim;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class GyAnimItemView extends FrameLayout {
    private TextView tipTv;
    public GyAnimItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.gift_item, this);
        tipTv = findViewById(R.id.tip_tv);
    }

    void setYellow() {
        tipTv.setBackgroundColor(Color.YELLOW);
    }

    void update(GyAnimInfo animInfo) {
        tipTv.setText(animInfo.toString());
    }

    void enter(GyAnimInfo animInfo) {
        tipTv.setText(animInfo.toString());
        tipTv.post(new Runnable() {
            @Override
            public void run() {
                tipTv.setTranslationX(-tipTv.getMeasuredWidth());
                tipTv.animate().translationX(0).setDuration(200).start();
            }
        });
    }
}
