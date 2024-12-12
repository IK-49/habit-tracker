import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;

/**
 * VisualPage class provides a JavaFX-based UI for visualizing habit streaks
 * using colored rectangles. Streak data is read from a file and displayed in a grid layout.
 */
public class VisualPage extends Application {
    private final String FILE_NAME = "habits.txt"; // File storing habit data
    private final ArrayList<Rectangle> visuals = new ArrayList<>(); // Stores visual rectangles for streaks

    /**
     * Main entry point for launching the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    /**
     * The start method initializes the UI components and sets up the scene.
     *
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        // Button to navigate back to the main habit tracker
        Button back = new Button("Back");

        // Create a grid pane for the habit calendar
        GridPane habitCalendar = new GridPane();
        ScrollPane scroller = new ScrollPane(); // Enables scrolling for large visualizations
        scroller.setContent(habitCalendar); // Attach habitCalendar to the scroller
        habitCalendar.setPadding(new Insets(10, 10, 10, 10)); // Add padding around grid
        habitCalendar.setMinSize(300, 300); // Set minimum size for the grid
        habitCalendar.setVgap(10); // Vertical spacing between rows
        habitCalendar.setHgap(10); // Horizontal spacing between columns

        // Back button functionality to return to HabitTracker view
        back.setOnAction(event -> {
            try {
                HabitTracker ht = new HabitTracker();
                ht.start(stage); // Switch back to the main habit tracker view
            } catch (Exception e) {
                throw new RuntimeException(e); // Handle exceptions related to stage switching
            }
        });

        // Visualize habit streaks using rectangles
        streaksVisual(habitCalendar, back);

        // Set up the scene with a scrollable layout
        Scene habitVisual = new Scene(scroller, 550, 500); // Scene with scrollable content
        stage.setTitle("Habit Visualization"); // Set window title
        stage.setScene(habitVisual); // Set the primary scene
        stage.show(); // Display the stage
    }

    /**
     * Generates a visual representation of habit streaks and adds them to the provided GridPane.
     *
     * @param habitCalendar The GridPane to populate with habit streak data.
     * @param backButton    The "Back" button to return to the previous screen.
     */
    public void streaksVisual(GridPane habitCalendar, Button backButton) {
        habitCalendar.getChildren().clear(); // Clear previous content
        habitCalendar.add(backButton, 0, 0); // Add the back button to the top-left corner

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1; // Tracks the current row in the grid
            int returnNumber = 0; // Tracks row resets for streaks longer than 7
            Color fillColor = Color.GREEN; // Default color for streak rectangles

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue; // Skip invalid lines

                // Extract habit name and streak count
                String habitName = parts[0];
                int streak = Integer.parseInt(parts[1]);

                // Add the habit name to the first column of the current row
                habitCalendar.add(new Label(habitName), 0, lineNumber);

                // Create rectangles representing streak days
                for (int i = 0; i < streak; i++) {
                    // Change color every 7 days to indicate week milestones
                    fillColor = ((i + 1) % 7 == 0) ? Color.BLUE : Color.GREEN;

                    // Start a new row if streak exceeds the width of 7 days
                    if ((i + 1) > 7 && (i + 1) % 7 == 1) {
                        lineNumber++; // Move to the next row
                        returnNumber++; // Adjust column placement for new row
                    }

                    // Add a rectangle to represent the streak day
                    habitCalendar.add(new Rectangle(40, 40, fillColor), i + 1 - (returnNumber * 7), lineNumber);
                }

                // Move to the next row for the next habit
                lineNumber++;
                returnNumber = 0; // Reset return tracker for the next habit
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Handle file reading errors gracefully
        }
    }
}
