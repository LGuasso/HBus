package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Linha;

/**
 * Created by Rafael on 27/05/2015.
 */
public class ListViewAdapterLines extends ArrayAdapter<Linha> {

    Context context;
    LayoutInflater layoutInflater;
    List<Linha> lineList;

    public ListViewAdapterLines(Context context, int id, List<Linha> lista){
        super(context,id,lista);
        this.context = context;
        //this.lista = lista;
        this.lineList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Linha linha = lineList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_line, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listView_background_line);
        if(position%2==0){
            //layout.setBackgroundColor(context.getResources().getColor(R.color.orange_ultralight));
        }
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview_line);
        TextView textViewTipo = (TextView) view.findViewById(R.id.item_list_textview_line_type);
        textView.setText(linha.getNome());
        textViewTipo.setText(linha.getTipos().toString());
        return view;
    }
}
