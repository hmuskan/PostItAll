package com.example.muskanhussain.postitall.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muskanhussain.postitall.Model.Blog;
import com.example.muskanhussain.postitall.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Blog blog = blogList.get(i);
        String imgUrl = blog.getPostImage();
        Picasso.get().load(imgUrl).into(viewHolder.imageView);
        viewHolder.postTitle.setText(blog.getPostTitle());
        viewHolder.desc.setText(blog.getPostDesc());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimeStamp())).getTime());
        viewHolder.timeStamp.setText(formattedDate);
        imgUrl = blog.getPostImage();


    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView postTitle, desc, timeStamp;
        public ImageView imageView;
        String userId ;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            postTitle = itemView.findViewById(R.id.postTitleList);
            desc = itemView.findViewById(R.id.postTextList);
            timeStamp = itemView.findViewById(R.id.timestampList);
            imageView = itemView.findViewById(R.id.postImageList);
            userId = null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next activity
                }
            });
        }
    }
}
