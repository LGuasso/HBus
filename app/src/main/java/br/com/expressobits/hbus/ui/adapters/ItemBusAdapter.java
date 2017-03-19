package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.ui.alarm.AlarmEditorActivity;
import br.com.expressobits.hbus.utils.StringUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 *
 * @author Rafael Correa
 * @since 24/06/2015
 */
public class ItemBusAdapter extends RecyclerView.Adapter<ItemBusAdapter.MyViewHolder> implements View.OnClickListener {


    private final List<Bus> listBus;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final AlarmDAO alarmDAO;
    private final int countLastBus;
    private static final int LASTRECENTLYBUSTITLE = 0;
    private static final int LASTRECENTLYBUS = 1;
    private static final int NOWBUS = 2;
    private static final int LASTBUS = 3;

    public ItemBusAdapter(Context context, List<Bus> listBus){
        this.context = context;
        Pair<Integer,List<Bus>> pair = sortByTimeBus(listBus);
        this.listBus = pair.second;
        this.countLastBus = pair.first;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.alarmDAO = new AlarmDAO(context);

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
        //Tip: code path site in about type of view holder
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
        bus.setCode(bus.getCode().replace(" ",""));
        myViewHolder.txtViewSchedule.setText(TimeUtils.getFormatTime(bus.getTime()));
        if(bus.getCode().length()>Code.CODE_LENGTH_TO_DESCRIPTION){
            myViewHolder.txtViewDescription.setText(bus.getCode());
        }else {
            myViewHolder.txtViewCode.setText(bus.getCode());
            myViewHolder.txtViewDescription.setText(context.getString(R.string.loading));
            ScheduleDAO dao = new ScheduleDAO(context,
                    SQLConstants.getCountryFromBusId(bus.getId()),
                    SQLConstants.getCityFromBusId(bus.getId()));
            Code code = dao.getCode(SQLConstants.getCompanyFromBusId(bus.getId()),
                    SQLConstants.getItinerary(bus.getId()),
                    bus.getCode());
            dao.close();
            if (code != null) {
                myViewHolder.txtViewDescription.setText(code.getDescrition());
            } else {
                Log.e("TESTE",SQLConstants.getCountryFromBusId(bus.getId())+" "+
                        SQLConstants.getCityFromBusId(bus.getId())+" "+
                        SQLConstants.getCompanyFromBusId(bus.getId())+" "+
                        bus.getCode());
                myViewHolder.txtViewDescription.setText(context.getString(R.string.error_loading_description));
            }
        }

        final Alarm alarm = alarmDAO.getAlarm(bus.getId());
        if(myViewHolder.getItemViewType()==LASTBUS){
            myViewHolder.imageViewAlarm.setColorFilter(new
                    PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY));
        }
        if(alarm!=null){
            myViewHolder.imageViewAlarm.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_alarm_white_24dp));
            myViewHolder.imageViewAlarm.setAlpha(1f);
        }else {
            myViewHolder.imageViewAlarm.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_alarm_plus_white_24dp));
            myViewHolder.imageViewAlarm.setAlpha(0.5f);
        }
        myViewHolder.imageViewAlarm.setOnLongClickListener(v -> {
            Alarm alarm1 = alarmDAO.getAlarm(bus.getId());
            if (alarm1 != null) {
                alarmDAO.delete(bus.getId());
                Toast.makeText(context,
                        context.getString(R.string.alarm_removed), Toast.LENGTH_SHORT).show();
                ((AppCompatImageView) v).setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_alarm_plus_white_24dp));
                v.setAlpha(0.5f);
            } else {
                Alarm alarm2 = new Alarm();
                alarm2.setId(bus.getId());
                alarm2.setSunday(true);
                alarm2.setMonday(true);
                alarm2.setTuesday(true);
                alarm2.setWednesday(true);
                alarm2.setThursday(true);
                alarm2.setFriday(true);
                alarm2.setSaturday(true);
                alarm2.setActived(true);
                alarm2.setMinuteDelay(-5);
                alarm2.setTimeAlarm(StringUtils.getTimeWithDelayTime(TimeUtils.getFormatTime(bus.getTime()), alarm2.getMinuteDelay()));
                alarmDAO.insert(alarm2);
                Toast.makeText(context,
                        context.getString(R.string.alarm_added), Toast.LENGTH_SHORT).show();
                ((AppCompatImageView) v).setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_alarm_white_24dp));
                v.setAlpha(1f);
            }
            return true;
        });
        myViewHolder.txtViewDescription.setSelected(true);

    }

    @Override
    public int getItemCount() {
        return listBus.size();
    }

    @Override
    public void onClick(View v) {

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView txtViewSchedule;
        final TextView txtViewCode;
        final TextView txtViewDescription;
        final AppCompatImageView imageViewAlarm;

        MyViewHolder(View itemView) {

            super(itemView);

            txtViewSchedule = (TextView) itemView.findViewById(R.id.item_list_textview_horario);
            txtViewCode = (TextView) itemView.findViewById(R.id.item_list_textview_codigo);
            txtViewDescription = (TextView) itemView.findViewById(R.id.item_list_textview_descricao_do_codigo);
            imageViewAlarm = (AppCompatImageView) itemView.findViewById(R.id.item_list_imageview);
            imageViewAlarm.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_list_imageview:
                    Intent intent = new Intent(context, AlarmEditorActivity.class);
                    Bus bus = listBus.get(getAdapterPosition());
                    String busId = bus.getId();
                    String code = bus.getCode();
                    intent.putExtra(AlarmEditorActivity.ARGS_ALARM_ID, busId);
                    intent.putExtra(AlarmEditorActivity.ARGS_ALARM_CODE, code);
                    intent.putExtra(AlarmEditorActivity.ARGS_ALARM_TYPE_DAY,StringUtils.getTypeDayInt(bus.getTypeday()));
                    context.startActivity(intent);
                    break;
            }

        }
    }

    private static Pair<Integer,List<Bus>> sortByTimeBus(List<Bus> busList){
        Bus bus = new Bus();
        bus.setTime(Calendar.getInstance().getTimeInMillis());
        List<Bus> busFinal = new ArrayList<>();
        ArrayList<Bus> twoLastBuses = new ArrayList<>();
        ArrayList<Bus> lastBuses = new ArrayList<>();
        ArrayList<Bus> nextBuses = new ArrayList<>();

        int countTwoLast;

        for (int i=0;i<busList.size();i++){
            if(TimeUtils.getHour(busList.get(i).getTime())> TimeUtils.getHour(bus.getTime())){
                nextBuses.add(busList.get(i));
            }else if(TimeUtils.getHour(busList.get(i).getTime())< TimeUtils.getHour(bus.getTime())){
                lastBuses.add(busList.get(i));

            }else{
                if(TimeUtils.getMinute(busList.get(i).getTime())>= TimeUtils.getMinute(bus.getTime())){
                    nextBuses.add(busList.get(i));
                }else if(TimeUtils.getMinute(busList.get(i).getTime())< TimeUtils.getMinute(bus.getTime())) {
                    lastBuses.add(busList.get(i));
                }
            }
        }

        if(lastBuses.size()>1){
            Bus bus2 = lastBuses.get(lastBuses.size() - 2);
            Bus bus1 = lastBuses.get(lastBuses.size() - 1);
            twoLastBuses.add(bus2);
            lastBuses.remove(bus2);
            //It has to be inverted to appear underneath the latest
            twoLastBuses.add(bus1);
            lastBuses.remove(bus1);

        }else if(lastBuses.size()>0){
            Bus bus1 = lastBuses.get(lastBuses.size() - 1);
            twoLastBuses.add(bus1);
            lastBuses.remove(bus1);
        }

        countTwoLast = twoLastBuses.size();


        busFinal.addAll(twoLastBuses);

        busFinal.addAll(nextBuses);

        busFinal.addAll(lastBuses);

        return new Pair<>(countTwoLast,busFinal);
    }
}
