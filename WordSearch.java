import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
public class WordSearch{
    private char[][]data;
    private int seed;
    private Random randgen;
    private ArrayList<String>wordsToAdd;
    private ArrayList<String>wordsAdded;
    /**Initialize the grid to the size specified
     *and fill all of the positions with '_'
     *@param rows is the starting height of the WordSearch
     *@param cols is the starting width of the WordSearch
     */
    public static void main(String[] args){
      if(args.length<3){
        System.out.println("explain this");
      }
      else if(args.length==3){
        int givenSeed = (int)(Math.random()*10000);
        WordSearch WS = new WordSearch(Integer.parseInt(args[0]),
                   Integer.parseInt(args[1]),
                   args[2],
                   givenSeed);
        WS.fillInLetters();
        System.out.println(WS);
      }
      else{
        WordSearch WS = new WordSearch(Integer.parseInt(args[0]),
                   Integer.parseInt(args[1]),
                   args[2],
                   Integer.parseInt(args[3]));
        if(args.length==4||!args[4].equals("key")){
          WS.fillInLetters();
        }
        else{
          WS.cleanUnderscores();
        }
        System.out.println(WS);
      }
    }
    public WordSearch(int rows, int cols, String fileName, int randSeed){
      wordsToAdd = new ArrayList<String>();
      wordsAdded = new ArrayList<String>();
      seed = randSeed;
      randgen = new Random(seed);
      try{
        File f = new File(fileName);
        Scanner s = new Scanner(f);
        while(s.hasNext()){
          wordsToAdd.add(s.next().toUpperCase());
        }
      }catch(FileNotFoundException e){
        System.out.println("File not found: "+fileName);
        e.printStackTrace();
        System.exit(0);
      }
      data = new char[rows][cols];
      clear();
      addAllWords();
    }
    private void fillInLetters(){
      for(int i=0;i<data.length;i++){
        for(int j=0;j<data[i].length;j++){
          if(data[i][j]=='_'){
            data[i][j]=(char)(65+Math.abs(randgen.nextInt())%26);
          }
        }
      }
    }
    private void cleanUnderscores(){
      for(int i=0;i<data.length;i++){
        for(int j=0;j<data[i].length;j++){
          if(data[i][j]=='_'){
            data[i][j]=' ';
          }
        }
      }
    }
    private boolean addWord(String word, int r, int c, int rowIncrement, int colIncrement){
      if(r+word.length()*rowIncrement>data.length||
         r+word.length()*rowIncrement<-1||
         c+word.length()*colIncrement>data[0].length||
         c+word.length()*colIncrement<-1||
         (rowIncrement==colIncrement&&rowIncrement==0)){
           return false;
      }
      for(int i=0;i<word.length();i++){
        if(data[r+i*rowIncrement][c+i*colIncrement]!='_'&&data[r+i*rowIncrement][c+i*colIncrement]
        !=word.charAt(i)){
          return false;
        }
      }
      for(int i=0;i<word.length();i++){
        data[r+i*rowIncrement][c+i*colIncrement]=word.charAt(i);
      }
      return true;
    }
    private void addAllWords(){
      int tries = 0;
      int fails = 0;
      while(wordsToAdd.size()>0&&fails<data.length*data[0].length){
        String word = wordsToAdd.get(Math.abs(randgen.nextInt())%wordsToAdd.size());
        int ri = Math.abs(randgen.nextInt())%3-1;
        int ci = Math.abs(randgen.nextInt())%3-1;
        while(tries<data.length*data[0].length){
          if(!addWord(word,Math.abs(randgen.nextInt())%data.length,
                      Math.abs(randgen.nextInt())%data[0].length,
                      ri, ci))
                  {
                    tries++;
                  }
          else{
            wordsAdded.add(word);
            wordsToAdd.remove(word);
            tries=data.length*data[0].length;
            fails=0;
          }
        }
        tries=0;
        fails++;
      }
    }


    /**Set all values in the WordSearch to underscores'_'*/
    private void clear(){
      for(int i=0;i<data.length;i++){
        for(int j=0;j<data[i].length;j++){
          data[i][j]='_';
        }
      }
    }

    /**Each row is a new line, there is a space between each letter
     *@return a String with each character separated by spaces, and rows
     *separated by newlines.
     */
    public String toString(){
      String ans = "";
      for(int i=0;i<data.length;i++){
        ans+="|";
        for(int j=0;j<data[i].length;j++){
          ans+=data[i][j];
          if(j<data[i].length-1){
            ans+=" ";
          }
        }

        ans+="|\n";
      }
      ans+="Words:";
      for(int i=0;i<wordsAdded.size();i++){
        ans+=wordsAdded.get(i) + " ";
      }
      ans+="(seed: "+seed+")";
      return ans;
    }


}
