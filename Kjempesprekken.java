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
	Overview o = new Overview();
	o.readCompetitors("kjempesprekken.csv");
	o.shell();
	o.printCompetitors("hei.csv");
    }

}

class Overview {
    public final static String firstColumn = "";
    public final static String secondColumn = "Navn";
    public final static String thirdColumn = "Deltakelser";
    public final static String firstFooterColumn = "";
    public final static String secondFooterColumn = "Totalt antall deltakelser:";
    public final static String separator = ";";

    private int thirdFooterColumn;
    private TreeSet<Competitor> competitors = new TreeSet<Competitor>();

    public void readCompetitors(String filename) throws Exception {
	CSVReader reader = new CSVReader(new FileReader(filename), ';');

	List<String[]> myEntries = reader.readAll();

	for (String[] s : myEntries) {
	    if (s[0].equalsIgnoreCase(firstColumn) &&
		s[1].equalsIgnoreCase(secondColumn) &&
		s[2].equalsIgnoreCase(thirdColumn)) {
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

    public void printCompetitors(String filename) throws Exception {
	CSVWriter writer = new CSVWriter(new FileWriter(filename), ';');

	writer.writeNext(new String[]{firstColumn, secondColumn, thirdColumn});

	int currentCount = 1;
	for (Competitor c : competitors) {
	    String[] cur = c.toStringArray();
	    cur[0] = "" + currentCount;

	    writer.writeNext(cur);

	    currentCount++;
	}

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

    public String[] toStringArray() {
	return new String[] {"", name, count+""};
    }
}
