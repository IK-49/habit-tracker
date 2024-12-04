

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Write a description of JavaFX class VisualPage here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class VisualPage extends Application
{
    // We keep track of the count, and label displaying the count:
    
    ArrayList<Rectangle> visuals = new ArrayList<>(); //Stores rectangles
    Label debug1 = new Label();
    Label debug2 = new Label();
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        // Create a Button or any control item
        Button test = new Button();
        
        
        // Create a new grid pane
        GridPane habitCalendar = new GridPane();
        habitCalendar.setPadding(new Insets(10, 10, 10, 10));
        habitCalendar.setMinSize(300, 300);
        habitCalendar.setVgap(10);
        habitCalendar.setHgap(10);

        //Create file object
        File habits = new File("record.txt");
        
        //Read from file, add to visuals ArrayList, set color for each rectangle
        fileRead(habits, habitCalendar);
        
        //Add rectangles to scene
        for(int i = 0; i < visuals.size(); i++){
            habitCalendar.add(visuals.get(i), i, 0);
        }
        
        //Debug labels
        habitCalendar.add(debug1, 0, 1);
        habitCalendar.add(debug2, 0, 2);
        
        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(habitCalendar, 1000,500);
        stage.setTitle("Visual Test");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
    
      public boolean fileRead(File file, GridPane pane) {
        try {
            // If file exists, continue
            if (!file.exists()) {
                System.out.println("File not found.");
                return false;
            }
            
            //Create scanner
            Scanner reader = new Scanner(file);
            
            // [---------------------------
            //String will store file content
            String content = "";
            
            //Add to content
            while(reader.hasNext()){
                content += reader.next();
            }
            // ----------------------------]
            //This doesn't make a ton of sense, essentially storing the entire file in memory.
            //It will need to be optimized. For now, though, it works.
            
            debug1.setText("File Content: \n" + content);
            
            //Create array of tokens
            String[] tokens = content.split("&");
            
            // Change each Rectangle as needed
            for(int i = 0; i < tokens.length; i++){
                visuals.add(new Rectangle(80, 80, Color.RED)); //Assume incomplete
                if (Integer.valueOf(tokens[i]) > 0) { //Conditional for testing
                    visuals.get(i).setFill(Color.GREEN);
                }
                System.out.println(i); //Debug
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return false;
    }

}
