package br.com.expressobits.hbus.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Correa
 * @since 15/03/17
 */

public class FileManager {

    public static void createPath(String fileName){
        Path pathToFile = Paths.get(fileName);
        try {
            Files.createDirectories(pathToFile.getParent());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR CREATE PATH");
        }
    }


    public static void write(boolean debug,String line,String fileName){
        createPath(fileName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName,true));
            writer.write(line+"\n");
            if(debug){
                System.out.println("write in "+fileName+"->"+line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> readFile(boolean debug,String fileName){

        List<String> text = new ArrayList<String>();
        BufferedReader br = null;

        try {
            String mLine;

            br = new BufferedReader(new FileReader(fileName));
            while ((mLine = br.readLine()) != null) {
                text.add(mLine);
                if(debug){
                    System.out.println(mLine);
                }

            }
        } catch (IOException e) {
            System.err.println("ERRO AO LER ARQUIVO!");
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return text;
    }
}
