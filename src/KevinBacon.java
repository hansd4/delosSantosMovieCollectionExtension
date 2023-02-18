import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KevinBacon {
    private Scanner scan;

    private boolean running;

    private ArrayList<SimpleMovie> movies;
    private ArrayList<String> actors;
    private ArrayList<String> connections;
    private ArrayList<Integer> lastActorIndices;

    public KevinBacon() {
        scan = new Scanner(System.in);

        running = true;

        movies = MovieDatabaseBuilder.getMovieDB("src/movie_data.txt");
        actors = MovieDatabaseBuilder.getActorDB("src/actors.txt");

        // temp code to write actors to a txt file
//        try {
//            File f = new File("src/actors.txt");
//            FileWriter fw = new FileWriter(f);
//            for (String actor : actors) {
//                fw.write(actor + "\n");
//            }
//            fw.close();
//        }
//        catch (IOException ioe) {
//            System.out.println("Writing file failed");
//            System.out.println(ioe);
//        }

        connections = MovieDatabaseBuilder.getConnectionsDB("src/connections.txt");
        lastActorIndices = new ArrayList<>();
    }

    public void run() {
//        getConnections();
//
//        // temp code to write connections to a txt file
//        try {
//            File f = new File("src/connections.txt");
//            FileWriter fw = new FileWriter(f);
//            for (ArrayList<String> connection : connections) {
//                if (connection == null) {
//                    fw.write("\n");
//                } else {
//                    for (int i = 0; i < connection.size() - 1; i++) {
//                        fw.write(connection.get(i) + " -> ");
//                    }
//                    fw.write("Kevin Bacon\n");
//                }
//            }
//            fw.close();
//        }
//        catch (IOException ioe) {
//            System.out.println("Writing file failed");
//            System.out.println(ioe);
//        }
        while (running) {
            System.out.print("Enter an actor's name or (q) to quit: ");
            String input = scan.nextLine();
            if (input.equals("q")) { // quit
                running = false;
            } else {
                int actorIndex = actorIndex(input);
                if (actorIndex == -1 || connections.get(actorIndex).equals("")) { // actor not found in array or doesn't have a connection
                    System.out.println("No results found");
                } else {
                    String connection = connections.get(actorIndex);
                    System.out.println(connection);
                    System.out.println("Bacon Number of: " + connection.split(" -> ").length / 2);
                }
            }
            System.out.println();
        }
    }

//    private void getConnections() {
//        for (int i = 0; i < actors.size(); i++) { // initialize connections as a fully null list with the same size as actors
//            connections.add(null);
//        }
//        int kevinIndex = actorIndex("Kevin Bacon");
//        connections.set(kevinIndex, new ArrayList<>(List.of("Kevin Bacon"))); // Kevin Bacon himself
//        int count = 0;
//        int actorCount = 0;
//        int totalChangedIndices = 1;
//        lastActorIndices.add(kevinIndex); // adds kevin's index to the list of indices to be checked in the first iteration
//        while (count <= 50 && isIncomplete()) { // while six degrees haven't been met yet AND the list has at least one unconnected actor
//            count++;
//            ArrayList<Integer> newActorIndices = new ArrayList<>(); // create a new list to store new indices in this iteration
//            for (Integer i : lastActorIndices) { // go through all the indices modified in the previous iteration
//                ArrayList<String> connection = connections.get(i);
//                for (SimpleMovie movie : getMoviesFromActor(connection.get(0))) { // iterate through every movie from this actor from last iteration
//                    for (String actor : movie.getActors()) { // iterate through every actor in this movie
//                        int actorIndex = actorIndex(actor); // get the index of this actor
//                        if (actorIndex != -1 && connections.get(actorIndex) == null) { // if actor found AND this actor hasn't been connected to Kevin Bacon yet,
//                            ArrayList<String> newConnection = new ArrayList<>(connection); // create a new connection copying the current root connection
//                            newConnection.add(0, movie.getTitle()); // add the movie title that connects the actors to the connection, at the front
//                            newConnection.add(0, actor); // add the actor that hasn't been connected yet
//                            connections.set(actorIndex, newConnection); // put this new actor connection to Kevin Bacon at the index corresponding to the actor's location in the actors list
//                            newActorIndices.add(actorIndex); // add this actor as a stem to be expanded upon in future iterations
//                            actorCount++;
//                            System.out.println("(Iteration " + count + ") " + (((double) actorCount / actors.size()) * 100) + " percent done, " + actorCount + " out of " + actors.size() + " complete. Just connected " + actor + " via " + newConnection);
//                        }
//                    }
//                }
//            }
//            lastActorIndices = newActorIndices; // once the whole iteration is finished, clear out the last degree of actors and replace it with current degree
//            totalChangedIndices += newActorIndices.size();
//            System.out.println("Iteration " + count + " complete. " + newActorIndices.size() + " " + count + " degree actors successfully connected. Remaining actors: " + (actors.size() - totalChangedIndices));
//        }
//    }

    // returns an ALPHABETICALLY SORTED list of all the actors that played in a given list of movies
    private ArrayList<String> getActorsFromMovies(ArrayList<SimpleMovie> movieList) {
        ArrayList<String> result = new ArrayList<>(); // returned actors
        int count = 0;
        for (SimpleMovie movie : movieList) { // iterate through all given movies
            ArrayList<String> movieActors = movie.getActors(); // get actors of current movie
            result.add(movieActors.get(0)); // add the first actor to appear in the list of actors from the current movie (they can't be duplicates and a list of 1 is always sorted)
            outerLoop:
            for (int j = 1; j < movieActors.size(); j++) { // iterate through actors of current movie
                for (int i = 0; i < result.size(); i++) { // iterate through actors in current resulting list
                    int comparison = movieActors.get(j).compareTo(result.get(i));
                    if (comparison == 0) { // if a duplicate is found in result, ignore this actor and continue to the next
                        continue outerLoop;
                    } else if (comparison < 0) { // otherwise, when a spot is found that would keep the list alphabetically sorted, insert this actor into that spot
                        result.add(i, movieActors.get(j));
                        continue outerLoop;
                    }
                }
            }
            count++;
            System.out.println(count + " out of " + movieList.size() + " finished. " + (((double) count / movieList.size()) * 100) + " percent of the way through. Just added: " + movie.getTitle());
        }
        System.out.println(result);
        return result;
    }

    // returns all the movies this actor has starred in
    private ArrayList<SimpleMovie> getMoviesFromActor(String actor) {
        ArrayList<SimpleMovie> result = new ArrayList<>();
        for (SimpleMovie movie : movies) {
            if (movie.getActors().contains(actor)) {
                result.add(movie);
            }
        }
        return result;
    }

    // returns an ALPHABETICALLY SORTED list of all the actors with a degree of 1 to this actor
    private ArrayList<String> getActorsInCommon(String actor) {
        return getActorsFromMovies(getMoviesFromActor(actor));
    }

    private SimpleMovie getMovieInCommon(String actor1, String actor2) {
        for (SimpleMovie movie : getMoviesFromActor(actor1)) {
            for (SimpleMovie movie2 :getMoviesFromActor(actor2)) {
                if (movie.equals(movie2)) {
                    return movie;
                }
            }
        }
        return null;
    }

    // finds the index of the actor in both the actors and connections lists, using BINARY SEARCH
    private int actorIndex(String actor) {
        int low = 0;
        int high = actors.size() - 1;

        while (high >= low) {
            int middle = (high + low) / 2;
            int comparison = actor.compareTo(actors.get(middle));
            if (comparison < 0) {
                high = middle - 1;
            } else if (comparison > 0) {
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    // like actorIndex but with commentary
    private int actorIndexDebug(String actor) {
        int low = 0;
        int high = actors.size() - 1;

        int numChecks = 0;

        while (high >= low) {
            numChecks++;

            int middle = (high + low) / 2;
            System.out.println("Checking value at index " + middle + " (value = " + actors.get(middle) + ")");
            int comparison = actor.compareTo(actors.get(middle));
            if (comparison < 0) {
                high = middle - 1;
                System.out.println("-- " + actors.get(middle) + " is too HIGH! Setting right index to: " + high + " (value = " + actors.get(middle) + ")");
            } else if (comparison > 0) {
                low = middle + 1;
                System.out.println("-- " + actors.get(middle) + " is too LOW! Setting left index to: " + low + " (value = " + actors.get(middle) + ")");
            } else {
                System.out.println("The target " + actor + " was FOUND at index " + middle + "!  Returning " + middle);
                System.out.println("A total of " + numChecks + " elements were checked for a runtime of " + numChecks + " loop iterations");
                return middle;
            }
        }
        System.out.println("The target " + actor + " was NOT found; returning -1");
        System.out.println("A total of " + numChecks + " elements were checked for a runtime of " + numChecks + " loop iterations");
        return -1;
    }

//    private boolean isIncomplete() {
//        for (ArrayList<String> connection : connections) {
//            if (connection == null) {
//                return true;
//            }
//        }
//        return false;
//    }
}
