import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.util.ArrayList;
import java.time.*;
import java.time.temporal.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;

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
        ArrayList<String> dates = loadDatesFromFile("habits.txt");
        ArrayList<String> streaks = loadStreaksFromFile("habits.txt");
        
        ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
        
        
        
        // GridPane will be used to display each habit
        GridPane habitGrid = new GridPane();
        habitGrid.setVgap(10);
        habitGrid.setHgap(20);

        // Iterates through each habit
        for (int i = 0; i < habits.size(); i++) {
            String habit = habits.get(i);
            
            checkboxes.add(new CheckBox(habit));
            
            Label streak = new Label("Streak: "+streaks.get(i));

            habitGrid.add(checkboxes.get(i), 0, i);
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
                writeHabitsToFile("habits.txt", newHabit); // Habit name written

                // Gets the next row and assigns the new habit to it / displays on PaneGrid
                int newRow = habits.size() - 1;
                CheckBox newCheckBox = new CheckBox(newHabit);
                Label streak = new Label("Streak: ");
                habitGrid.add(newCheckBox, 0, newRow);
                habitGrid.add(streak, 1, newRow);

                newHabitField.clear();
            }
        });
        
        Button confirmChecked = new Button("Confirm?");
        addHabitButton.setOnAction(event -> {
            for(int i = 0; i < checkboxes.size(); i++){
                if(checkboxes.get(i).isSelected()){
                    updateStreak("habits.text", habits, streaks, dates, i, 1);
                }
            }
        });

        HBox inputArea = new HBox(10, newHabitField, addHabitButton);
        HBox confirm = new HBox(10, confirmChecked);

        layout.getChildren().addAll(habitGrid, inputArea, confirm);

        Scene scene = new Scene(layout, 500, 400);
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
                Scanner s = new Scanner(line.trim());
                s.useDelimiter("\"");
                habits.add(s.next()); // Adds each habit name in habits.txt to the habits ArrayList
                s.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        return habits;
    }
    
    private ArrayList<String> loadStreaksFromFile(String fileName) 
    {
        ArrayList<String> streaks = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Scanner s = new Scanner(line.trim());
                s.useDelimiter("\"");
                s.next(); s.next();
                streaks.add(s.next()); // Adds each habit name in habits.txt to the habits ArrayList
                s.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        return streaks;
    }

    private ArrayList<String> loadDatesFromFile(String fileName){
        ArrayList<String> dates = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Scanner s = new Scanner(line.trim());
                s.useDelimiter("\"");
                s.next();
                String date = s.next();
                
                s.close();
                dates.add(date);// Adds each individual streak in habits.txt to the streaks ArrayList
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        
        return dates;
    }
    
    private void writeHabitsToFile(String fileName, String habit) {
        LocalDate creationDate = LocalDate.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("\""+habit+"\""+creationDate+"\"");
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
    }
    
    private void updateStreak(String fileName, ArrayList<String> habits, ArrayList<String> streaks, ArrayList<String> dates, int lineNumber, int streakDelta){
        
        ArrayList<String> fileContent = new ArrayList<>();
        int j = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                j+=1;
                fileContent.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        
        String targetLine = "\""+habits.get(j)+"\""+dates.get(j)+"\""+streaks.get(j)+"\"";
        
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(targetLine)) {
                fileContent.set(i, "\""+habits.get(j)+"\""+dates.get(j)+"\""+streaks.set(j, streaks.get(j)+streakDelta));
            }
        }

        
    }
    
    private long stringToDate(String date){
        System.out.println(date);

        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the input string into a LocalDate
        LocalDate inputDate = LocalDate.parse(date.trim(), formatter);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the difference in days
        long daysBetween = ChronoUnit.DAYS.between(inputDate, currentDate);

        // Print the result
        return daysBetween;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


