import java.util.TreeSet;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Created 2015/03/11 09:31:11
 * @author mikaello@student.matnat.uio.no
 */
public class Kjempesprekken {
    public static void main(String[] args) throws Exception {
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
    public final static String separator = ";";

    private int thirdFooterColumn;
    private TreeSet<Competitor> competitors = new TreeSet<Competitor>();

    /**
     * Read competitors from a CSV-file to a TreeSet<Competitor>. Also
     * updates the thirdFooterColumn to the number given in the footer
     * of the CSV-file.
     * @param filename name of the CSV-file to be read
     */
    public void readCompetitors(String filename) throws Exception {
	CSVReader reader = new CSVReader(new FileReader(filename), ';');

	List<String[]> myEntries = reader.readAll();

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
	System.out.println("shell");

	// while(true) {
	//     System.out.print ("Command: ");
	// }
    }

}

/**
 * Every person with the correct stats are stored as objects of this
 * class.
 */
class Competitor implements Comparable<Competitor> {
    private final String name;
    private int count;

    Competitor(String name, int count) {
	this.name = name;
	this.count = count;
    }

    public int getCount() { return count; }
    public int increaseCount() { return ++count; }

    public int compareTo(Competitor c) {
	int bestCount = c.count - count;
	if (bestCount == 0) {
	    return name.compareTo(c.name);
	}

	return bestCount;
    }

    public String toString() {
	return String.format("%3d %s", count, name);
    }

    /**
     * Makes a string array with all the fields of an competitor. When
     * printing to a CSV-file it is necessary to give a string-array,
     * then this method can be used.
     */
    public String[] toStringArray() {
	return new String[] {"", name, count+""};
    }
}
