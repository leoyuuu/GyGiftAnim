package com.leoyuu.gygiftanim;

import androidx.annotation.NonNull;

import com.leoyuu.gygiftanim.anim.AnimInfo;

/**
 * date 2020/9/14
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GyAnimInfo implements AnimInfo<GyAnimInfo> {
    int sender;
    int giftId;
    int giftNum;
    int receiver;

    public GyAnimInfo(int sender, int giftId, int giftNum, int receiver) {
        this.sender = sender;
        this.giftId = giftId;
        this.giftNum = giftNum;
        this.receiver = receiver;
    }

    @Override
    public int queueId() {
        return sender;
    }

    @Override
    public int mergeId() {
        return giftId;
    }

    @Override
    public void merge(GyAnimInfo animInfo) {
        giftNum += animInfo.giftNum;
    }

    @Override
    public int animTimeInMill() {
        return 2000;
    }

    @NonNull
    @Override
    public String toString() {
        return "用户" + sender + "送给 收礼人 " + giftNum + "\n个 礼物【"+ + giftId + "】";
    }
}
