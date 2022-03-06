package com.example.yourquerybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterProjects extends RecyclerView.Adapter<AdapterProjects.MyViewHolder> {

    private RecyclerViewClickListener recyclerViewClickListener;
    Context context;
    ArrayList<ProjectList> list;
    private static RecyclerViewClickListener listener;

    public AdapterProjects(Context context, ArrayList<ProjectList> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        AdapterProjects.listener =listener;
    }
    public AdapterProjects(Context context, ArrayList<ProjectList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.project_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProjectList projectList = list.get(position);
        holder.title.setText(projectList.getCount()+" "+projectList.getTitle());
        holder.description.setText(projectList.getDescription());
        holder.link.setText(projectList.getProjectLink());

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(v,holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView description, title, link;
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.NoticeTitle);
            description=itemView.findViewById(R.id.NoticeDesc);
            link=itemView.findViewById(R.id.NoticeLink);
            mView = itemView;
            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position=getAdapterPosition();
//                    recyclerViewClickListener.onLongClick(itemView,position);
//                    Toast.makeText(context.getApplicationContext(), "Long pressed", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });
//            itemView.setOnLongClickListener((View.OnLongClickListener) this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
//        public void onLongClick(View v) {
//            listener.onLongClick(itemView, getAdapterPosition());
//        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }
}
