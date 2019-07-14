package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TxtFileReader {
    private String fileName;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public TxtFileReader(String fileName){
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> read() {
        ArrayList<String> lines = new ArrayList<String>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}
