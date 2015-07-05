package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.modelo.Bus;

/**
 * Created by Rafael on 24/06/2015.
 */
public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {


    private List<Bus> listBus;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public BusAdapter(Context context,List<Bus> listBus){
        this.listBus = listBus;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_list_bus,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        //Método que atualiza informações
        myViewHolder.txtViewHorario.setText(listBus.get(i).getTime());
        myViewHolder.txtViewCode.setText(listBus.get(i).getCodigo().getId());
        myViewHolder.txtViewDescrition.setText(listBus.get(i).getCodigo().getDescricao());
    }

    @Override
    public int getItemCount() {
        return listBus.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    public void addListItem(Bus bus,int position){
        listBus.add(bus);
        notifyItemInserted(position);
    }

    public void removeListItem(int position){
        listBus.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtViewHorario;
        public TextView txtViewCode;
        public TextView txtViewDescrition;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtViewHorario = (TextView) itemView.findViewById(R.id.item_list_textview_horario);
            txtViewCode = (TextView) itemView.findViewById(R.id.item_list_textview_codigo);
            txtViewDescrition = (TextView) itemView.findViewById(R.id.item_list_textview_descricao_do_codigo);
            //itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
