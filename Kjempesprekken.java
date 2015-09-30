import java.util.TreeSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Created 2015/03/11 09:31:11
 *
 * How to transfer CSV to HTML and back:
 *  http://www.convertcsv.com/html-table-to-csv.htm
 *
 * @author mikaello
 */
public class Kjempesprekken {
    public static void main(String[] args) throws Exception {
        String inCSV, outCSV = "";

        if (args.length == 2) {
            inCSV = args[0];
            outCSV = args[1];
        } else {
            System.err.println("Run the program like this:");
            System.err.println("   java Kjempesprekken <inCSV> <outCSV>");
            return;
        }

        Overview o = new Overview();
        o.readCompetitors(inCSV);
        o.shell();
        o.printCompetitors(outCSV);
    }

}

class Overview {
    public final static String firstHeaderColumn = "";
    public final static String secondHeaderColumn = "Navn";
    public final static String thirdHeaderColumn = "Deltakelser";
    public final static String firstFooterColumn = "";
    public final static String secondFooterColumn = "Totalt antall deltakelser:";
    public final static char separator = ',';

    private static final String UNDO = "!undo";
    private static final String LIST_ALL = "!list";
    private static final String LIST_PREFIX = "!list";
    private static final String LIST_COMMANDS = "!help";
    private static final String EXIT_SAVE = "!exit";
    private static final String EXIT_DISCARD = "!exit hard";

    private int thirdFooterColumn;
    private Set<Competitor> competitors = new TreeSet<Competitor>();
    private Stack<Competitor> namesAdded = new Stack<Competitor>();

    /**
     * Read competitors from a CSV-file to a TreeSet<Competitor>. Also
     * updates the thirdFooterColumn to the number given in the footer
     * of the CSV-file.
     * @param filename name of the CSV-file to be read
     */
    public void readCompetitors(String filename) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(filename), separator);

        List<String[]> myEntries = reader.readAll();

	if (myEntries.isEmpty()) {
	    System.err.println("ERROR: No entries is found, check file");
	    System.exit(1);
	} else if (myEntries.get(0).length == 1) {
	    System.err.println("ERROR: Only one element per line, check separator (use " + separator + ")");
	    System.exit(1);
	}

        for (String[] s : myEntries) {
            if (s[0].equalsIgnoreCase(firstHeaderColumn) &&
                s[1].equalsIgnoreCase(secondHeaderColumn) &&
                s[2].equalsIgnoreCase(thirdHeaderColumn)) {
                // Catch header
                System.out.println("Starting reading .csv-file...");

            } else if (s[0].equalsIgnoreCase(firstFooterColumn) &&
                       s[1].equalsIgnoreCase(secondFooterColumn)) {
                // Catch footer
                thirdFooterColumn = Integer.parseInt(s[2]);
                System.out.printf("Finished reading .csv-file...     (%d entries)\n", competitors.size());

            } else {
                // Or catch competitor
                competitors.add(new Competitor(s[1], Integer.parseInt(s[2])));
            }
        }

        for (Competitor c : competitors) {
            //System.out.println(c);
        }
    }

    /**
     * Prints out all entries from the TreeSet containing all the
     * competitors and their stats to a CSV-file with the filename
     * given. Makes header and footer in the CSV-file.
     * @param filename name of the CSV-file that is made
     */
    public void printCompetitors(String filename) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(filename), ';');

        // Add header
        writer.writeNext(new String[]{firstHeaderColumn, secondHeaderColumn, thirdHeaderColumn});

        int currentCount = 1;
        for (Competitor c : competitors) {
	    if (c.getCount() < 1) {
		continue;
	    }

            // Get right position number of the competitor
            String[] cur = c.toStringArray();
            cur[0] = "" + currentCount;

            // Store all three fields (position, name, stat) to CSV-file
            writer.writeNext(cur);

            // Update position
            currentCount++;
        }

        // Add footer
        writer.writeNext(new String[]{firstFooterColumn, secondFooterColumn, thirdFooterColumn + ""});
        writer.close();

        System.out.printf("Finished printing %s-file... (%d entries)\n", filename, currentCount-1);
    }

    public void shell() {
        Scanner scan = new Scanner(System.in);
	boolean queryUser = true;

	printCommands();
        do {
            System.out.print("Name: " );
            String lastNameProvided = scan.nextLine().trim();

	    if (lastNameProvided.length() == 0) {
		continue;
	    } else if (lastNameProvided.charAt(0) == '!') {
		queryUser = handleCommand(lastNameProvided);
            } else {
		if ((lastNameProvided.split("\\s").length == 1)) {
		    System.out.print("Name consisting of only one word, is it correct [y/n]: ");
		    String answer = scan.nextLine();

		    if (answer.charAt(0) == 'n') {
			continue;
		    }
		}

                Competitor added = addNewName(lastNameProvided);
		if (added == null) {
		    continue;
		} else {
		    namesAdded.push(added);
		    System.out.println(added + "\n");
		}
            }
        } while(queryUser);
    }

    /**
     * Takes the correct action according to the given command.
     *
     * @return true if the job is done and is ready for a new command
     */
    private boolean handleCommand(String command) {
            if (command.equalsIgnoreCase(EXIT_SAVE)) {
                System.out.println();
                return false;
	    } else if (command.equalsIgnoreCase(EXIT_DISCARD)) {
		// Exit without saving
		System.exit(0);
            } else if (command.equalsIgnoreCase(UNDO)) {
		undoLastAdd();
	    } else if (command.equalsIgnoreCase(LIST_ALL)) {
		listAllNames();
	    } else if (command.equalsIgnoreCase(LIST_PREFIX)) {
		// TODO
	    } else if (command.equalsIgnoreCase(LIST_COMMANDS)) {
		printCommands();
	    }

	    return true;
    }

    private void printCommands() {
        System.out.println("\n\nPossible commands:");
        System.out.println("To add new name:               <name>      " +
			   "(just write the name and ENTER, no special command)");
	System.out.printf("To undo last add:              %s\n", UNDO);
	System.out.printf("To list all names:             %s\n", LIST_ALL);
	System.out.printf("To list all names with prefix: %s <name>\n", LIST_PREFIX);
	System.out.printf("To print this list:            %s\n", LIST_COMMANDS);
        System.out.printf("To quit and save:              %s\n", EXIT_SAVE);
        System.out.printf("To quit and not save:          %s\n", EXIT_DISCARD);
        System.out.println();
    }

    public boolean undoLastAdd() {
	if (namesAdded.isEmpty()) {
	    System.err.println("Nothing to undo\n");
	    return false;
	}

	Competitor lastAdded = namesAdded.pop();
	int currentCount = decreaseCompetitorCount(lastAdded);

	if (currentCount < 0) {
	    System.err.printf("Something went wrong, '%s' has a negative participation count: %d\n\n",
			      lastAdded.getName(), currentCount);
	    return false;
	} else if (currentCount == 0) {
	    // Remove newly added competitors when doing an 'undo'
	    competitors.remove(lastAdded);
	    System.out.printf("The competitor '%s' is removed from the list\n\n", lastAdded.getName());
	} else {
	    System.out.printf("The last added participation of '%s' is removed, current stat is now:\n",
			      lastAdded.getName());
	    System.out.println(lastAdded + "\n");
	}
	return true;
    }

    /**
     * Either adds a new name to the TreeSet containing all the
     * competitors, or it will increase the count of a competitor
     * already in the list.
     */
    public Competitor addNewName(String name) {
        Competitor c = getCompetitor(name);

        if (c != null) {
	    // Have to remove-update-add to make the TreeSet sorted
	    increaseCompetitorCount(c);

        } else {
            List<Competitor> l = getSimilarCompetitor(name);

            if (l.size() != 0) {
		// Check for similar competitors, and let user decide
                Scanner scan = new Scanner(System.in);

                System.out.println("Could not find " + name + ", but found similar: ");

                for (Competitor com : l) {
                    System.out.println(com);
                }

		System.out.println("\nIs any of these the correct? y / n: ");
		if (scan.nextLine().equalsIgnoreCase("y")) {
		    // Return null, and let user type new name
		    return null;
		}
            }

            c = new Competitor(name);
            competitors.add(c);
        }

	thirdFooterColumn++; // Total number of participations
        return c;
    }

    /**
     * Get an competitor from the TreeSet matching the name.
     *
     * @param name name of the competitor to be fetched
     * @return the matching competitor-object, or null if not found
     */
    public Competitor getCompetitor(String name) {
        for (Competitor c : competitors) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Returns competitors in the TreeSet containing all the
     * competitors, that partially matches the given name.
     */
    public List<Competitor> getSimilarCompetitor(String name) {
        String[] names = name.toLowerCase().split("([-]|\\s)+");

	List<Competitor> competitorList = new ArrayList<Competitor>();

        for (Competitor c : competitors) {
            String cName = c.getName().toLowerCase();

            // Check all subnames of the name
            for (String s : names) {
                if (cName.contains(s)) {
                    competitorList.add(c);
                    break;
                }
            }
        }

        return competitorList;
    }

    /**
     * List all competitors with participation count
     */
    public void listAllNames() {
	for (Competitor c : competitors) {
	    System.out.println(c);
	}
	System.out.println();
    }

    /**
     * Wrapper for Competitor.increaseCount(). Necessary for the TreeSet to be sorted.
     * @param c the competitor for which the count should be increased
     * @return current count
     */
    public int increaseCompetitorCount(Competitor c) {
	    competitors.remove(c);
            int currentCount = c.increaseCount();
	    competitors.add(c);

	    return currentCount;
    }

    /**
     * Wrapper for Competitor.decreaseCount(). Necessary for the TreeSet to be sorted.
     * @param c the competitor for which the count should be decreased
     * @return current count
     */
    public int decreaseCompetitorCount(Competitor c) {
	    competitors.remove(c);
            int currentCount = c.decreaseCount();
	    competitors.add(c);

	    return currentCount;
    }

}

/**
 * Every person with the correct stats are stored as objects of this
 * class.
 */
class Competitor implements Comparable<Competitor> {
    private final String name;
    private int count;

    Competitor(String name) {
        this(name, 1);
    }

    Competitor(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public int getCount() { return count; }
    public String getName() { return name; }

    /** Do not call this method directly, use Kjempesprekken.increaseCompetitorCount() */
    public int increaseCount() { return ++count; }

    /** Do not call this method directly, use Kjempesprekken.decreaseCompetitorCount() */
    public int decreaseCount() { return --count; }

    public int compareTo(Competitor c) {
        int bestCount = c.getCount() - getCount();
        if (bestCount == 0) {
            return getName().compareTo(c.getName());
        }

        return bestCount;
    }

    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Competitor) {
            Competitor that = (Competitor) other;
            result = (this.getName() == that.getName() &&
		      this.getCount() == that.getCount());
        }
        return result;
    }

    public String toString() {
        return String.format("%3d %s", getCount(), getName());
    }

    /**
     * Makes a string array with all the fields of an competitor. When
     * printing to a CSV-file it is necessary to give a string-array,
     * then this method can be used.
     */
    public String[] toStringArray() {
        return new String[] {"", getName(), getCount()+""};
    }
}
