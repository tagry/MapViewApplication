/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapprinter;

import java.awt.Point;
import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import mapprinter.data.Matrice;

public class MapPrinter extends Application {

    private static int appW = 1012;
    private static int appH = 600;
    public static final String DATA_PATH = "./";
    public static final int SIZE_BLOCK_SIDE = 1024;
    
    private Point currentIndex = new Point(0, 0);
    private int currentZoom = 2;
    
    private List<File> fileLoaded = new ArrayList<>();
    private Map<String,Matrice> matricesFiles = new HashMap<>();
    
    private Scene scene;
    private Pane root;
    private  ImageView imageView;
    
    private int mouseClickedX = -1;
    private int mouseClickedY = -1;
    
    private int maxZoom = 3;

    private Parent createContent() {
        Image image = null;
        updateFile();
        Image modifiedImage = dataToImage();
        imageView = new ImageView(modifiedImage);
        
        root = new Pane(imageView);
        
        imageView.setOnMousePressed(value->{
            mouseClickedX = (int)value.getX();
            mouseClickedY = (int)value.getY();
        });
        
        imageView.setOnMouseDragged(value->{
            int newX = currentIndex.x + (mouseClickedX - (int)value.getX());
            int newY = currentIndex.y + (mouseClickedY-(int)value.getY());
            
            mouseClickedX = (int)value.getX();
            mouseClickedY = (int)value.getY();
            
            if(checkDragOk(newX, newY)){
                currentIndex.x = newX;
                currentIndex.y = newY;
                Image modifImage = dataToImage();
            
                imageView.setImage(modifImage);
            }
        });
        
        
        imageView.setOnMouseReleased(value->{
            mouseClickedX = -1;
            mouseClickedY = -1;
            updateFile();
            Image modifImage = dataToImage();
            imageView.setImage(modifImage);
           });
        
        imageView.setOnScroll(value->{
            if((int)value.getDeltaY()< 0 && currentZoom > 1)
                currentZoom--;
            else if((int)value.getDeltaY()> 0 && currentZoom < maxZoom)
                currentZoom++;
            
            if(!checkDragOk(currentIndex.x, currentIndex.y)){
                currentIndex.x = 0;
                currentIndex.y = 0;
            }
            
            updateFile();
            
            Image modifImage = dataToImage();
            imageView.setImage(modifImage);
        });
            
        return root;
    }
    
    public boolean checkDragOk(int x, int y){
        return (x + SIZE_BLOCK_SIDE) < currentZoom * SIZE_BLOCK_SIDE &&
               (y + SIZE_BLOCK_SIDE) < currentZoom * SIZE_BLOCK_SIDE &&
                x > 0 &&
                y > 0;
    }
    
    private void updateFile(){
        int x_min = currentIndex.x /SIZE_BLOCK_SIDE ;
        int x_max = (currentIndex.x + appW) / SIZE_BLOCK_SIDE;
        
        int y_min = currentIndex.y / SIZE_BLOCK_SIDE;
        int y_max = (currentIndex.y + appH) / SIZE_BLOCK_SIDE;
        
        List<File> removeList = new ArrayList<>();
        
        fileLoaded.forEach(f->{int indexX = Integer.parseInt(String.valueOf(f.getName().toCharArray()[0]));
        int indexY = Integer.parseInt(String.valueOf(f.getName().toCharArray()[2]));
            
        if( indexX < x_min 
                    || indexX > x_max  
                    || indexY < y_min
                    || indexY > y_max)
                
            removeList.add(f); 
        matricesFiles.remove(indexX+","+indexY);
        });
        
        fileLoaded.removeAll(removeList);
        
        for(int j = x_min; j <= x_max; j++){
            for(int i = y_min; i <= y_max ; i++){
                if(!matricesFiles.containsKey(j+","+i)){
                    File f = new File(DATA_PATH+j+"-"+i+"-"+currentZoom);
                    fileLoaded.add(f);
                    matricesFiles.put(j + "," + i, new Matrice(f));
                }
            }
        }
    }

    /**
     * Modifies the pixel data.
     *
     * @param data original image data
     * @return modified image data
     */
    private byte[] modify(byte[] data) {
        
        // this is where changes happen
        return data;
    }


    private Image dataToImage() {
        // if we don't know the image size beforehand we can encode width and height
        // into image data too
        WritableImage image = new WritableImage(appW, appH);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < appH; y++) {
            for (int x = 0; x < appW; x++) {
                int fileIndexX = (currentIndex.x + x)/SIZE_BLOCK_SIDE;
                int fileIndexY = (currentIndex.y + y)/SIZE_BLOCK_SIDE;
                
                int indexInFileX = (currentIndex.x + x) - fileIndexX*SIZE_BLOCK_SIDE;
                int indexInFileY = (currentIndex.y + y) - fileIndexY*SIZE_BLOCK_SIDE;
                
                if(matricesFiles.containsKey(fileIndexX + "," + fileIndexY)){
                    int argb = matricesFiles.get(fileIndexX + "," + fileIndexY).getHigh(indexInFileX, indexInFileY);
               
                    writer.setArgb(x, y, argb);
         
                    writer.setColor(x, y, new Color((float)argb/256, 0.1, 0.1, 1));
                }
                else
                    writer.setColor(x, y, new Color(1, 1, 1, 1));
            }
        }

        return image;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        this.scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Group Group() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
