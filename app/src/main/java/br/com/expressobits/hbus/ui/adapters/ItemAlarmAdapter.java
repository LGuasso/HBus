package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael Correa
 * @since 01/04/16
 */
public class ItemAlarmAdapter extends RecyclerView.Adapter<ItemAlarmAdapter.MyViewHolder> implements View.OnClickListener{

    private List<Alarm> alarmList;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Context context;
    //http://stackoverflow.com/questions/27203817/recyclerview-expand-collapse-items
    //private int expandedPosition = -1;

    public ItemAlarmAdapter(Context context,List<Alarm> alarmList){
        this.context = context;
        this.alarmList = alarmList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_alarm,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Alarm alarm = alarmList.get(position);
        //holder.itemView.setOnClickListener(ItemAlarmAdapter.this);
        //holder.itemView.setTag(holder);
        holder.textViewItineraryName.setText(FirebaseUtils.getItinerary(alarm.getId()));
        holder.textViewName.setText(alarm.getName()!=null&alarm.getName().length()>1?alarm.getName():context.getString(R.string.alarm));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTimeAlarm());
        holder.textViewTime.setText(HoursUtils.getFormatTime(calendar));
        holder.switchActived.setChecked(alarm.isActived());
        verifyIsActivedAlarm(holder,alarm);
        holder.switchActived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                AlarmDAO alarmDAO = new AlarmDAO(context);
                alarm.setActived(isChecked);
                alarmDAO.insert(alarm);
                String name = alarm.getName();
                if(name==null){
                    name = context.getString(R.string.alarm);
                }
                verifyIsActivedAlarm(holder,alarm);
            }
        });
        /**if (position == expandedPosition) {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        } else {
            holder.llExpandArea.setVisibility(View.GONE);
        }*/
        //holder.textViewDays.setText(alarm.getDaysOfWeek().toString());


    }

    /**
     * Forma ao UI se está ativado e responde esses comandos ao item view
     * @param alarm
     * @param holder
     */
    private void verifyIsActivedAlarm(MyViewHolder holder,Alarm alarm){
        if(alarm.isActived()){
            holder.textViewTime.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textViewName.setTextColor(context.getResources().getColor(R.color.subtext_color));
            holder.textViewItineraryName.setTextColor(context.getResources().getColor(R.color.subtext_color));
            holder.textViewSunday.setEnabled(alarm.isSunday());
            holder.textViewMonday.setEnabled(alarm.isMonday());
            holder.textViewTuesday.setEnabled(alarm.isTuesday());
            holder.textViewWednesday.setEnabled(alarm.isWednesday());
            holder.textViewThursday.setEnabled(alarm.isThursday());
            holder.textViewFriday.setEnabled(alarm.isFriday());
            holder.textViewSaturday.setEnabled(alarm.isSaturday());
        }else {
            holder.textViewTime.setTextColor(context.getResources().getColor(android.R.color.secondary_text_dark_nodisable));
            holder.textViewName.setTextColor(context.getResources().getColor(android.R.color.secondary_text_dark_nodisable));
            holder.textViewItineraryName.setTextColor(context.getResources().getColor(android.R.color.secondary_text_dark_nodisable));
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

    @Override
    public void onClick(View v) {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewName;
        public TextView textViewSunday;
        public TextView textViewMonday;
        public TextView textViewTuesday;
        public TextView textViewWednesday;
        public TextView textViewThursday;
        public TextView textViewFriday;
        public TextView textViewSaturday;
        public TextView textViewTime;
        public SwitchCompat switchActived;
        public LinearLayout relativeLayout;
        public TextView textViewItineraryName;
        public View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            relativeLayout = (LinearLayout) itemView.findViewById(R.id.relativeLayoutItemListAlarm);
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
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }

}
