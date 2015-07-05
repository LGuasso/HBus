package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Bus;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 03/05/2015.
 */
public class ListViewAdapterBus extends ArrayAdapter<Bus>{

    //Contexto do aplicativo
    Context context;
    //Inflater
    LayoutInflater layoutInflater;
    //Lista dos ônibus
    List<Bus> busList;

    public ListViewAdapterBus(Context context, int id, List<Bus> lista){
        super(context,id,lista);
        this.context = context;
        //this.lista = lista;
        this.busList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bus bus = busList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.item_list_bus,null);

        //Iniciando o laoyout e views
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_list_bus_square);
        if(bus.isTomorrow()) {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
        }

        TextView textViewHorario = (TextView) view.findViewById(R.id.item_list_textview_horario);
        TextView textViewTempoFaltando = (TextView) view.findViewById(R.id.item_list_textview_tempo_faltando);
        TextView textViewDescricaoCodigo = (TextView) view.findViewById(R.id.item_list_textview_descricao_do_codigo);
        TextView textViewCodigo = (TextView) view.findViewById(R.id.item_list_textview_codigo);
        //ImageView imageView = (ImageView) view.findViewById(R.id.item_list_imageview);
        textViewHorario.setText(bus.getTime());
        textViewCodigo.setText(bus.getCodigo().getId());
        //textViewCodigo.setText(context.getString(R.string.code_of_bus) + " " + onibus.getCodigo());
        // TODO Arrumar codigos que contem caracteres diferentes
        // TODO Separar a descrição por diferents Strings para poder trabalhar
        textViewDescricaoCodigo.setText(bus.getCodigo().getDescricao());
        textViewDescricaoCodigo.setSelected(true);
        textViewTempoFaltando.setSelected(true);
        textViewTempoFaltando.setText(TimeUtils.getFaltaparaHorario(bus.getTime()));
        //imageView.setImageResource(android.R.drawable.ic_notification_clear_all);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
