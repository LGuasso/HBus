package br.com.expressobits.hbus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe responsavel pela escrita e leitura de arquivo.
 * @author Romar Consultoria
 *
 */
class ManageFile {
    private static final String TAG = "ManageFile";
    private Context context;
    
    private boolean sdCardAvailable;
    private boolean sdCardWritableReadable;
    private boolean sdCardReadableOnly;
    
    public ManageFile(Context context){
        this.context = context;
    }

    /**
     * Escreve no arquivo texto.
     * @param text Texto a ser escrito.
     * @return True se o texto foi escrito com sucesso.
     */
    public boolean WriteFile(String text,String fileName,String dir){
        try {
            
            //Deleta arquivo
            /**File file = context.getExternalFilesDir(type)();
            File textfile = new File(file + "/"+fileName);
            
            textfile.delete();
            */
            // Abre o arquivo para escrita ou cria se nao existir
            File file = new File(context.getExternalFilesDir(null),
                    fileName);
            file.delete();
                FileOutputStream out = new FileOutputStream(file, true);
            
            
            out.write(text.getBytes());
            out.write("\n".getBytes());
            out.flush();
            out.close();    
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    
    
    /**
     * Faz a leitura do arquivo
     * @return O texto lido.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String ReadFile(String fileName){


            // Stringbuilder que sera utilizado no processamento
            StringBuilder sb = new StringBuilder();



            // Adiciona o nome do Arquivo
            sb.append(fileName);

            try
            {

                // Obtem o contexto definido globalmente e abre o arquivo do Assets
                InputStream is = context.getAssets().open(sb.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String s = null;

                // Instancia do stringbuilder que sera utilizada para leitura do arquivo
                sb = new StringBuilder();

                while ((s = br.readLine()) != null)
                    sb.append(s + "\r\n");

                br.close();
                is.close();
                return sb.toString();

		} catch (Exception e) {
			// TODO Bloco catch gerado automaticamente
			Log.e(TAG, "ERRO "+e.getMessage());
			e.printStackTrace();
			return null;
		}            
        
        
    }
    
 public void getStateSDcard(){
        
        // Obtem o status do cartao SD
        String status = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_BAD_REMOVAL.equals(status)) {
            // Midia foi removida antes de ser montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia removida.");
        }
        else if (Environment.MEDIA_CHECKING.equals(status)) {
            // Midia esta presente e est� sendo feita a verifica��o
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia sendo verificada.");
        }
        else if (Environment.MEDIA_MOUNTED.equals(status)) {
            // A midia est� presente e montada neste momento com
            // permiss�o de escrita e leitura
            sdCardAvailable = true;
            sdCardWritableReadable = true;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com permiss�o de escrita e leitura.");
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
            // A midia est� presente e montada neste momento com 
            // permiss�o somente de leitura
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com permiss�o somente leitura.");
        }
        else if (Environment.MEDIA_NOFS.equals(status)) {
            // A midia est� presente, mas est� vazia ou utilizando um
            // sistema de arquivos n�o suportado    
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com sistema de arquivos n�o compat�vel.");
        }
        else if (Environment.MEDIA_REMOVED.equals(status)) {
            // A midia n�o est� presente
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia n�o presente.");
        }
        else if (Environment.MEDIA_SHARED.equals(status)) {
            // A midia est� presente, n�o montada e compartilhada 
            // via USB
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia compartilhada via USB.");
        }
        else if (Environment.MEDIA_UNMOUNTABLE.equals(status)) {
            // A midia est� presente mas n�o pode ser montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia n�o pode ser montada");
        }
        else if (Environment.MEDIA_UNMOUNTED.equals(status)) {
            // A midia est� presente mas n�o montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia n�o montada.");
        }
    }

    public boolean isSdCardAvailable() {
        return sdCardAvailable;
    }

    public void setSdCardAvailable(boolean sdCardAvailable) {
        this.sdCardAvailable = sdCardAvailable;
    }

    public boolean isSdCardWritableReadable() {
        return sdCardWritableReadable;
    }

    public void setSdCardWritableReadable(boolean sdCardWritableReadable) {
        this.sdCardWritableReadable = sdCardWritableReadable;
    }

    public boolean isSdCardReadableOnly() {
        return sdCardReadableOnly;
    }

    public void setSdCardReadableOnly(boolean sdCardReadableOnly) {
        this.sdCardReadableOnly = sdCardReadableOnly;
    }

}