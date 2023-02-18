import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Collections;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

public class MovieDatabaseBuilder {

    public static ArrayList<SimpleMovie> getMovieDB(String fileName) {
        ArrayList<SimpleMovie> movies = new ArrayList<SimpleMovie>();
        try {
            File movieData = new File(fileName);
            Scanner reader = new Scanner(movieData);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split("---");
                if (data.length > 1) {
                    SimpleMovie s = new SimpleMovie(data[0], data[1]);
                    movies.add(s);
                }

            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return movies;
    }

    public static ArrayList<String> getActorDB(String fileName) {
        ArrayList<String> actors = new ArrayList<>();
        try {
            File actorData = new File(fileName);
            Scanner reader = new Scanner(actorData);
            while (reader.hasNextLine()) {
                actors.add(reader.nextLine());
            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        List<String> actorList = actors.subList(0, actors.size());
        Collections.sort(actorList);
        return new ArrayList<>(actorList);
    }

    public static ArrayList<String> getConnectionsDB(String fileName) {
        ArrayList<String> connections = new ArrayList<>();
        try {
            File connectionsData = new File(fileName);
            Scanner reader = new Scanner(connectionsData);
            while (reader.hasNextLine()) {
                connections.add(reader.nextLine());
            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return connections;
    }
}
