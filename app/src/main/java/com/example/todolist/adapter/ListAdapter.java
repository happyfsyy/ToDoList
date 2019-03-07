package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.bean.ListItem;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.bean.ListItem.ItemStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder{
        EditText content_edit;
        TextView content_text;
        ImageView status;
        ImageView finish;
        ImageView unFinish;
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
    public ListAdapter(List<ListItem> dataList){
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View itemView= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        viewHolder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("点击了完成按钮");
            }
        });
        viewHolder.unFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("点击了未完成按钮");
            }
        });
        //todo 监听editText，输入完成之后（或者失去焦点之后），右侧显示出Finish和UnFinish，当心convertView复用，editText焦点判断不准确
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemStatus status=dataList.get(position).getStatus();
        String content=dataList.get(position).getContent();
        switch (status){
            case NO_CONTENT:
                //todo 判断你是不是当前第一个no_content，如果是的话，就editText可以编辑，否则就是个TextView
                holder.content_edit.setText("");
                if(position!=0){
                    holder.content_edit.setFocusable(false);
                }
                holder.status.setVisibility(View.INVISIBLE);
                holder.finish.setVisibility(View.GONE);
                holder.unFinish.setVisibility(View.GONE);
                break;
            case NO_RECORD:
                holder.content_edit.setText(content);
                holder.status.setVisibility(View.GONE);
                holder.finish.setVisibility(View.VISIBLE);
                holder.unFinish.setVisibility(View.VISIBLE);
                break;
            case FINISH:
                holder.content_edit.setText(content);
                //todo holder.status.setResources() 完成按钮
                holder.finish.setVisibility(View.GONE);
                holder.unFinish.setVisibility(View.GONE);
                break;
            case UNFINISH:
                holder.content_edit.setText(content);
                //todo holder.status.setResources() 未完成按钮
                holder.finish.setVisibility(View.GONE);
                holder.unFinish.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
