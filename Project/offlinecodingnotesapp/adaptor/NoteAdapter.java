package com.example.offlinecodingnotesapp.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinecodingnotesapp.activities.AddNoteActivity;
import com.example.offlinecodingnotesapp.R;
import com.example.offlinecodingnotesapp.database.AppDatabase;
import com.example.offlinecodingnotesapp.database.NoteEntity;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    public interface OnNoteChangeListener {
        void onNoteDeleted();
        void onFavoriteChanged();
    }

    private Context context;
    private List<NoteEntity> list;
    private AppDatabase db;
    private OnNoteChangeListener listener;

    public NoteAdapter(Context context, List<NoteEntity> list) {
        this.context = context;
        this.list = list;
        this.db = AppDatabase.getInstance(context);
    }

    public void setOnNoteChangeListener(OnNoteChangeListener listener) {
        this.listener = listener;
    }

    public void updateList(List<NoteEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate, tvLanguage;
        ImageView ivFavorite;
        View vLangDot;
        View btnCopy, btnShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            vLangDot = itemView.findViewById(R.id.vLangDot);
            btnCopy = itemView.findViewById(R.id.btnCopy);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteEntity note = list.get(holder.getAdapterPosition());

        if (holder.tvTitle != null) holder.tvTitle.setText(note.title != null ? note.title : "");
        if (holder.tvContent != null) holder.tvContent.setText(note.content != null ? note.content : "");
        if (holder.tvDate != null) holder.tvDate.setText(note.date != null ? note.date : "");
        if (holder.tvLanguage != null) holder.tvLanguage.setText(note.language != null ? note.language : "");
        
        // Favorite state
        if (holder.ivFavorite != null) {
            if (note.isFavorite) {
                holder.ivFavorite.setImageResource(R.drawable.ic_star_filled);
                holder.ivFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFD700")));
            } else {
                holder.ivFavorite.setImageResource(R.drawable.ic_star_outline);
                holder.ivFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#5C608A")));
            }
        }

        // Language colors
        int langColor = Color.parseColor("#888888");
        if (note.language != null) {
            switch (note.language.toLowerCase()) {
                case "java": langColor = Color.parseColor("#F8981D"); break;
                case "javascript": langColor = Color.parseColor("#FFD700"); break;
                case "typescript": langColor = Color.parseColor("#3178C6"); break;
                case "python": langColor = Color.parseColor("#3776AB"); break;
                case "c++": langColor = Color.parseColor("#00599C"); break;
            }
        }
        if (holder.tvLanguage != null) holder.tvLanguage.setTextColor(langColor);
        if (holder.vLangDot != null) holder.vLangDot.setBackgroundTintList(ColorStateList.valueOf(langColor));

        // Copy functionality
        if (holder.btnCopy != null) {
            holder.btnCopy.setOnClickListener(v -> {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Snippet", note.content);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show();
            });
        }

        // Share functionality
        if (holder.btnShare != null) {
            holder.btnShare.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "--- " + note.title + " (" + note.language + ") ---\n\n" + note.content);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Share Snippet via");
                context.startActivity(shareIntent);
            });
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNoteActivity.class);
            intent.putExtra("noteId", note.id);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setTitle("Delete Note")
                    .setMessage("Move this snippet to trash?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        int currentPos = holder.getAdapterPosition();
                        if (currentPos != RecyclerView.NO_POSITION) {
                            new Thread(() -> {
                                db.noteDao().delete(note);
                                ((android.app.Activity)context).runOnUiThread(() -> {
                                    list.remove(currentPos);
                                    notifyItemRemoved(currentPos);
                                    if (listener != null) listener.onNoteDeleted();
                                });
                            }).start();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        holder.ivFavorite.setOnClickListener(v -> {
            // Check if we are in FavoritesFragment to disable unfavoriting
            if (listener instanceof fragments.FavoritesFragment) {
                Toast.makeText(context, "Cannot unfavorite from here", Toast.LENGTH_SHORT).show();
                return;
            }

            note.isFavorite = !note.isFavorite;
            
            if (note.isFavorite) {
                holder.ivFavorite.setImageResource(R.drawable.ic_star_filled);
                holder.ivFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFD700")));
            } else {
                holder.ivFavorite.setImageResource(R.drawable.ic_star_outline);
                holder.ivFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#5C608A")));
            }

            new Thread(() -> {
                db.noteDao().update(note);
                if (listener != null) {
                    ((android.app.Activity)context).runOnUiThread(() -> listener.onFavoriteChanged());
                }
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}