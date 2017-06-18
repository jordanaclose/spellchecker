
import java.util.*;
import java.io.*;

public class spellChecker{

	public int count = 0;

	ArrayList<String> misspeltWords = new ArrayList<String>(); //arraylist to hold all the misspelt words in the text file
	ArrayList<String> suggestions = new ArrayList<String>(); //arraylist to store the suggested replacements
	ArrayList<String> text = new ArrayList<String>(); //arraylist to store all the words in the text file
	ArrayList<String> dictionary = new ArrayList<String>(); //arraylist to store all the words in the dictionary file
	ArrayList<String> localDictionary = new ArrayList<String>(); //arraylist to store words to be added to a local dictionary

	levenshtein levenshtein = new levenshtein(); //initialises a levenshtein object

	public void checkFile(){
		try{
			File textFile = new File("Text.txt");
			File dictionaryFile = new File("dictionary.txt");
			File localDictionaryFile = new File("localDictionary.txt");

			Scanner scanner = new Scanner(textFile);
			Scanner scanner2 = new Scanner(dictionaryFile);
			Scanner scanner3 = new Scanner(localDictionaryFile);

			while(scanner.hasNext()){
				text.add(scanner.next().replace(".", "").replace(",", "").replace("?", "").replace("!", "").replace("\"", ""));
				//add every word from the text file into an arraylist so we can scan through multiple times.
				//at the same time it removes certain punctuation from the strings
			}
			while(scanner2.hasNext()){
				dictionary.add(scanner2.next()); //add every word from the dictionary file into an arraylist so we can scan through multiple times
			}
			while(scanner3.hasNext()){
				localDictionary.add(scanner3.next());
			}
			for(int i = 0; i < text.size(); i++){
			boolean found = false;
				for(int j = 0; j < dictionary.size(); j++){
					if((text.get(i).toLowerCase()).equals(dictionary.get(j))){ //for each word in the input file, the dictionary file gets searched to see if
						found = true;	// any words match it. If so, the count gets increased to show that the word is recognised.
					}
				}
				for(int n = 0; n < localDictionary.size(); n++){
					if((text.get(i).toLowerCase()).equals(localDictionary.get(n))){ //the local dictionary is checked too to see if the word has already been added to it
						found = true;
					}
				}
				if(found == false){
					for(int k = 0; k < misspeltWords.size(); k++){
						if(text.get(i).toLowerCase().equals(misspeltWords.get(k).toLowerCase())){
							count++; //counts if the unrecognised word from the file has already been added to the arraylist
						}
					}
					if(count == 0){ //only adds the unrecognised word if it isn't already in the arraylist, so that multiple words aren't added
						misspeltWords.add(text.get(i).toLowerCase()); //if no matching words were found, the word gets added to an array of misspelt words
					}
				}
			}
		}catch(FileNotFoundException oh){
			System.out.println("Error: file not found");
		}
	}

	public void suggestions(){
		//loops through the array of misspelt words and, for each word, applies the levenshtein function to each word in the dictionary
		// to create a list of suggested replacements. i.e. if the word in the dictionary has a levenshtein distance of 2 or less from
		//the misspelt word, it gets added to an array of suggestions.
		for(int i = 0; i < misspeltWords.size(); i++){
			System.out.println("The word '" + misspeltWords.get(i) + "' was not recognised. Please choose a replacement from the list of suggestions below, or type in your own replacement.");
			for(int j = 0; j < dictionary.size(); j++){ //
				if((levenshtein.levenshtein(misspeltWords.get(i),dictionary.get(j))) <= 2){
					suggestions.add(dictionary.get(j));
				}
			}
			if(suggestions.size() == 0){
				System.out.println("There are no word suggestions");
			}
			else{
				System.out.println("Suggested replacements:" + suggestions);
			}
			suggestions.clear(); //empties the arraylist so that the suggestions for the first word don't get shown for the second word as well

			Scanner input = new Scanner(System.in); //scans for user input to check if they want to add their word to the dictionary or replace it with a suggestion
			System.out.println("Would you like to use one of the suggested words or add your word to the dictionary?\nType 'add' to add your word, otherwise type the word you'd like to replace yours with.");
			String answer = input.next().trim(); //the variable "answer" is whatever they type next
			if(answer.equals("add") || answer.equals("Add") || answer.equals("ADD")){
				try{
				 FileWriter writer2 = new FileWriter("localDictionary.txt", true); //if the user types "add", their word gets written
				 writer2.write(misspeltWords.get(i) + "\n");						//to the local dictionary file

				 writer2.close();

			 }catch(IOException help){
				 System.out.println("ERROR");
			 }
			}
			else{ //if they don't type "add", the word they type will replace the spelling mistake in the original file
			try{
				 BufferedReader reader = new BufferedReader(new FileReader(new File("Text.txt")));
				 String line = "";
				 String oldtext = "";
				 while((line = reader.readLine()) != null){ //the buffered reader reads through the text file until it reaches the end
					 oldtext += line + "\r\n";
				 }
				 reader.close();

				 //replace all instances of the misspelt word with whatever replacement the user chose
				 String replacedtext  = oldtext.toLowerCase().replaceAll(misspeltWords.get(i).toLowerCase(), "" + answer);
				 FileWriter writer = new FileWriter("Text.txt");
				 writer.write(replacedtext); //rewrites the file with the new replacement words


				 writer.close();

			 }catch(IOException help){
				 System.out.println("ERROR");
			}
		}
			System.out.println("==========================================");

		}
	}

}