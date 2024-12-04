

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;


/**
 * Write a description of JavaFX class HabitTracker here.
 *
 * @authors: Izad Khokhar and Christian Harrison
 * @version (a version number or a date)
 */
public class ListPage extends Application
{
    //
    // Ideally, the HabitTracker class is the master controller, and the two pane views (the habit listing and streak calendar) are separate
    //
    private int count = 0;
    private Label myLabel = new Label("0");
    Button habitCompleted = new Button("Complete!");
    Button habitFailed = new Button("Failed!");
        
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
        
        Label myHabit = new Label("Habit Habit Bahit");
        // Create a new grid pane
        GridPane habitList = new GridPane();
        habitList.setPadding(new Insets(10, 10, 10, 10));
        habitList.setMinSize(300, 300);
        habitList.setVgap(10);
        habitList.setHgap(10);

        //set an action on the button using method reference
        habitCompleted.setOnAction(this::complete);
        habitFailed.setOnAction(this::incomplete);
        
        // Add the button and label into the pane
        habitList.add(myHabit, 0, 1);
        habitList.add(myLabel, 0, 3);
        habitList.add(habitFailed, 0, 0);
        habitList.add(habitCompleted, 0, 2);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(habitList, 300,100);
        stage.setTitle("Habit Listing");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }

    /**
     * This will be executed when the button is clicked
     * It increments the count by 1
     */
    private void complete(ActionEvent event)
    {
        // Counts number of button clicks and shows the result on a label
        count = count + 1;
        myLabel.setText(Integer.toString(count));
        habitCompleted.setDisable(true);
        habitFailed.setDisable(true);
    }
    
    private void incomplete(ActionEvent event)
    {
        // Counts number of button clicks and shows the result on a label
        count = count - 1;
        myLabel.setText(Integer.toString(count));
        habitCompleted.setDisable(true);
        habitFailed.setDisable(true);
    }
}
