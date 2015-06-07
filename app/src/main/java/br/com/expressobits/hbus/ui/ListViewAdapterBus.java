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

/**
 * Created by Rafael on 03/05/2015.
 */
public class ListViewAdapterBus extends ArrayAdapter<Onibus>{

    //Contexto do aplicativo
    Context context;
    //Inflater
    LayoutInflater layoutInflater;
    //Lista dos �nibus
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
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview);
        TextView textViewUltimo = (TextView) view.findViewById(R.id.item_list_textview_ultimo);
        TextView textViewDescricao = (TextView) view.findViewById(R.id.item_list_textview_codigo);
        TextView textViewCodigo = (TextView) view.findViewById(R.id.item_list_code);
        //ImageView imageView = (ImageView) view.findViewById(R.id.item_list_imageview);
        textView.setText(onibus.getHorario());
        textViewCodigo.setText(onibus.getCodigo().getId());
        //textViewCodigo.setText(context.getString(R.string.code_of_bus) + " " + onibus.getCodigo());
        textViewDescricao.setText(onibus.getCodigo().getDescricao());
        textViewDescricao.setSelected(true);
        textViewUltimo.setSelected(true);
        textView.setTextAppearance(context, R.style.Base_TextAppearance_AppCompat_Large);
        Calendar dataInicial = new GregorianCalendar();
        Calendar dataFinal = new GregorianCalendar();
        dataFinal.set(Calendar.HOUR_OF_DAY,onibus.getHora());
        dataFinal.set(Calendar.MINUTE,onibus.getMinuto());
        long horas = (dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis()) / 3600000;
        long minutos = (dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis() - horas*3600000) / 60000;
        String textoDoHorario = new String();
        textoDoHorario+="Chegando em ";
        if(horas>=0){
            if(horas == 0){
                if(minutos>=0){
                    textoDoHorario+=minutos+" minuto(s)";
                    textViewUltimo.setTextColor(context.getResources().getColor(R.color.red));
                }else{
                    horas+=24;
                    textoDoHorario+=horas+" hora(s)";
                    textViewUltimo.setTextColor(context.getResources().getColor(R.color.black));
                }

            }else{
                textoDoHorario+=horas+" hora(s)";
                textViewUltimo.setTextColor(context.getResources().getColor(R.color.green));
            }

        }else{
                horas+=24;
                textoDoHorario+=horas+" hora(s)";
                textViewUltimo.setTextColor(context.getResources().getColor(R.color.black));


        }
        textViewUltimo.setText(textoDoHorario);
        //imageView.setImageResource(android.R.drawable.ic_notification_clear_all);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
