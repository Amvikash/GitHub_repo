package com.lge.iat.filepicker;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jitendra.parkar
 */
public class FilePickerDialog {
      public static void createFilePickerDialog(IFilePickerDialogListener listener) {
             Stage s   =   new Stage(); 
             FileChooser fileChooser = new FileChooser();
             fileChooser.setTitle("Open Resource File");
             fileChooser.getExtensionFilters().addAll(
                      new FileChooser.ExtensionFilter("DB", "*.db") //change to your database file extension
                    );
              File file = fileChooser.showOpenDialog(s);
              if((file == null)){  
                  System.out.println("WARNING : Pleasr Select a File");
              }else{
                  listener.OnDBFileSelected(file.getAbsolutePath());
              }
        
    }
}
