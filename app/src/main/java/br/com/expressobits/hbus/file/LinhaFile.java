package br.com.expressobits.hbus.file;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Codigo;
import br.com.expressobits.hbus.modelo.Itinerario;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.Bus;
import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * Created by Rafael on 20/05/2015.
 * @since 02 de junho de 2015
 */
public class LinhaFile {

    public static String TAG = "LinhaFile";
    public static final String ITINERARIOFILE = "itinerarios";
    public static final String ITINERARIOFILETODOS = "aaa_itinerarios";
    public static final String CODIGOFILE = "codigos";
    private static HashMap<String,Codigo> codigos = new HashMap<>();
    private static ArrayList<Itinerario> itinerarios = new ArrayList<>();
    private ArrayList<Bus> onibuses = new ArrayList<>();
    private static Context context;


    public LinhaFile(Context context){
        this.context = context;
    }


    public void iniciarDados(){
        iniciarItinerariosFavoritos();
        iniciarCodigos();
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



    /**
     * Complementa o ArrayList com as linhas do arquivo
     * @since 02 de junho de 2015
     */
    private void iniciarItinerariosFavoritos() {
        itinerarios.clear();
        ArrayList<String> texto = lerTexto(ITINERARIOFILE);
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Itinerario itinerario = new Itinerario();
                itinerario.setNome(txt.split(" - ")[0]);
                itinerario.setCodigos(new ArrayList<>(Arrays.asList(txt.split(" - ")[1].split(":"))));
                itinerario.setSentidos(getSentidos(context,itinerario.getNome()));


                ArrayList<Linha> linhas = new ArrayList<>();

                for(int i=0;i<itinerario.getSentidos().size();i++){

                    linhas.add(createLinha(itinerario.getNome(), itinerario.getSentidos().get(i),TipoDeDia.UTEIS));
                    linhas.add(createLinha(itinerario.getNome(), itinerario.getSentidos().get(i), TipoDeDia.SABADO));
                    linhas.add(createLinha(itinerario.getNome(), itinerario.getSentidos().get(i),TipoDeDia.DOMINGO));

                }
                itinerario.setLinhas(linhas);
                itinerarios.add(itinerario);


            }
        }
        for (Itinerario itinerario1:itinerarios){
            Log.e("TESTE05","itinerario "+itinerario1.getNome());
            for (Linha linha1 : itinerario1.getLinhas() ){
                Log.e("TESTE05"," - linha "+linha1.getOnibuses().size());
            }
        }
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
     * Complementa o ArrayList com as códigos do arquivo
     * @since 02 de junho de 2015
     */
    public void iniciarCodigos() {
        codigos.clear();
        ArrayList<String> texto = lerTexto(CODIGOFILE);
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Codigo codigo = new Codigo();
                codigo.setId(txt.split(":")[0].toUpperCase().trim());
                codigo.setDescricao(txt.split(":")[1]);
                codigos.put(codigo.getId(), codigo);

            }
        }
    }

    /**
     * Complementa o ArrayList com os horários do arquivo
     * @param nome Nome da linha
     *             @see Linha
     * @param sentido Sentido da linha <i>Ex.: Centro > Bairro</i>
     *                @see Linha
     * @param dias Tipo do dia (Úteis, Sábado ou Domingo)
     *             @see TipoDeDia
     */
    private void iniciarOnibuses(String nome,String sentido,TipoDeDia dias){
        onibuses = new ArrayList<Bus>();
        iniciarCodigos();
        if(onibuses.isEmpty()){
            ArrayList<String> texto = lerTexto((toSimpleName(nome) + "_" + toSimpleName(sentido) + "_" + dias));
            //TODO implementar um relátorio para os arquivos que leiam e não existam horários!
            if(texto.size()>1){
                if(!texto.get(0).equals("ERRO")) {
                    for (String txt : texto) {
                        Bus bus = new Bus();
                        bus.setTime(txt.split(" - ")[0]);
                        bus.setCodigo(getCodigoId(txt.split(" - ")[1]));
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
     *           @see Codigo
     * @return Código de ônibus.
     */
    public static Codigo getCodigoId(String id){
        Codigo codigo;
        if(codigos.containsKey(id.trim())){
            codigo = codigos.get(id.trim());
        }else{
            codigo = new Codigo();
            //TODO implementar texto dinamico do res
            codigo.setId("Falta ID");
            codigo.setDescricao("Não há descrição");
        }

        return codigo;
    }


    /**
     * Lista de linhas disponíveis no programa
     * @see Linha
     * @return Lista de linhas.
     */
    public static ArrayList<Itinerario> getItinerarios(){
        return itinerarios;
    }

    public static HashMap<String,Codigo> getCodigos(){
        return codigos;
    }

    /**
     * Retorna os lista de ônibus da linha definida.
     * <p>Este método também inicia a lista interna para cada definição.</p>
     * @param nome Nome da linha.
     * @param sentido Sentido da linha.
     * @param dias Tipo de dia da semana (Ex.: Úteis)
     *             @see TipoDeDia
     * @return Lista com ônibus da linha definida.
     */
    public ArrayList<Bus> getOnibuses(String nome,String sentido,TipoDeDia dias){
        iniciarOnibuses(nome,sentido,dias);
        return onibuses;
    }

    /**
     * Retorna nome sem espaço sem acentos e sem maiusculas.
     * @param name
     * @return
     */
    private String toSimpleName(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" > ","-");
            ok = ok.replace(" < ","-");
            ok = ok.replace(" ", "-");
            ok = ok.replace("é", "e");
            ok = ok.toLowerCase();
            return ok;
        }
    }

    public Linha createLinha(String nome,String sentido,TipoDeDia tipodeDia){
        Linha linha = new Linha();
        ArrayList<Bus> buses = new ArrayList<>();
        buses = getOnibuses(nome,sentido,tipodeDia);
        linha.setOnibuses(buses);
        return linha;
    }

    public List<String> getNomeLinhas(){
        ArrayList<String> linhas = new ArrayList<>();

        ArrayList<String> linhasAux = lerTexto(ITINERARIOFILETODOS);

        for (String txt :linhasAux){
            if(txt.contains(" - ")) {
               linhas.add(txt.split(" - ")[0]);
            }
        }
        return linhas;
    }

}
