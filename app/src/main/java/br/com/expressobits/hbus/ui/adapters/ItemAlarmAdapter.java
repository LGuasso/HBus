package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa
 * @since 01/04/16
 */
public class ItemAlarmAdapter extends RecyclerView.Adapter<ItemAlarmAdapter.MyViewHolder>{

    private final List<Alarm> alarmList;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private final Context context;
    //http://stackoverflow.com/questions/27203817/recyclerview-expand-collapse-items
    //private int expandedPosition = -1;

    public ItemAlarmAdapter(Context context, List<Alarm> alarmList){
        this.context = context;
        this.alarmList = alarmList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_list_alarm,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Alarm alarm = alarmList.get(position);
        //holder.view.setOnClickListener(ItemAlarmAdapter.this);
        //holder.view.setTag(holder);
        holder.textViewItineraryName.setText(FirebaseUtils.getItinerary(alarm.getId()));
        holder.textViewName.setText(alarm.getName()!=null&alarm.getName().length()>1?alarm.getName():context.getString(R.string.alarm));
        holder.textViewTime.setText(TimeUtils.getFormatTime(alarm.getTimeAlarm()));
        holder.switchActived.setChecked(alarm.isActived());
        verifyIsActivedAlarm(holder,alarm);
        holder.switchActived.setOnCheckedChangeListener((buttonView, isChecked) -> {

            AlarmDAO alarmDAO = new AlarmDAO(context);
            alarm.setActived(isChecked);
            alarmDAO.insert(alarm);
            String name = alarm.getName();
            if(name==null){
                name = context.getString(R.string.alarm);
            }
            verifyIsActivedAlarm(holder,alarm);
        });


    }

    /**
     *  Form to UI if enabled and responds to the view item
     */
    private void verifyIsActivedAlarm(MyViewHolder holder,Alarm alarm){
        if(alarm.isActived()){
            holder.textViewTime.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.textViewName.setTextColor(ContextCompat.getColor(context,R.color.subtext_color));
            holder.textViewItineraryName.setTextColor(ContextCompat.getColor(context,R.color.subtext_color));
            holder.textViewSunday.setEnabled(alarm.isSunday());
            holder.textViewMonday.setEnabled(alarm.isMonday());
            holder.textViewTuesday.setEnabled(alarm.isTuesday());
            holder.textViewWednesday.setEnabled(alarm.isWednesday());
            holder.textViewThursday.setEnabled(alarm.isThursday());
            holder.textViewFriday.setEnabled(alarm.isFriday());
            holder.textViewSaturday.setEnabled(alarm.isSaturday());
        }else {
            holder.textViewTime.setTextColor(ContextCompat.getColor(context,android.R.color.secondary_text_dark_nodisable));
            holder.textViewName.setTextColor(ContextCompat.getColor(context,android.R.color.secondary_text_dark_nodisable));
            holder.textViewItineraryName.setTextColor(ContextCompat.getColor(context,android.R.color.secondary_text_dark_nodisable));
            holder.textViewSunday.setEnabled(false);
            holder.textViewMonday.setEnabled(false);
            holder.textViewTuesday.setEnabled(false);
            holder.textViewWednesday.setEnabled(false);
            holder.textViewThursday.setEnabled(false);
            holder.textViewFriday.setEnabled(false);
            holder.textViewSaturday.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView textViewName;
        final TextView textViewSunday;
        final TextView textViewMonday;
        final TextView textViewTuesday;
        final TextView textViewWednesday;
        final TextView textViewThursday;
        final TextView textViewFriday;
        final TextView textViewSaturday;
        final TextView textViewTime;
        final SwitchCompat switchActived;
        final TextView textViewItineraryName;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewSunday = (TextView) itemView.findViewById(R.id.textViewDaysSunday);
            textViewMonday = (TextView) itemView.findViewById(R.id.textViewDaysMonday);
            textViewTuesday = (TextView) itemView.findViewById(R.id.textViewDaysTuesday);
            textViewWednesday = (TextView) itemView.findViewById(R.id.textViewDaysWednesday);
            textViewThursday = (TextView) itemView.findViewById(R.id.textViewDaysThursday);
            textViewFriday = (TextView) itemView.findViewById(R.id.textViewDaysFriday);
            textViewSaturday = (TextView) itemView.findViewById(R.id.textViewDaysSaturday);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            switchActived = (SwitchCompat) itemView.findViewById(R.id.toggleActived);
            textViewItineraryName = (TextView)itemView.findViewById(R.id.textViewItineraryName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }

}
