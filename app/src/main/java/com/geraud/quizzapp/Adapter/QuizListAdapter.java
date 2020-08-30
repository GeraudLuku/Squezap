package com.geraud.quizzapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geraud.quizzapp.Model.Category;
import com.geraud.quizzapp.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<Category> categories;
    private OnQuizListItemClicked onQuizListItemClicked;

    public QuizListAdapter(OnQuizListItemClicked onQuizListItemClicked, List<Category> categories) {
        this.onQuizListItemClicked = onQuizListItemClicked;
        this.categories = categories;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        //category object
        Category category = categories.get(position);

        //set title of category
        holder.Title.setText(category.getName());

        //set category image
        Glide.with(holder.itemView.getContext())
                .load(category.getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.Image);

        String listDescription = categories.get(position).getDesc();
        if (listDescription.length() > 150) {
            listDescription = listDescription.substring(0, 100);
        }

        //set category description
        holder.Desc.setText(String.format("%s....",listDescription));
    }

    @Override
    public int getItemCount() {
        if (categories == null) {
            return 0;
        } else {
            return categories.size();
        }
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView Image;
        private TextView Title;
        private TextView Desc;
        private Button Btn;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            Image = itemView.findViewById(R.id.list_image);
            Title = itemView.findViewById(R.id.list_title);
            Desc = itemView.findViewById(R.id.list_desc);
            Btn = itemView.findViewById(R.id.list_btn);

            Btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizListItemClicked.onItemClicked(categories.get(getAdapterPosition()));
        }
    }

    public interface OnQuizListItemClicked {
        void onItemClicked(Category category);
    }
}
