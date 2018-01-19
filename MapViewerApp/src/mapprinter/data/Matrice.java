/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapprinter.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mapprinter.MapPrinter;


/**
 *
 * @author tagry
 */
public class Matrice {
    private int[][] matrice = new int[MapPrinter.SIZE_BLOCK_SIDE][MapPrinter.SIZE_BLOCK_SIDE];
    
    public Matrice(File f){
        boolean first = true;
        FileReader reader = null;
        try {
            reader = new FileReader(f);
            char charRead;
            int x = 0;
            int y = 0;
            String high = "";
            
            while((charRead = (char)reader.read()) != charRead-1){
                switch (charRead) {
                    case ',':
                        matrice[y][x] = Integer.parseInt(high);
                        x++;
                        break;
                    case '\n':
                        matrice[y][x] = Integer.parseInt(high);
                        y++;
                        x = 0;
                        break;
                    default:
                        high += charRead;
                        break;
                }                    
            }     
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Matrice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Matrice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int get(int x, int y){
        return matrice[y][x];
    }
}
