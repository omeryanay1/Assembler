import java.util.HashMap;

public class SymbolTable extends HashMap<String, Integer>{

    // put all the default variables in the table
    public SymbolTable(){
        String key;
        for(int i=0; i<16; i++){
            key = "R"+i;
            this.put(key,i);
        }
        this.put("SCREEN", 16384);
        this.put("KBD", 24576);
        this.put("SP", 0);
        this.put("LCL", 1);
        this.put("ARG", 2);
        this.put("THIS", 3);
        this.put("THAT", 4);
    }

    //add a new variable to the table
    public void addEntry(String symbol, int adress){
        this.put(symbol, (Integer)adress);
    }

   // check if the symbole contain in the table 
    public boolean contain(String symbol){
        if(this.containsKey(symbol)) return true;
        return false;
    }

    public int getAddress(String symbol){
        return this.get(symbol);
    }
}
