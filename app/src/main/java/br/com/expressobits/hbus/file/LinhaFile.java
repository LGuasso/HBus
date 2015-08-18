package br.com.expressobits.hbus.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.Line;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.MainActivity;

/**
 * Created by Rafael on 20/05/2015.
 * @since 02 de junho de 2015
 */
public class LinhaFile {

    public static final String KEY_SQLINIT = "sqlinit";
    public static String TAG = "LinhaFile";
    public static final String ITINERARIOFILE = "itinerarios";
    public static final String ITINERARIOFILETODOS = "aaa_itinerarios";
    public static final String CODIGOFILE = "codigos";
    private static HashMap<String,Code> codigos = new HashMap<>();
    //private static ArrayList<Itinerary> Itinerary = new ArrayList<>();
    private ArrayList<Bus> onibuses = new ArrayList<>();
    private static Context context;


    public LinhaFile(Context context){
        this.context = context;
    }


    /**
     * Inicializa todas linhas e dados do banco.
     * TODO implementar progressBar
     */
    public void init(MainActivity main){

        if(context.getSharedPreferences(TAG,context.MODE_PRIVATE).getBoolean(KEY_SQLINIT,true)){

            BusDAO dao = new BusDAO(context);
            dao.deleteAll();
            initItinerary();
            main.setLoadText("Load itinerary index");
            initCodes();
            for(Itinerary itinerary : dao.getItineraries()){
                for (String sentido:itinerary.getSentidos()){
                    for (TypeDay typeDay:TypeDay.values()){
                        initBusLine(itinerary.getName(), sentido, typeDay);

                    }
                }
                main.setLoadText(itinerary.getName());

            }
            SharedPreferences sharedPref = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(KEY_SQLINIT, false);
            editor.commit();
        }




    }

    /**
     * Ler lista de texto baseado.
     * Exibe um Toast e um Log debug com nome do arquivo lido.
     * @param nome nome do arquivo sem a extensão ,pois ele automaticamente adiciona a
     *             extensão <i>.dat</i>
     * @return Lista com <b>String</b> do arquivo separados por linhas.
     * @since 02 de junho de 2015
     */
    private ArrayList<String> lerTexto(String nome){
        ManageFile file = new ManageFile(context);
        String nameFile = nome+".dat";
        String fileString = file.ReadFile(nameFile);
        String[] fileStringarray;
        if(fileString!=null){
            fileStringarray=fileString.split("\n");
            if(BuildConfig.DEBUG){
                Toast.makeText(context,nameFile,Toast.LENGTH_LONG).show();
            }
        }else{
            fileStringarray=new String[]{"ERRO","ERRO"};
            Log.e(TAG, "File empty" + nameFile);
        }

        Log.d(TAG, "Read file " + nameFile);
        return new ArrayList<>(Arrays.asList(fileStringarray));
    }



    public List<String> getSentidos(Context ctx,String name){
            switch (name) {
                case "Big Rodoviaria":
                    return (Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_big_rodoviaria)));
                case "Seletivo UFSM Bombeiros":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_ufsm_bombeiros)));
                case "T Neves Campus":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_tneves_campus)));
                case "Boi Morto":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_boi_morto)));
                case "Carolina Sao Jose":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_carolina_sao_jose)));
                case "Itarare Brigada":
                case "Circular Cemiterio Sul":
                case "Circular Cemiterio Norte":
                case "Circular Camobi":
                case "Circular Barao":
                case "Brigada Itarare":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_circular)));
                case "UFSM Circular":
                case "UFSM":
                case "Seletivo UFSM":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_ufsm_centro)));
                case "UFSM Bombeiros":
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido_ufsm_bombeiros)));
                default:
                    return(Arrays.asList(context.getResources().getStringArray(R.array.list_sentido)));
            }

    }

    /**
     * Inicializa os itinerários.
     */
    public void initItinerary(){
        BusDAO dao = new BusDAO(context);
        ArrayList<String> texto = lerTexto(ITINERARIOFILETODOS);
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Itinerary itinerary = new Itinerary();
                itinerary.setName(txt.split(";")[0].trim());
                itinerary.setFavorite(false);
                itinerary.setSentidos(new ArrayList<String>(Arrays.asList(txt.split(";")[1].split(","))));
                dao.insert(itinerary);
            }
        }
        dao.close();
    }

    /**
     * Complementa o ArrayList com as códigos do arquivo
     * @since 02 de junho de 2015
     */
    public void initCodes() {
        //codigos.clear();
        BusDAO dao = new BusDAO(context);
        ArrayList<String> texto = lerTexto(CODIGOFILE);
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Code codigo = new Code();
                codigo.setName(txt.split(":")[0].toUpperCase().trim());
                codigo.setDescrition(txt.split(":")[1]);
                dao.insert(codigo);
            }
        }
        dao.close();
    }

    public void initBusLine(String itineraryName,String way,TypeDay typeDay){
        BusDAO dao = new BusDAO(context);
        ArrayList<String> texto = lerTexto((toSimpleName(itineraryName) + "_" + toSimpleName(way) + "_" + typeDay));
        if(texto.size()>1){
            if(!texto.get(0).equals("ERRO")) {
                for (String txt : texto) {
                    Bus bus = new Bus();
                    bus.setTime(txt.split(" - ")[0]);
                    Code code = new Code();
                    String scode = txt.split(" - ")[1];
                    scode = scode.replaceAll("\r", "");
                    scode = scode.replaceAll("\t", "");
                    scode = scode.replaceAll("\n", "");
                    code.setName(scode);
                    //Log.d(TAG,"identify CODE "+txt.split(" - ")[1]+" RESult "+code);
                    bus.setCode(code);
                    bus.setWay(way);
                    Itinerary itinerary = dao.getItinerary(itineraryName);
                    bus.setItinerary(itinerary);
                    bus.setTypeday(typeDay);
                    dao.insert(bus);
                }
            }
        }

    }

    /**
     * Complementa o ArrayList com os horários do arquivo
     * @param nome Nome da linha
     *             @see Line
     * @param sentido Sentido da linha <i>Ex.: Centro > Bairro</i>
     *                @see Line
     * @param dias Tipo do dia (Úteis, Sábado ou Domingo)
     *             @see TypeDay
     *
     *             @deprecated
     *
     */
    private void iniciarOnibuses(String nome,String sentido,TypeDay dias){
        onibuses = new ArrayList<Bus>();
        initCodes();
        if(onibuses.isEmpty()){
            ArrayList<String> texto = lerTexto((toSimpleName(nome) + "_" + toSimpleName(sentido) + "_" + dias));
            //TODO implementar um relátorio para os arquivos que leiam e não existam horários!
            if(texto.size()>1){
                if(!texto.get(0).equals("ERRO")) {
                    for (String txt : texto) {
                        Bus bus = new Bus();
                        bus.setTime(txt.split(" - ")[0]);
                        bus.setCode(getCodigoId(txt.split(" - ")[1]));
                        onibuses.add(bus);
                    }
                }
            }


        }else{
            //throw new RuntimeException("ERRO onibuses not empty");
        }

    }

    /**
     * Retorna o código baseado na procura pelo seu id.
     * @param id String com identificação do código de ônibus
     *           @see Code
     * @return Código de ônibus.
     */
    public static Code getCodigoId(String id){
        Code codigo;
        if(codigos.containsKey(id.trim())){
            codigo = codigos.get(id.trim());
        }else{
            codigo = new Code();
            //TODO implementar texto dinamico do res
            codigo.setName("Falta ID");
            codigo.setDescrition("Não há descrição");
        }

        return codigo;
    }



    public static HashMap<String,Code> getCodigos(){
        return codigos;
    }

    /**
     * Retorna os lista de ônibus da linha definida.
     * <p>Este método também inicia a lista interna para cada definição.</p>
     * @param nome Nome da linha.
     * @param sentido Sentido da linha.
     * @param dias Tipo de dia da semana (Ex.: Úteis)
     *             @see TypeDay
     * @return Lista com ônibus da linha definida.
     */
    public ArrayList<Bus> getOnibuses(String nome,String sentido,TypeDay dias){
        iniciarOnibuses(nome,sentido,dias);
        return onibuses;
    }

    /**
     * Retorna nome sem espaço sem acentos e sem maiusculas.
     * @param name
     * @return
     */
    public static String toSimpleName(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" > ","-");
            ok = ok.replace(" < ","-");
            ok = ok.replace(" ", "-");
            ok = ok.replace("é", "e");
            ok = ok.replace(",", "_");
            ok = ok.toLowerCase();
            return ok;
        }
    }



}
