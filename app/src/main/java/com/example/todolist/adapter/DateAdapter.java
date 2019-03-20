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

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.NormalViewHolder> {

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
    public DateAdapter.NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType==ListItem.TYPE_EMPTY){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_empty_item,parent,false);
        }else if(viewType==ListItem.TYPE_NORMAL){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_normal_item,parent,false);
        }
        return new NormalViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.NormalViewHolder holder, int position) {
        int itemViewType=getItemViewType(position);
        if(itemViewType==ListItem.TYPE_NORMAL){
            int status=dataList.get(position).getStatus();
            String content=dataList.get(position).getContent();
            if(status==ListItem.FINISH){
                holder.status.setImageResource(R.drawable.finish);
            }else if(status==ListItem.UNFINISH){
                holder.status.setImageResource(R.drawable.un_finish);
            }else if(status==ListItem.NO_RECORD){
                holder.status.setImageResource(R.drawable.circle);
            }
            holder.content.setText(content);
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
