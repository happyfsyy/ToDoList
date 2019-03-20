package com.example.todolist.adapter;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.bean.ListItem;
import com.example.todolist.listener.OnBackPressListener;
import com.example.todolist.listener.OnClickListener;
import com.example.todolist.listener.OnNextListener;
import com.example.todolist.listener.OnTextChangeListener;
import com.example.todolist.utils.LogUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView status;
        public EditText content_edit;
        public TextView content_text;
        public ImageView finish;
        public ImageView unFinish;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content_edit=itemView.findViewById(R.id.item_content_edit);
            this.content_text=itemView.findViewById(R.id.item_content_text);
            this.status=itemView.findViewById(R.id.item_status);
            this.finish=itemView.findViewById(R.id.item_finish);
            this.unFinish=itemView.findViewById(R.id.item_unfinish);
        }
    }
    private List<ListItem> dataList;
    private Context context;
    private LayoutTransition mTransitioner;
    private OnClickListener onFinishListener;
    private OnClickListener onUnFinishListener;
    private OnNextListener onNextListener;
    private OnTextChangeListener onTextChangeListener;
    private OnBackPressListener onBackPressListener;
    private boolean isNewItem;
    public ListAdapter(List<ListItem> dataList){
        this.dataList=dataList;
        mTransitioner=new LayoutTransition();
        isNewItem=true;
    }
    public void setOnFinishListener(OnClickListener onFinishListener){
        this.onFinishListener=onFinishListener;
    }
    public void setOnUnFinishListener(OnClickListener onUnFinishListener){
        this.onUnFinishListener=onUnFinishListener;
    }
    public void setOnNextListener(OnNextListener onNextListener){
        this.onNextListener=onNextListener;
    }
    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener){
        this.onTextChangeListener = onTextChangeListener;
    }
    public void setOnBackPressListener(OnBackPressListener onBackPressListener){
        this.onBackPressListener=onBackPressListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View itemView= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(itemView);
        ((ViewGroup)viewHolder.itemView).setLayoutTransition(mTransitioner);
        initEditListener(viewHolder);
        initButtonListener(viewHolder);
        return viewHolder;
    }
    private void initEditListener(final ViewHolder viewHolder){
        viewHolder.content_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int pos=viewHolder.getAdapterPosition();
                onTextChangeListener.onTextChange(s,pos);
            }
        });
        viewHolder.content_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int pos=viewHolder.getAdapterPosition();
                onNextListener.onNext(pos);
                return true;
            }
        });
        LogUtil.e("setOnKeyListener");
        viewHolder.content_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtil.e("keyCode="+keyCode+"\tevent="+event.getAction());
                LogUtil.e("keycode_del="+KeyEvent.KEYCODE_DEL+"\taction_down="+KeyEvent.ACTION_DOWN);
                if(event.getAction()==KeyEvent.ACTION_DOWN&&
                    event.getKeyCode()==KeyEvent.KEYCODE_DEL){
                    EditText editText=(EditText)v;
                    LogUtil.e("执行onBackPress了");
                    onBackPressListener.onBackPress(editText,viewHolder.getAdapterPosition());
                }
                return false;
            }
        });
    }
    private void initButtonListener(final ViewHolder viewHolder){
        viewHolder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                onFinishListener.onClick(v,pos);
            }
        });
        viewHolder.unFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                onUnFinishListener.onClick(v,pos);
            }
        });
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        LogUtil.e("onBindViewHolder");
        int status=dataList.get(position).getStatus();
        String content=dataList.get(position).getContent();
        switch (status){
            case ListItem.NO_CONTENT:
                holder.content_edit.setText("");
                holder.content_edit.setVisibility(View.VISIBLE);
                holder.content_text.setVisibility(View.GONE);
                holder.status.setImageResource(R.drawable.circle);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
            case ListItem.NO_RECORD:
                holder.content_edit.setText(content);
                holder.content_edit.setVisibility(View.VISIBLE);
                holder.content_text.setVisibility(View.GONE);
                holder.status.setImageResource(R.drawable.circle);
                holder.finish.setVisibility(View.VISIBLE);
                holder.unFinish.setVisibility(View.VISIBLE);
                break;
            case ListItem.FINISH:
                holder.content_edit.setVisibility(View.GONE);
                holder.content_text.setVisibility(View.VISIBLE);
                holder.content_text.setText(content);
                holder.status.setImageResource(R.drawable.finish);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
            case ListItem.UNFINISH:
                holder.content_edit.setVisibility(View.GONE);
                holder.content_text.setVisibility(View.VISIBLE);
                holder.content_text.setText(content);
                holder.status.setImageResource(R.drawable.un_finish);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
        }
        if(isNewItem&&position==dataList.size()-1){
            holder.content_edit.requestFocus();
            holder.content_edit.setSelection(content.length());
        }

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setNewItem(boolean newItem) {
        isNewItem = newItem;
    }
}
