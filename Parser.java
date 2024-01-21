import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser{

   private BufferedReader reader;
   private String instruction;
    // Opens the input file/stream and gets ready to parse it.
   public Parser(File f){
        try{
        this.reader = new BufferedReader(new FileReader(f), (int)f.length());
        }
        catch (FileNotFoundException e) {
            // Handle any IOException if necessary
            System.out.println("File not found: " + f.getAbsolutePath());
            this.reader = null;
        }
        this.instruction = "";
   }

   //Are ther more lines in the input?
   public boolean hasMoreLines() {
    try {
        reader.mark(4096); // Set a mark with a reasonably large readlimit
        if(reader.read() != -1){
            this.reader.reset();
            return true;
        }
        return false;
    } catch (IOException e) {
        // Handle any IOException if necessary
        System.out.println("Error: " + e.getMessage());
        return false;
    }
}

    //Skips over whitwspace and comments, if necessary. Reads the next instruction from the input, and makes it the current instruction.
    //Initially there is no current instruction.
    public void advance(){
        if(this.hasMoreLines()){
            try{
                this.instruction = reader.readLine();
                while(instruction.indexOf('/') != -1 || instruction.isEmpty()){
                    instruction = reader.readLine();
                }
            }
            catch (IOException e) {
                // Handle any IOException if necessary
                System.out.println("error1");
            }
        }
    }

    // return type of current instruction
    //A_INSTRUCTION for @xxx, where xxx is either a decimal number or a symbol
    //C_INSTRUCTION for dest=comp;jump
    //L_INSTRUCTION for (xxx), where xxx is a symbol
    public String instructionType(){
            if(instruction.indexOf('(') != -1) return "L_INSTRUCTION";
            if(instruction.indexOf('@') != -1) return "A_INSTRUCTION";
            return "C_INSTRUCTION";
    }

    //if the current instruction is (xxx), returns the symbol xxx.if the current instruction
    //is @xxx, returns thr symbol or decimal xxx(as a string).
    public String symbol(){
            if(this.instructionType() == "L_INSTRUCTION"){
                return instruction.substring(instruction.indexOf('(') +1, instruction.indexOf(')'));
            }
            else{
                int i =instruction.indexOf('@') + 1;
                while(i != instruction.length()  && instruction.charAt(i) != ' '){
                    i++;
                }
                return instruction.substring(instruction.indexOf('@')+1, i);
            }
        }

    //Returns the symbolic dest part of the current c-instruction
    public String dest(){
            int start = 0;
            int index = this.instruction.indexOf('=');
            int Aindex = this.instruction.lastIndexOf('A', index);
            int Dindex = this.instruction.lastIndexOf('D', index);
            int Mindex = this.instruction.lastIndexOf('M', index);
            if (Aindex != -1) start = Aindex;
            else if (Dindex != -1 && Mindex != -1) start = Math.min(Dindex, Mindex);
            else if (Dindex != -1) start = Dindex;
            else if (Mindex != -1) start = Mindex;
            else return "null";
            return this.instruction.substring(start, index);
    }

    // Returns the symbolic comp part of the current c-instruction
    public String comp() {
        int index1 = this.instruction.indexOf('=');
        int index2 = this.instruction.indexOf(';');
        if (index1 != -1 && index2 != -1) return this.instruction.substring(index1 + 1, index2);
        else if (index1 != -1) return this.instruction.substring(index1 + 1);
        else return this.instruction.substring(index2 - 1, index2);
    }

    // Returns the symbolic jump part of the current c-instruction
    public String jump() {
        int index = this.instruction.indexOf(';');
        if (index != -1){
            int i =instruction.indexOf(';') + 1;
            while(i != instruction.length()  && instruction.charAt(i) != ' '){
                i++;
            }
            return this.instruction.substring(index+1, i);
        } 
        return "null";
    }

}