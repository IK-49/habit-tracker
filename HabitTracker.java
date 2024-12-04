

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/**
 * Write a description of JavaFX class HabitTracker here.
 *
 * @authors: Izad Khokhar and Christian Harrison
 * @version (a version number or a date)
 */
public class HabitTracker extends Application
{
    // Label and Buttons moved here so the methods can interact without throwing errors.
    // private int count = 0;
    private Label fileText = new Label("0");
    private Label habits = new Label("Habit");
    // Button habitCompleted = new Button("Complete!");
    // Button habitFailed = new Button("Failed!");
    Button createHabitButton = new Button("Create Habit");
    TextField habitToAdd = new TextField();
    
    public static int numOfHabits = 0;
    
    // Streak Variables
    int streakCount = 0;
    
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
        // Habit Example
        
        
        //File 
        try {
          File myObj = new File("habits.txt");
          if (myObj.createNewFile()) {
              System.out.println("File Created.");
          } else {
              System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        
        // Create master pane
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);
        pane.setVgap(10);
        pane.setHgap(10);

        // Button Actions
        // habitCompleted.setOnAction(this::complete);
        // habitFailed.setOnAction(this::incomplete);
        // Add incompleted and completed, along with counter for debug
        // pane.add(myHabit, 1, 0);
        // pane.add(habitFailed, 0, 0);
        // pane.add(habitCompleted, 2, 0);
        pane.add(createHabitButton, 1, 0);
        createHabitButton.setOnAction(this::createHabit); // need to clear text field after clicking the button
        pane.add(habitToAdd, 1, 1);
        
        Scene scene = new Scene(pane, 480,720);
        stage.setTitle("Habit Tracker");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }

    /*
    private void complete(ActionEvent event)
    {
        // Increments count and disables buttons
        count = count + 1;
        myLabel.setText(Integer.toString(count));
        habitCompleted.setDisable(true);
        habitFailed.setDisable(true);
        writeTo("habits.txt", myLabel.getText());
    }
    
    private void incomplete(ActionEvent event)
    {
        // Decrements count and disables buttons
        count = count - 1;
        myLabel.setText(Integer.toString(count));
        habitCompleted.setDisable(true);
        habitFailed.setDisable(true);
        writeTo("habits.txt", myLabel.getText());
    }
    */
   
    private void createHabit(ActionEvent event)
    {
        LocalDate creationDate = LocalDate.now();
        writeTo("habits.txt", habitToAdd.getText() + " $" + creationDate + "$ \n");
        
        // myLabel.setText(Integer.toString(count));
    }
    
    private void writeTo(String file, String value){
        try {
          FileWriter myWriter = new FileWriter(file, true);
          myWriter.write(value);
          myWriter.close();
          System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
}
