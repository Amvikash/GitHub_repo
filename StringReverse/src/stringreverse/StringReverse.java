/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stringreverse;

import java.util.Scanner;

/**
 *
 * @author vikash02.kumar
 */
public class StringReverse {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         System.out.println("enter the string");
        Scanner sc =new Scanner(System.in);
        String r = sc.nextLine();
        
        String[] words= r.split(" ");
        String rev = "";
       
    for(int i=0;i<words.length;i++){
        String word = words[i];
        String revw= "";
        for(int j = word.length()-1; j>=0; j--){
        revw= revw+ word.charAt(j);
        
    }
        rev=rev+ revw + " " ;
    }
  // System.out.println(rev);
   
   
   StringBuilder input1 = new StringBuilder();

        // append a string into StringBuilder input1
       
        input1.append(rev);
 
        // reverse StringBuilder input1
        input1 = input1.reverse();
       
        // print reversed String
        for (int i=0; i<input1.length(); i++)
            System.out.print(input1.charAt(i));
    }
   
   
   
   }

