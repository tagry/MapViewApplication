/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapprinter;

import com.sun.prism.image.Coords;
import java.awt.Point;
import java.io.File;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mapprinter.data.Matrice;

public class MapPrinter extends Application {

    private static final int APP_W = 1012;
    private static final int APP_H = 600;
    private static final String DATA_PATH = ".";
    public static final int SIZE_BLOCK_SIDE = 1024;
    
    private Point currentIndex = new Point(0, 0);
    private int currentZoom = 1;
    
    private List<File> fileLoaded = new ArrayList<>();
    private Map<String,Matrice> matricesFiles = new HashMap<>();
    
    private Scene scene;

    private Parent createContent() {
        Image image = null;
        byte[] imageData = imageToData(image);

        byte[] modifiedImageData = modify(imageData);
        Image modifiedImage = dataToImage(modifiedImageData);

        HBox root = new HBox(25);
        root.getChildren().addAll(new ImageView(image), new ImageView(modifiedImage));

        return root;
    }
    
    private void updateFile(){
        int x_min = currentIndex.x /SIZE_BLOCK_SIDE ;
        int x_max = (currentIndex.x + (int)scene.getWidth()) / SIZE_BLOCK_SIDE;
        
        int y_min = currentIndex.y / SIZE_BLOCK_SIDE;
        int y_max = (currentIndex.y + (int)scene.getHeight()) / SIZE_BLOCK_SIDE;
        
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

    private byte[] imageToData(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        byte[] data = new byte[width * height * 4];

        int i = 0;

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int argb = image.getPixelReader().getArgb(x, y);

                byte[] pixelData = ByteBuffer.allocate(4).putInt(argb).array();

                data[i++] = pixelData[0];
                data[i++] = pixelData[1];
                data[i++] = pixelData[2];
                data[i++] = pixelData[3];
            }
        }

        return data;
    }

    private Image dataToImage(byte[] data) {
        // if we don't know the image size beforehand we can encode width and height
        // into image data too
        WritableImage image = new WritableImage(APP_W / 2, APP_H);
        PixelWriter writer = image.getPixelWriter();

        int i = 0;
        for (int y = 0; y < APP_H; y++) {
            for (int x = 0; x < APP_W / 2; x++) {
                int argb = ByteBuffer.wrap(Arrays.copyOfRange(data, i, i + 4)).getInt();

                writer.setArgb(x, y, argb);

                i += 4;
            }
        }

        return image;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
