package com.example.voicerecorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AudioViewHolder> {

    //private File[] files;
    private List<File> files;
    private boolean play = false;

    private CorrectTime correctTime;

    private onItemListClick onItemListClick;

    public Adapter(List<File> files, onItemListClick onItemListClick){
        this.files = files;
        this.onItemListClick = onItemListClick;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        correctTime = new CorrectTime();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.list_title.setText(files.get(position).getName());
        holder.list_date.setText(correctTime.getCorrectTime(files.get(position).lastModified()));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private ImageButton deleteBtn;
        private TextView list_title;
        private TextView list_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_image = itemView.findViewById(R.id.imageView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            list_title = itemView.findViewById(R.id.titleText);
            list_date = itemView.findViewById(R.id.dateText);

            deleteBtn.setOnClickListener(this);
            list_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.deleteBtn:
                    onItemListClick.onDelete(files.get(getAdapterPosition()), getAdapterPosition());
                    break;
                case R.id.imageView:
                    try {
                        onItemListClick.onPlay(files.get(getAdapterPosition()), getAdapterPosition());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface onItemListClick{
        void onDelete(File file,int position);
        void onPlay(File file,int position) throws FileNotFoundException;
    }
}
