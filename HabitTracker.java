import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.time.*;
import java.util.*;

public class HabitTracker extends Application {
    private static final String FILE_NAME = "habits.txt";
    private final ArrayList<String> habits = new ArrayList<>();
    private final ArrayList<Integer> streaks = new ArrayList<>();
    private final ArrayList<CheckBox> checkboxes = new ArrayList<>();

    private final TextField newHabitField = new TextField();
    private final GridPane habitGrid = new GridPane();
    private final VBox layout = new VBox(10);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initializeApp();

        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setOnAction(event -> addHabit());

        Button confirmChecked = new Button("Confirm Habits for Today");
        confirmChecked.setOnAction(event -> confirmHabits());

        HBox inputArea = new HBox(10, newHabitField, addHabitButton);
        HBox confirmArea = new HBox(10, confirmChecked);

        layout.getChildren().addAll(habitGrid, confirmArea, inputArea);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 500, 400);
        stage.setTitle("Habit Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeApp() {
        loadHabitsFromFile();
        displayHabits();
        newHabitField.setPromptText("Enter a new habit");

        habitGrid.setVgap(10);
        habitGrid.setHgap(20);
    }

    private void addHabit() {
        String newHabit = newHabitField.getText().trim();
        if (!newHabit.isEmpty()) {
            habits.add(newHabit);
            streaks.add(0);
            saveAllHabitsToFile();
            newHabitField.clear();
            displayHabits();
        }
    }

    private void confirmHabits() {
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                streaks.set(i, streaks.get(i) + 1);
            } else {
                streaks.set(i, 0);
            }
        }
        saveAllHabitsToFile();
        displayHabits();
    }

    private void displayHabits() {
        habitGrid.getChildren().clear();
        checkboxes.clear();

        for (int i = 0; i < habits.size(); i++) {
            String habit = habits.get(i);
            CheckBox checkBox = new CheckBox(habit);
            Label streakLabel = new Label("Streak: " + streaks.get(i));

            Button deleteHabitButton = new Button("Delete");
            int index = i; // Capture the current index
            deleteHabitButton.setOnAction(event -> {
                deleteHabit(index);
                displayHabits();
            });

            habitGrid.add(checkBox, 0, i);
            habitGrid.add(streakLabel, 1, i);
            habitGrid.add(deleteHabitButton, 2, i);
            checkboxes.add(checkBox);
        }
    }

    private void deleteHabit(int index) {
        habits.remove(index);
        streaks.remove(index);
        saveAllHabitsToFile();
    }

    private void loadHabitsFromFile() {
        habits.clear();
        streaks.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    habits.add(parts[0].trim());
                    streaks.add(Integer.parseInt(parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveAllHabitsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < habits.size(); i++) {
                writer.write(habits.get(i) + "," + streaks.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // i'm going to be redoing the streak system; not sure if we need this but i'll keep it here anyways
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
}
    