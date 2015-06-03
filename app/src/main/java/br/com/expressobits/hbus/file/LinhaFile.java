package br.com.expressobits.hbus.file;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import br.com.expressobits.hbus.modelo.Codigo;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * Created by Rafael on 20/05/2015.
 * @since 02 de junho de 2015
 */
public class LinhaFile {

    public static LinhaFile instance;
    public static String TAG = "LinhaFile";
    public static final String LINHAFILE = "linhas";
    public static final String CODIGOFILE = "codigos";

    private static ArrayList<Linha> linhas = new ArrayList<>();
    //private static ArrayList<Codigo> codigos = new ArrayList<>();
    private static HashMap<String,Codigo> codigos = new HashMap<>();
    private ArrayList<Onibus> onibuses = new ArrayList<>();
    private static Context context;

    public LinhaFile(Context context){
        this.context = context;
    }

    public void iniciarDados(){
        iniciarLinhas();
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
        String[] fileStringarray=fileString.split("\n");
        Toast.makeText(context,nameFile,Toast.LENGTH_LONG).show();
        Log.d(TAG, "Read file " + nameFile);
        return new ArrayList<>(Arrays.asList(fileStringarray));
    }

    /**
     * Complementa o ArrayList com as linhas do arquivo
     * @since 02 de junho de 2015
     */
    private void iniciarLinhas() {
        linhas.clear();
        ArrayList<String> texto = lerTexto(LINHAFILE);
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Linha linha = new Linha();
                linha.setNome(txt.split(" - ")[0]);
                linha.setTipos(new ArrayList<>(Arrays.asList(txt.split(" - ")[1].split(":"))));
                linhas.add(linha);
            }
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
                codigos.put(codigo.getId(),codigo);

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
        onibuses.clear();
        iniciarCodigos();
        if(onibuses.isEmpty()){
            ArrayList<String> texto = lerTexto((nome + "_" + sentido + "_" + dias));

            if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
                for (String txt : texto) {
                    Onibus onibus = new Onibus();
                    onibus.setHorario(txt.split(" - ")[0]);
                    onibus.setCodigo(getCodigoId(txt.split(" - ")[1]));
                    onibuses.add(onibus);
                }
            }

        }else{
            throw new RuntimeException("ERRO onibuses not empty");
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
            //TODO implementar texto dinamico
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
    public static ArrayList<Linha> getLinhas(){
        return linhas;
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
    public ArrayList<Onibus> getOnibuses(String nome,String sentido,TipoDeDia dias){
        iniciarOnibuses(nome,sentido,dias);
        return onibuses;
    }

}
