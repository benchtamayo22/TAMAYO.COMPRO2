package com.bench;

//import org.mindrot.jbcrypt.BCrypt;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String password = "i love java";
        String hashed = Bcrypt.hashpw(password, Bcrypt.gensalt(12));
        System.out.println(hashed);

        String loginPassword = "i love java";
        if(Bcrypt.checkpw(loginPassword, hashed)){
            System.out.println();
        } else {
            System.out.println();
        }
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
    }
}