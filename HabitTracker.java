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
        ArrayList<String> streaks = loadStreakFromFile("habits.txt");
        
        
        // GridPane will be used to display each habit
        GridPane habitGrid = new GridPane();
        habitGrid.setVgap(10);
        habitGrid.setHgap(20);

        // Iterates through each habit
        for (int i = 0; i < habits.size()-1; i++) {
            String habit = habits.get(i);
            
            CheckBox checkBox = new CheckBox(habit);
            
            Label streak = new Label("Streak: "+streaks.get(i));
            System.out.println(streak.getText());

            habitGrid.add(checkBox, 0, i);
            habitGrid.add(streak, 1, i);
        }

        // Habit entry field
        TextField newHabitField = new TextField();
        newHabitField.setPromptText("Enter a new habit");
        ArrayList<CheckBox> checkboxes = new ArrayList<>();
        ArrayList<Label> streakLabels = new ArrayList<>();
        
        
        displayHabits(habitGrid, habits, streaks, checkboxes);
        
        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setOnAction(event -> {
            String newHabit = newHabitField.getText().trim();
            if (!newHabit.isEmpty()) {
                habits.add(newHabit);
                streaks.add("0");
                writeHabitsToFile("habits.txt", newHabit, 0); // Habit name written
                
                // Gets the next row and assigns the new habit to it / displays on PaneGrid
                
                //int newRow = habitGrid.getRowCount();
                
                CheckBox newCheckBox = new CheckBox(newHabit);
                /*
                Label streak = new Label("Streak: "+streaks.get(streaks.size()-1));
                habitGrid.add(newCheckBox, 0, newRow);
                habitGrid.add(streak, 1, newRow);
                */
                newHabitField.clear();
                habitGrid.getChildren().clear();
                displayHabits(habitGrid, habits, streaks, checkboxes);
            }
        });
        
        Button confirmChecked = new Button("Confirm?");
        confirmChecked.setOnAction(event -> {
            for(int i = 0; i < checkboxes.size(); i++){
                if(checkboxes.get(i).isSelected()){
                    updateStreak("habits.txt", i, true);
                    streaks.set(i, String.valueOf(Integer.parseInt(streaks.get(i))+1)); //Converts to number, adds, and back to string
                }
            }
            
            ArrayList<String> updatedStreaks = loadStreakFromFile("habits.txt");
            
            displayHabits(habitGrid, habits, updatedStreaks, checkboxes);
        
        });
        
        HBox inputArea = new HBox(10, newHabitField, addHabitButton);
        HBox confirm = new HBox(10, confirmChecked);

        layout.getChildren().addAll(habitGrid, inputArea, confirm);

        Scene scene = new Scene(layout, 500, 400);
        stage.setTitle("Habit Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void displayHabits(GridPane habitGrid, ArrayList<String> habits, ArrayList<String> streaks, ArrayList<CheckBox> checkboxes) {
        habitGrid.getChildren().clear(); // Clear any previous content in the GridPane to avoid duplicate entries
        
        for (int i = 0; i < habits.size(); i++) {
            String habit = habits.get(i);
            CheckBox checkBox = new CheckBox(habit);
            Label streakLabel = new Label("Streak: " + streaks.get(i));
            
            habitGrid.add(checkBox, 0, i); // Add the CheckBox at column 0, row i
            habitGrid.add(streakLabel, 1, i); // Add the streak label at column 1, row i
            
            checkboxes.add(checkBox); // Add the CheckBox to the checkboxes list for later use
        }
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
    
    private ArrayList<String> loadStreakFromFile(String fileName) 
    {
        ArrayList<String> streaks = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Scanner s = new Scanner(line.trim());
                s.useDelimiter("\"");
                s.next(); s.next();
                String streak = s.next();
                System.out.println(streak);
                s.close();
                streaks.add(streak);// Adds each individual streak in habits.txt to the streaks ArrayList

            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        
        return streaks;
    }

    private void writeHabitsToFile(String fileName, String habit, int streak) {
        LocalDate creationDate = now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("\""+habit+"\""+"\""+creationDate+"\""+streak+"\"");
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
    }
    
    private LocalDate now(){
        LocalDate creationDate = LocalDate.now();
        return creationDate;
    }
    
    private void updateStreak(String fileName, int lineNumber, boolean extend){
        ArrayList<String> fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                fileContent.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Error while loading habits
        }
        
        String target = fileContent.get(lineNumber);
        Scanner s = new Scanner(target).useDelimiter("\"");
        s.next(); s.next();
        String oldStreak = s.next();
        
        if(extend){
            target = target.replaceAll("\""+oldStreak+"\"", "\""+String.valueOf(Integer.parseInt(oldStreak)+1)+"\"");
        }
        else{
            target = target.replaceAll("\""+oldStreak+"\"", "\""+0+"\"");
        }
        
        fileContent.set(lineNumber, target);
        
        for(int i = 0; i < fileContent.size(); i++){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, i > 0))) { //Need to overwrite what we have
                writer.write(fileContent.get(i));
                writer.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage()); // Error while loading habits
            }
        
        }
    }
    /*
    private String stringToStreak(String date){
        System.out.println("date: "+date);    
            
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        if(date.equals("")){
            return "0";
        }

        // Parse the input string into a LocalDate
        LocalDate inputDate = LocalDate.parse(date.trim(), formatter);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the difference in days
        String daysBetween = String.valueOf(ChronoUnit.DAYS.between(inputDate, currentDate));
        
        // Print the result
        
        return daysBetween;
    }
    */
    public static void main(String[] args) {
        launch(args);
    }
}