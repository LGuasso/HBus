package br.com.expressobits.hbus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe responsavel pela escrita e leitura de arquivo.
 * @author Romar Consultoria
 *
 */
public class ManageFile {
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
       
        
        try {
        	 File file = context.getFilesDir();
             File textfile = new File(file+"/"+fileName);
             Log.i(TAG,textfile.getAbsolutePath());	
             
             
             FileInputStream input = context.openFileInput(fileName);
             byte[] buffer = new byte[(int)textfile.length()];
             
             
			input.read(buffer);
			
			Log.i(TAG,new String(buffer));	
			return new String(buffer);
		} catch (Exception e) {
			// TODO Bloco catch gerado automaticamente
			Log.e(TAG, "ERRO "+e.getMessage());
			e.printStackTrace();
			return "ERRO ERRO";
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
            // Midia esta presente e está sendo feita a verificação
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia sendo verificada.");
        }
        else if (Environment.MEDIA_MOUNTED.equals(status)) {
            // A midia está presente e montada neste momento com
            // permissão de escrita e leitura
            sdCardAvailable = true;
            sdCardWritableReadable = true;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com permissão de escrita e leitura.");
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(status)) {
            // A midia está presente e montada neste momento com 
            // permissão somente de leitura
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com permissão somente leitura.");
        }
        else if (Environment.MEDIA_NOFS.equals(status)) {
            // A midia está presente, mas está vazia ou utilizando um
            // sistema de arquivos não suportado    
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia com sistema de arquivos não compatível.");
        }
        else if (Environment.MEDIA_REMOVED.equals(status)) {
            // A midia não está presente
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia não presente.");
        }
        else if (Environment.MEDIA_SHARED.equals(status)) {
            // A midia está presente, não montada e compartilhada 
            // via USB
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia compartilhada via USB.");
        }
        else if (Environment.MEDIA_UNMOUNTABLE.equals(status)) {
            // A midia está presente mas não pode ser montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia não pode ser montada");
        }
        else if (Environment.MEDIA_UNMOUNTED.equals(status)) {
            // A midia está presente mas não montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;
            Log.d(TAG, "Midia não montada.");
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