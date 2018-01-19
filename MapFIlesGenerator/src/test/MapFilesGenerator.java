package test;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tagry
 */
public class MapFilesGenerator {
    public static final String DATA_PATH = "./";
    public static final int SIZE_BLOCK_SIDE = 1024;
    
    public static void main(String[] args) {
        createFile("0-0-1");
        
        createFile("0-0-2");
        createFile("1-0-2");
        createFile("1-1-2");
        createFile("0-1-2");
        
        createFile("0-0-3");
        createFile("0-1-3");
        createFile("0-2-3");
        createFile("1-0-3");
        createFile("1-1-3");
        createFile("1-2-3");
        createFile("2-0-3");
        createFile("2-0-3");
        createFile("2-1-3");
        createFile("2-2-3");
    }
    
    public static void createFile(String name){
        try {
            File file = new File(DATA_PATH + name);
            file.createNewFile();
            
            FileWriter writer = new FileWriter(file);
            int randomNum = 0;
            for(int i = 0 ; i < SIZE_BLOCK_SIDE ; i++){
                for(int j = 0; j < SIZE_BLOCK_SIDE; j++){
                    if(j < SIZE_BLOCK_SIDE/2 )
                        randomNum = 0 + (int)(Math.random() * ((50 - 0) + 1));
                    else
                       randomNum = 150 + (int)(Math.random() * ((230 - 150) + 1));
                    
                    if(j == SIZE_BLOCK_SIDE -1){
                        if(i == SIZE_BLOCK_SIDE -1)
                            writer.write(randomNum+"");
                        else
                            writer.write(randomNum+ "\n");
                        
                    }
                    else
                        writer.write(randomNum + ",");
                }
            }
            
            writer.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(MapFilesGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
