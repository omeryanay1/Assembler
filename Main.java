import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //Opens the input file (Prog.asm),
        //and gets ready to process it
        //Constructs a symbol table,
        //and adds to it all the predefined symbols
       if(args.length > 0){
            
            String fileName = args[0];
            String currentDirectory = System.getProperty("user.dir");
            File inputFile = new File(currentDirectory+File.separator+fileName);
            File[] files;
            if(inputFile.isDirectory()){
                System.setProperty("user.dir", currentDirectory + File.separator + fileName);
                files = inputFile.listFiles();
            }
            else{
                files = new File[]{inputFile};
            }
            for(int i=0;i<files.length;i++){
                String currentPath = files[i].getAbsolutePath();
                File file = new File(currentPath);
                if(file.getAbsolutePath().endsWith(".asm")){
                    SymbolTable symbolTable = new SymbolTable();

                    //Reads the program lines, one by one,
                    //focusing only on (label) declarations.
                    //Adds the found labels to the symbol table
                    Parser parser1 = new Parser(file);
                    String lable;
                    int lineCount = 0;
                    while(parser1.hasMoreLines()){
                        parser1.advance();
                        lineCount ++;
                        if(parser1.instructionType() == "L_INSTRUCTION"){
                            lineCount--;
                            lable = parser1.symbol();
                            symbolTable.addEntry(lable, lineCount);
                        }
                            
                    }
                    try{
                        // Extract the prefix of the input file name
                        String currentFilename;
                        if(inputFile.isDirectory()){
                            currentFilename = file.getAbsolutePath();
                        }
                        else{
                            currentFilename = fileName;
                        }
                        String inputFileNameWithoutExtension = currentFilename.replace(".asm", "");
    
                        // Output file name
                        File outputFile = new File(inputFileNameWithoutExtension + ".hack");
                        if(!outputFile.exists()){
                            outputFile.createNewFile();
                        }
                        outputFile.setWritable(true);
                        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
    
                            //(starts again from the beginning of the file)
                            //While there are more lines to process:
                            //Gets the next instruction, and parses it
                            Parser parser2 = new Parser(file);
                            int tableValue = 16, value;
                            String symbol, dest, comp, jump, binaryString = "";
                            while(parser2.hasMoreLines()){
                                parser2.advance();
                                //If the instruction is @ symbol
                                //If symbol is not in the symbol table, adds <symbol , value> to the table, and
                                //translates value to its binary value
                                if(parser2.instructionType() == "A_INSTRUCTION"){
                                    symbol = parser2.symbol();
                                    if(!symbolTable.contain(symbol)){
                                        if(isNumeric(symbol)){
                                            symbolTable.addEntry(symbol, Integer.parseInt(symbol));
                                        }
                                        else{
                                            symbolTable.addEntry(symbol, tableValue);
                                            tableValue ++;
                                        }
                                    } 
                                    value = symbolTable.getAddress(symbol);
                                    binaryString = Integer.toBinaryString(value);
                                    while(binaryString.length() < 16){
                                        binaryString = "0" + binaryString;
                                    }
                                    writer.write(binaryString);
                                    writer.newLine();
                                }
                                else if(parser2.instructionType() == "C_INSTRUCTION"){
                                    dest = parser2.dest();
                                    comp = parser2.comp();
                                    jump = parser2.jump();
                                    binaryString = "111" + Code.compTable.get(comp) + Code.destTable.get(dest) + Code.jumpTable.get(jump);
                                    writer.write(binaryString);
                                    writer.newLine();
                                }
                            }
                            writer.close();
                            
                        
    
                }
                catch (IOException e) {
                // Handle any IOException if necessary
                System.out.println("error1");
                }   
                }

            }   
        }    
    }
    // check if string is valid int index
    public static boolean isNumeric(String st){
        try {
            int index = Integer.parseInt(st);
            return index >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
