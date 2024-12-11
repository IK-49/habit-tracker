import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.time.*;
import java.util.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Write a description of JavaFX class VisualPage here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class VisualPage extends Application
{
    // We keep track of the count, and label displaying the count:
    private final String FILE_NAME = "habits.txt";
    private final ArrayList<Rectangle> visuals = new ArrayList<>(); //Stores rectangles
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    
    public static void main(String args[]){
        launch(args);
    }
    
    @Override
    public void start(Stage stage)
    {
        // Create a Button or any control item
        Button back = new Button();
        
        
        // Create a new grid pane
        GridPane habitCalendar = new GridPane();
        ScrollPane scroller = new ScrollPane();
        scroller.setContent(habitCalendar);
        habitCalendar.setPadding(new Insets(10, 10, 10, 10));
        habitCalendar.setMinSize(300, 300);
        habitCalendar.setVgap(10);
        habitCalendar.setHgap(10);
        
        //Gather rectangles
        streaksVisual(habitCalendar); 
                        
        Scene scene = new Scene(scroller, 550,500);
        stage.setTitle("Visual Test");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
    
    public void streaksVisual(GridPane habitCalendar) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 0; //Stores row value
            int returnNumber = 0; //Multiplier for carriage returns
            Color fillColor = Color.GREEN;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int streak = Integer.parseInt(parts[1]);
                
                habitCalendar.add(new Label(parts[0]), 0, lineNumber); //Habit name
                if(streak > 0){
                    for(int i = 0; i < streak; i++){
                        //Determines Color
                        if((i+1)%7 == 0 && i >=6){
                            fillColor = Color.BLUE; //Every 7 days of keeping up is blue
                        } else {
                            fillColor = Color.GREEN;
                        }
                        
                        //Carriage Return
                        if((i+1) > 7 && (i+1)%7 == 1) {
                            lineNumber++; //Newline
                            returnNumber++; //Pull rectangles back to index 0 visually
                        } 
                        
                        habitCalendar.add(new Rectangle(40, 40, fillColor), i+1-(returnNumber*7), lineNumber);
                    }
                }
                lineNumber++; //New Habit line
                returnNumber = 0; //No returns yet for new habit
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
