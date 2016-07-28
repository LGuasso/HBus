package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.alarm.AlarmEditorActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 *
 * @author Rafael
 * @since 24/06/2015
 */
public class ItemBusAdapter extends RecyclerView.Adapter<ItemBusAdapter.MyViewHolder> implements View.OnClickListener {


    private List<Bus> listBus;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Context context;
    private AlarmDAO alarmDAO;
    private int countLastBus;
    private static final int LASTRECENTLYBUSTITLE = 0;
    private static final int LASTRECENTLYBUS = 1;
    private static final int NOWBUS = 2;
    private static final int LASTBUS = 3;

    public ItemBusAdapter(Context context, List<Bus> listBus){
        this.context = context;
        Pair<Integer,List<Bus>> pair = HoursUtils.sortByTimeBus(listBus);
        //Pair<Integer,List<Bus>> pair = new Pair<>(0,listBus);
        this.listBus = pair.second;
        this.countLastBus = pair.first;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.alarmDAO = new AlarmDAO(context);

    }

    public ItemBusAdapter(Context context, List<Bus> listBus,int countLastBus){
        this(context, listBus);
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
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final Bus bus = listBus.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        myViewHolder.txtViewHorario.setText(sdf.format(bus.getTime()));
        /**Code code = db.getCode(cityId,listBus.get(i).getCode());
        if(code!=null) {
            myViewHolder.txtViewCode.setText(code.getName());
            myViewHolder.txtViewDescrition.setText(code.getDescrition());
        }
        myViewHolder.txtViewDescrition.setSelected(true);
        db.close();*/
        Log.e("TESTE",bus.getId());
        myViewHolder.txtViewCode.setText(bus.getCode());
        myViewHolder.txtViewDescrition.setText("Carregando...");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference codeTableRef  = database.getReference(FirebaseUtils.CODE_TABLE);
        DatabaseReference countryRef = codeTableRef.child(FirebaseUtils.getCountry(bus.getId()));
        DatabaseReference cityRef = countryRef.child(FirebaseUtils.getCityName(bus.getId()));
        Log.e("TESTE",FirebaseUtils.getCompany(bus.getId()));
        DatabaseReference companyRef = cityRef.child(FirebaseUtils.getCompany(bus.getId()));
        DatabaseReference codeRef = companyRef.child(bus.getCode());

        Log.e("TESTE",codeRef.toString());
        codeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Code code = dataSnapshot.getValue(Code.class);
                if(code!=null){
                    myViewHolder.txtViewDescrition.setText(code.getDescrition());
                }else {
                    myViewHolder.txtViewDescrition.setText("Erro ao carregar detalhes!");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myViewHolder.txtViewDescrition.setText("Erro "+"Code "+databaseError.getCode() +" - Details "+databaseError.getDetails()+ " "+
                databaseError.getMessage());
            }
        });


        final Alarm alarm = alarmDAO.getAlarm(bus.getId());
        if(myViewHolder.getItemViewType()==LASTBUS){
            myViewHolder.imageViewAlarm.setColorFilter(new
                    PorterDuffColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY));
        }
        if(alarm!=null){
            myViewHolder.imageViewAlarm.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_white_24dp));
            myViewHolder.imageViewAlarm.setAlpha(1f);
        }else {
            myViewHolder.imageViewAlarm.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_plus_white_24dp));
            myViewHolder.imageViewAlarm.setAlpha(0.5f);
        }
        myViewHolder.imageViewAlarm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Alarm alarm1 = alarmDAO.getAlarm(bus.getId());
                if (alarm1 != null) {
                    alarmDAO.delete(bus.getId());
                    Toast.makeText(context,
                            context.getString(R.string.alarm_removed), Toast.LENGTH_SHORT).show();
                    ((AppCompatImageView) v).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_plus_white_24dp));
                    v.setAlpha(0.5f);
                } else {
                    Alarm alarm = new Alarm();
                    alarm.setId(bus.getId());
                    alarm.setSunday(true);
                    alarm.setMonday(true);
                    alarm.setTuesday(true);
                    alarm.setWednesday(true);
                    alarm.setThursday(true);
                    alarm.setFriday(true);
                    alarm.setSaturday(true);
                    alarm.setActived(true);
                    alarm.setMinuteDelay(-5);
                    alarm.setTimeAlarm(TextUtils.getTimeWithDelayTime(new SimpleDateFormat("HH:mm").format(bus.getTime()), alarm.getMinuteDelay()));
                    alarmDAO.insert(alarm);
                    Toast.makeText(context,
                            context.getString(R.string.alarm_added), Toast.LENGTH_SHORT).show();
                    ((AppCompatImageView) v).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_white_24dp));
                    v.setAlpha(1f);
                }
                return true;
            }
        });

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

    @Override
    public void onClick(View v) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtViewHorario;
        public TextView txtViewCode;
        public TextView txtViewDescrition;
        public RelativeLayout relativeLayout;
        public AppCompatImageView imageViewAlarm;
        public View itemView;

        public MyViewHolder(View itemView) {

            super(itemView);

            this.itemView = itemView;

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.linear_layout_list_bus_square);
            txtViewHorario = (TextView) itemView.findViewById(R.id.item_list_textview_horario);
            txtViewCode = (TextView) itemView.findViewById(R.id.item_list_textview_codigo);
            txtViewDescrition = (TextView) itemView.findViewById(R.id.item_list_textview_descricao_do_codigo);
            imageViewAlarm = (AppCompatImageView) itemView.findViewById(R.id.item_list_imageview);
            imageViewAlarm.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                switch (v.getId()) {
                    case R.id.item_list_imageview:
                        Intent intent = new Intent(context, AlarmEditorActivity.class);
                        String busId = listBus.get(getPosition()).getId();
                        String code = listBus.get(getPosition()).getCode();
                        intent.putExtra(AlarmEditorActivity.ARGS_ALARM_ID, busId);
                        intent.putExtra(AlarmEditorActivity.ARGS_ALARM_CODE, code);
                        context.startActivity(intent);
                        break;
                }
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
