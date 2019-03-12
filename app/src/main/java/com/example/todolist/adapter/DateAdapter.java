package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.bean.ListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class NormalViewHolder extends ListAdapter.ViewHolder {
        ImageView status;
        TextView content;
        public NormalViewHolder(@NonNull View itemView,int itemViewType) {
            super(itemView);
            if(itemViewType==ListItem.TYPE_EMPTY)
                return;
            status = itemView.findViewById(R.id.date_item_status);
            content = itemView.findViewById(R.id.date_item_content);
        }
    }
    private List<ListItem> dataList;
    private Context context;

    public DateAdapter(Context context,List<ListItem> dataList){
        this.dataList=dataList;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType==ListItem.TYPE_EMPTY){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_empty_item,parent,false);
        }else if(viewType==ListItem.TYPE_NORMAL){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_normal_item,parent,false);
        }
        return new NormalViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType=getItemViewType(position);
        if(itemViewType==ListItem.TYPE_NORMAL){
            int status=dataList.get(position).getStatus();
            String content=dataList.get(position).getContent();
            //todo 这里根据status的情况，setImageRes，完成未完成
            ((NormalViewHolder)holder).status.setImageResource(R.drawable.emotion1);
            ((NormalViewHolder)holder).content.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        if(dataList.size()==0){
            return 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size()==0){
            return ListItem.TYPE_EMPTY;
        }else{
            return ListItem.TYPE_NORMAL;
        }
    }
}
