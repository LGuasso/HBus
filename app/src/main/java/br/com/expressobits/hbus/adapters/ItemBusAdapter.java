package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.BusHelper;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.HoursUtils;

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
    private String cityId;
    private int countLastBus;
    private static final int LASTRECENTLYBUSTITLE = 0;
    private static final int LASTRECENTLYBUS = 1;
    private static final int NOWBUS = 2;
    private static final int LASTBUS = 3;

    public ItemBusAdapter(Context context, List<Bus> listBus,String cityId){
        this.cityId = cityId;
        this.context = context;
        Pair<Integer,List<Bus>> pair = HoursUtils.sortByTimeBus(listBus);
        this.listBus = pair.second;
        this.countLastBus = pair.first;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ItemBusAdapter(Context context, List<Bus> listBus,String cityId,int countLastBus){
        this(context, listBus, cityId);
        this.countLastBus = countLastBus;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;
        switch (type){
            case LASTRECENTLYBUSTITLE:
                view = layoutInflater.inflate(R.layout.item_list_bus_last_recently_title,viewGroup,false);
                break;
            case LASTRECENTLYBUS:
                view = layoutInflater.inflate(R.layout.item_list_bus_last_recently,viewGroup,false);
                break;
            case NOWBUS:
                view = layoutInflater.inflate(R.layout.item_list_bus_now,viewGroup,false);
                break;
            default:
                view = layoutInflater.inflate(R.layout.item_list_bus,viewGroup,false);
                break;
        }


        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        //Tip: codepath site in about type of viewholder
        if(countLastBus==2){
            switch (position){
                case 0:
                    return LASTRECENTLYBUSTITLE;
                case 1:
                    return LASTRECENTLYBUS;
                case 2:
                    return NOWBUS;
                default:
                    return LASTBUS;
            }
        }else if(countLastBus==1){
            switch (position){
                case 0:
                    return LASTRECENTLYBUSTITLE;
                case 1:
                    return NOWBUS;
                default:
                    return LASTBUS;
            }
        }else if(countLastBus==0){
            switch (position){
                case 0:
                    return NOWBUS;
                default:
                    return LASTBUS;
            }

        }
        return LASTBUS;

    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {


        BusDAO db = new BusDAO(context);

        myViewHolder.txtViewHorario.setText(listBus.get(i).getTime());
        Code code = db.getCode(cityId,listBus.get(i).getCode());
        if(code!=null) {
            myViewHolder.txtViewCode.setText(code.getName());
            myViewHolder.txtViewDescrition.setText(code.getDescrition());
        }
        myViewHolder.txtViewDescrition.setSelected(true);
        db.close();

        if(BuildConfig.VERSION_CODE<79){
            myViewHolder.imageViewAlarm.setVisibility(View.INVISIBLE);
        }

        //Metodo que atualiza informacoes

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
        public ImageView imageViewAlarm;
        public View itemView;

        public MyViewHolder(View itemView) {

            super(itemView);

            this.itemView = itemView;

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.linear_layout_list_bus_square);
            txtViewHorario = (TextView) itemView.findViewById(R.id.item_list_textview_horario);
            txtViewCode = (TextView) itemView.findViewById(R.id.item_list_textview_codigo);
            txtViewDescrition = (TextView) itemView.findViewById(R.id.item_list_textview_descricao_do_codigo);
            imageViewAlarm = (ImageView) itemView.findViewById(R.id.item_list_imageview);
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
