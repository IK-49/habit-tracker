import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.util.ArrayList;
import java.time.*;

public class HabitTracker extends Application {

    @Override
    public void start(Stage stage) {
        
        // Program creates a file called habits.txt to store all user habits
        try {
          File myObj = new File("habits.txt");
          if (myObj.createNewFile()) {} else {
              System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Read all the habits that are in the file
        ArrayList<String> habits = loadHabitsFromFile("habits.txt");

        // GridPane will be used to display each habit
        GridPane habitGrid = new GridPane();
        habitGrid.setVgap(10);
        habitGrid.setHgap(20);

        // Iterates through each habit
        for (int i = 0; i < habits.size(); i++) {
            String habit = habits.get(i);

            CheckBox checkBox = new CheckBox(habit);

            Label streak = new Label("Streak: (streak goes here)");

            habitGrid.add(checkBox, 0, i);
            habitGrid.add(streak, 1, i);
        }

        // Habit entry field
        TextField newHabitField = new TextField();
        newHabitField.setPromptText("Enter a new habit");

        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setOnAction(event -> {
            String newHabit = newHabitField.getText().trim();
            if (!newHabit.isEmpty()) {
                habits.add(newHabit);
                writeHabitsToFile("habits.txt", habits); // Habit name written

                // Gets the next row and assigns the new habit to it / displays on PaneGrid
                int newRow = habits.size() - 1;
                CheckBox newCheckBox = new CheckBox(newHabit);
                Label streak = new Label("Streak: ");
                habitGrid.add(newCheckBox, 0, newRow);
                habitGrid.add(streak, 1, newRow);

                newHabitField.clear();
            }
        });

        HBox inputArea = new HBox(10, newHabitField, addHabitButton);

        layout.getChildren().addAll(habitGrid, inputArea);

        Scene scene = new Scene(layout, 300, 400);
        stage.setTitle("Habit Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private ArrayList<String> loadHabitsFromFile(String fileName) 
    {
        ArrayList<String> habits = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                habits.add(line.trim()); // Adds each individual line in habits.txt to the habits ArrayList
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        return habits;
    }

    private void writeHabitsToFile(String fileName, ArrayList<String> habits) {
        LocalDate creationDate = LocalDate.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String habit : habits) // Write the habit from the habit text field to the file
            {
                writer.write(habit);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
