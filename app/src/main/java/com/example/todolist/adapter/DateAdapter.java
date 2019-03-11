package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.bean.ListItem;

import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.NormalViewHolder> {
    class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        TextView content;

        public NormalViewHolder(@NonNull View itemView,int type) {
            super(itemView);
            if(type==ListItem.TYPE_HEADER&&headerView!=null)
                return;
            if(type==ListItem.TYPE_EMPTY)
                return;
            status = itemView.findViewById(R.id.date_item_status);
            content = itemView.findViewById(R.id.date_item_content);
        }
    }
    private List<ListItem> dataList;
    private Context context;
    private View headerView;

    public DateAdapter(Context context,List<ListItem> dataList){
        this.dataList=dataList;
        this.context=context;
    }
    @NonNull
    @Override
    public DateAdapter.NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;
        if(headerView!=null&&viewType==ListItem.TYPE_HEADER){
            //todo 这里改为每一个itemview对应一种viewholder试试看，哪怕只有一个textview
//            itemView= LayoutInflater.from(context).inflate(R.layout.date_header_item,parent,false);
            itemView=headerView;
        }else if(viewType==ListItem.TYPE_EMPTY){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_empty_item,parent,false);
        }else if(viewType==ListItem.TYPE_NORMAL){
            itemView=LayoutInflater.from(context).inflate(R.layout.date_normal_item,parent,false);
        }
        return new NormalViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.NormalViewHolder holder, int position) {
        int itemViewType=getItemViewType(position);
        if(itemViewType==ListItem.TYPE_NORMAL){
            int pos=getListPos(position);
            int status=dataList.get(pos).getStatus();
            String content=dataList.get(pos).getContent();
            //todo 这里根据status的情况，setImageRes，完成未完成
            holder.status.setImageResource(R.drawable.emotion1);
            holder.content.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        if(dataList.size()==0){
            if(headerView!=null){
                return 2;
            }else{
                return 1;
            }
        }else{
            if(headerView!=null){
                return dataList.size()+1;
            }else{
                return dataList.size();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(headerView!=null&&position==0) return ListItem.TYPE_HEADER;
        if(dataList.size()==0) return ListItem.TYPE_EMPTY;
        return ListItem.TYPE_NORMAL;
    }
    public void addHeaderView(View headerView){
        this.headerView=headerView;
        notifyItemInserted(0);
    }
    private int getListPos(int pos){
        return headerView==null?pos:pos-1;
    }
}
