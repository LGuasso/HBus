package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 03/05/2015.
 */
public class ListViewAdapterBus extends ArrayAdapter<Onibus>{

    //Contexto do aplicativo
    Context context;
    //Inflater
    LayoutInflater layoutInflater;
    //Lista dos ônibus
    List<Onibus> onibusList;

    public ListViewAdapterBus(Context context, int id, List<Onibus> lista){
        super(context,id,lista);
        this.context = context;
        //this.lista = lista;
        this.onibusList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Onibus onibus = onibusList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.item_list_bus,null);

        //Iniciando o laoyout e views
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listView_background);
        if(position%2==0) {
            //layout.setBackgroundColor(context.getResources().getColor(R.color.orange_ultralight));
        }
        TextView textViewHorario = (TextView) view.findViewById(R.id.item_list_textview_horario);
        TextView textViewTempoFaltando = (TextView) view.findViewById(R.id.item_list_textview_tempo_faltando);
        TextView textViewDescricaoCodigo = (TextView) view.findViewById(R.id.item_list_textview_descricao_do_codigo);
        TextView textViewCodigo = (TextView) view.findViewById(R.id.item_list_textview_codigo);
        //ImageView imageView = (ImageView) view.findViewById(R.id.item_list_imageview);
        textViewHorario.setText(TimeUtils.calendarToTimeString(onibus.getTime()));
        textViewCodigo.setText(onibus.getCodigo().getId());
        //textViewCodigo.setText(context.getString(R.string.code_of_bus) + " " + onibus.getCodigo());
        // TODO Arrumar codigos que contem caracteres diferentes
        // TODO Separar a descrição por diferents Strings para poder trabalhar
        textViewDescricaoCodigo.setText(onibus.getCodigo().getDescricao());
        textViewDescricaoCodigo.setSelected(true);
        textViewTempoFaltando.setSelected(true);
        Calendar dataInicial = new GregorianCalendar();
        Calendar dataFinal = onibus.getTime();
        long horas = (dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis()) / 3600000;
        long minutos = (dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis() - horas*3600000) / 60000;
        String textoDoHorario = new String();
        textoDoHorario+="Em ";
        if(horas>=0){
            if(horas == 0){
                if(minutos>=0){
                    textoDoHorario+=minutos+" minuto(s)";
                    textViewTempoFaltando.setTextColor(context.getResources().getColor(R.color.red));
                }else{
                    horas+=24;
                    textoDoHorario+=horas+" hora(s)";
                    textViewTempoFaltando.setTextColor(context.getResources().getColor(R.color.black));
                }

            }else{
                textoDoHorario+=horas+" hora(s)";
                textViewTempoFaltando.setTextColor(context.getResources().getColor(R.color.green));
            }

        }else{
                horas+=24;
                textoDoHorario+=horas+" hora(s)";
                textViewTempoFaltando.setTextColor(context.getResources().getColor(R.color.black));


        }
        textViewTempoFaltando.setText(textoDoHorario);
        //imageView.setImageResource(android.R.drawable.ic_notification_clear_all);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
