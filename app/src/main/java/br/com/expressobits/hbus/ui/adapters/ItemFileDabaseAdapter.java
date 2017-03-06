package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.viewholder.FileDatabaseViewHolder;

/**
 * @author Rafael Correa
 * @since 06/03/17
 */

public class ItemFileDabaseAdapter extends RecyclerView.Adapter<FileDatabaseViewHolder> implements RecyclerViewOnClickListenerHack {

    private LayoutInflater layoutInflater;
    private List<File> filesDatabases;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ItemFileDabaseAdapter(Context context, List<File> filesDatabases){
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.filesDatabases = filesDatabases;
    }


    @Override
    public FileDatabaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_list_file_database,parent,false);
        FileDatabaseViewHolder fileDatabaseViewHolder = new FileDatabaseViewHolder(view);
        fileDatabaseViewHolder.setRecyclerViewOnClickListenerHack(this);
        return fileDatabaseViewHolder;
    }

    @Override
    public void onBindViewHolder(FileDatabaseViewHolder holder, int position) {
        File file = filesDatabases.get(position);
        holder.textViewFileName.setText(file.getName());
        holder.textViewFileSize.setText(file.length()+" bytes");
    }

    @Override
    public int getItemCount() {
        return filesDatabases.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    @Override
    public void onClickListener(View view, int position) {
        recyclerViewOnClickListenerHack.onClickListener(view, position);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return recyclerViewOnClickListenerHack.onLongClickListener(view, position);
    }
}
