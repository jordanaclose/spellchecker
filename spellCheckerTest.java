
import java.util.*;
import java.io.*;

public class spellCheckerTest{

	public static void main(String[] args) throws FileNotFoundException{

		spellChecker spellchecker = new spellChecker();

		spellchecker.checkFile();
		spellchecker.suggestions();
	}
}