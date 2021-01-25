/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordaday;

import java.util.HashMap;
import java.sql.*; // Using 'Connection', 'Statement' and 'ResultSet' classes in java.sql package

// The Absolute Full-path Filename for this JAR file is
//"C:\myWebProject\mysql-connector-java-8.0.{xx}\mysql-connector-java-8.0.{xx}.jar".
// C:\Users\Thanuja\Documents\NetBeansProjects\WordADay\mysql-connector-java-8.0.22

/**
 *
 * @author user1
 * 
 * Handles changes in the MySQL database
 */

public class WordStorage {

    HashMap<String, String> words = new HashMap<>();
    int length;

    String currWord = "";
    String currDef = "";

    // IMPROVE: add to one big group with bit if used and another bit if saved
    HashMap<String, String> usedWords = new HashMap<>();
    HashMap<String, String> savedWords = new HashMap<>();
    HashMap<String, String> newWords = new HashMap<>();

    //boolean full = false;

    private String password = ""; // input password here

    // instructor, similar to a getter
    public WordStorage() {
        // IMPROVE: increase length and store in file/database, but web scraping is better

        /* Originally Had 
        words.put("Exonerate", "to clear, as of an accusation; free from guilt or blame.");
        words.put("Ruthful", "compassionate or sorrowful.");
        words.put("Self-made", "set to be the youngest-ever self-made billionaire.");*/
        
        try {
            //Class.forName("com.mysql.jdbc.Driver"); // works without this
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306", "root", password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from world.words");

            while (rs.next()) {
                words.put(rs.getString("name"), rs.getString("def"));

                // need to update other files to save as bit
                if (rs.getBoolean("used")) {
                    usedWords.put(rs.getString("name"), rs.getString("def"));

                    // can only be saved if it was used
                    if (rs.getBoolean("saved")) {
                        savedWords.put(rs.getString("name"), rs.getString("def"));
                    }
                }else{
                    newWords.put(rs.getString("name"), rs.getString("def"));
                }

            }
            con.close();

        } catch (Exception e) {
            System.out.println("sql ERROR");
            System.out.println(e);
        }

        length = words.size();

    }
    
    
    // similar to a setter
    public void addUsed(String updateWord, Boolean save) { // http://alvinalexander.com/java/java-mysql-update-query-example/
        try {
            //Class.forName("com.mysql.jdbc.Driver"); // works without this
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306", "root", password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from world.words");

            String query = "UPDATE world.words SET used = ? WHERE name = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setBoolean(1, true);
            preparedStmt.setString(2, updateWord);
            preparedStmt.executeUpdate();

            if (save) {
                query = "UPDATE world.words SET saved = ? WHERE name = ?";
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setBoolean(1, true);
                preparedStmt.setString(2, updateWord);
                preparedStmt.executeUpdate();
            }

            con.close();
            
        } catch (Exception e) {
            System.out.println("sql ERROR");
            System.out.println(e);
        }

    }

    // https://stackoverflow.com/questions/15430009/mysql-change-all-values-in-a-column
    // https://stackoverflow.com/questions/11448068/mysql-error-code-1175-during-update-in-mysql-workbench
    
    // similar to a setter
    public void reset() {
        try {
            //Class.forName("com.mysql.jdbc.Driver"); // works without this
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306", "root", password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from world.words");

            String query = "SET SQL_SAFE_UPDATES = 0";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.executeUpdate();

            query = "UPDATE world.words " + "SET used = 0 " + "WHERE used = 1 " + "AND NOT isnull( used )";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.executeUpdate();

            query = "UPDATE world.words " + "SET saved = 0 " + "WHERE saved = 1 " + "AND NOT isnull( saved )";
            preparedStmt = con.prepareStatement(query);
            preparedStmt.executeUpdate();

            con.close();

            usedWords = new HashMap<>();
            savedWords = new HashMap<>();
            newWords = words;
                        
        } catch (Exception e) {
            System.out.println("sql ERROR");
            System.out.println(e);
        }

    }


}

/*
try to merge the setup and closing of each?


https://www3.ntu.edu.sg/home/ehchua/programming/java/JDBC_Basic.html
https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15
https://www.javatpoint.com/example-to-connect-to-the-mysql-database

https://www.codejava.net/java-se/jdbc/jdbc-tutorial-sql-insert-select-update-and-delete-examples
 */
