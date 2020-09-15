package com.leoyuu.gygiftanim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leoyuu.gygiftanim.anim.AnimHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSendListener {
    private AnimHelper<GyAnimItemView, GyAnimInfo> animHelper = new AnimHelper<>();
    private SimpleAnimView simpleAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleAnimView = findViewById(R.id.simple_anim);
        animHelper.setFactory(new GyViewFactory());
        animHelper.attachRv((RecyclerView) findViewById(R.id.anim_rv));
        RecyclerView rv = findViewById(R.id.send_rv);
        rv.setAdapter(new Adapter(this));
        rv.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void onSend(int sender, int giftId, int giftNum) {
        animHelper.send(new GyAnimInfo(sender, giftId, giftNum, 12345));
        simpleAnimView.send(new GyAnimInfo(sender, giftId, giftNum, 12345));
    }


    static class Adapter extends RecyclerView.Adapter<Holder> {
        private List<SendItem> list = new ArrayList<>();
        private OnSendListener onSendListener;

        public Adapter(OnSendListener onSendListener) {
            this.onSendListener = onSendListener;
            genList();
        }

        private void genList() {
            for (int i = 0; i < 100; i++) {
                list.add(new SendItem(i + 1, 1, 1));
                list.add(new SendItem(i + 1, 1, 5));
                list.add(new SendItem(i + 1, 1, 10));
                list.add(new SendItem(i + 1, 2, 1));
                list.add(new SendItem(i + 1, 2, 5));
                list.add(new SendItem(i + 1, 2, 10));
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(new AppCompatButton(parent.getContext()), onSendListener);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    static class Holder extends RecyclerView.ViewHolder {
        SendItem item;
        OnSendListener listener;
        public Holder(@NonNull View itemView, final OnSendListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send();
                }
            });
        }

        void bind(SendItem item) {
            this.item = item;
            ((TextView)itemView).setText(item.toString());
        }

        private void send() {
            listener.onSend(item.sender, item.giftId, item.giftNum);
        }
    }

    static class SendItem {
        int sender;
        int giftId;
        int giftNum;

        public SendItem(int sender, int giftId, int giftNum) {
            this.sender = sender;
            this.giftId = giftId;
            this.giftNum = giftNum;
        }

        @NonNull
        @Override
        public String toString() {
            return "{" + sender + ", " + giftId + ", " + giftNum + "}";
        }
    }

}