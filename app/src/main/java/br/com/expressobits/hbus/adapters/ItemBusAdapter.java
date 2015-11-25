package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 *
 * @author Rafael
 * @since 24/06/2015
 */
public class ItemBusAdapter extends RecyclerView.Adapter<ItemBusAdapter.MyViewHolder> {


    private List<Bus> listBus;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Context context;

    public ItemBusAdapter(Context context, List<Bus> listBus){
        this.context = context;
        this.listBus = TimeUtils.sortByTimeBus(listBus);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_list_bus,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        //Método que atualiza informações
        myViewHolder.txtViewHorario.setText(listBus.get(i).getTime());
        //TODO fixar bug de tomorrow no bus
         if(i%2==0) {
          myViewHolder.relativeLayout.setSelected(true);
        }
        myViewHolder.txtViewCode.setText(listBus.get(i).getCode().getName());
        myViewHolder.txtViewDescrition.setText(listBus.get(i).getCode().getDescrition());
        myViewHolder.txtViewDescrition.setSelected(true);
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
        public RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.linear_layout_list_bus_square);
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
