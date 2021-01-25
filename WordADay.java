/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordaday;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author user1
 */


/* Fix for proper OOP
don't allow object to change here, but in getter/setter methods
*/

public class WordADay implements ActionListener { // ActionListener for buttons

    /**
     * @param args the command line arguments
     */
    static String getPair(WordStorage newobj) { // returns new word
        // error checking
        // IMPROVE: move to getpair, but problem with object newobj not being recognized?
        Random generator = new Random();
        Object[] values = newobj.newWords.keySet().toArray();

        // OR
        // int randomNum = (int) (Math.random() * newobj.words.size());  // 0 to 100
        // could hypothetically have large run time if many items in list, so have new hash map as unused
        // do while since first time usedWords is null 
        //do {
        Object randomWord = values[generator.nextInt(values.length)];
        newobj.currWord = (String) values[generator.nextInt(values.length)];
        //System.out.println("add"); // only happens once since guarunteed to be new
        //} while (newobj.usedWords.get(newobj.currWord) != null); // want to exit when word chosen isn't there yet

        newobj.currDef = newobj.words.get(newobj.currWord);
        newobj.newWords.remove(newobj.currWord);

        return newobj.currWord;
    }

    // need to improve overloading, have additional parameter to not cause error
    static String display(HashMap<String, String> wordsToPrint) { // wordsToPrint seems to be passed as a pointer

        HashMap<String, String> copy = wordsToPrint;

        String text = "";
        for (String i : copy.keySet()) {
            text += i + ": " + copy.get(i) + "\n";
        }
        
        return text;
    }

    public static void main(String[] args) {

        System.out.println("WordADay\n");
        WordStorage newobj = new WordStorage();

        

        String userInput = "";
        boolean quit = false;
        Scanner scan = new Scanner(System.in);

        String text = "";
        String cont;

        // mainly for testing, would ideally only have GUI and remove this in final code
        while (!quit) {

            String initialText = "Generate (G), View All(V), View Used (U), View Saved (S), Quit(Q), Reset (R)\n";
            System.out.println("\n" + initialText);

            userInput = scan.nextLine();

            switch (userInput) {
                case "G": // move to generate button

                    if (newobj.words.size() == newobj.usedWords.size()) { // ideally want large enough database so that this is unlikely
                        text = "No more words to choose\nPlease reset";
                        System.out.println(text);
                        //newobj.full = true;
                        break;
                    }

                    newobj.currWord = getPair(newobj);

                    text = newobj.currWord + ": \"" + newobj.currDef + "\"\n" + "Want to save? Yes (Y), No (Any other input)";
                    System.out.println(text);

                    newobj.usedWords.put(newobj.currWord, newobj.currDef);

                    // IMPROVE: very basic approach, ideally have a button or error checking
                    String saving = scan.nextLine();

                    if (saving.equals("Y")) {
                        newobj.savedWords.put(newobj.currWord, newobj.currDef);
                        newobj.addUsed(newobj.currWord, true);
                    } else {
                        newobj.addUsed(newobj.currWord, false);
                    }

                    System.out.println("");

                    break;

                case "V":

                    text = display(newobj.words);
                    text += "Press any key to continue in display";
                    System.out.println(text);

                    cont = scan.nextLine();
                    System.out.println("");

                    break;
                case "U":

                    text = display(newobj.usedWords);
                    text += "Press any key to continue in display";
                    System.out.println(text);

                    cont = scan.nextLine();

                    break;

                case "S":

                    text = display(newobj.savedWords);
                    text += "Press any key to continue in display";
                    System.out.println(text);

                    cont = scan.nextLine();

                    break;

                case "R":
                    newobj.reset();
                    break;

                case "Q":
                    System.out.println("Goodbye");
                    quit = true; // not the best, but it doesn't affect the actual DB
                    break;
                default:
                    System.out.println("Invalid");
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

