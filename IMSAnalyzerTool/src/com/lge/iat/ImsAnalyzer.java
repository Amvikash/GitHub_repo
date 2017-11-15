
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat;


import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.filepicker.IFilePickerDialogListener;
import com.lge.iat.filepicker.FilePickerDialog;
import com.lge.iat.perfindicatorapp.PerformanceIndicatorFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JFrame;

public class ImsAnalyzer extends Application implements IFilePickerDialogListener{
        
    static JFrame mJFrame = null;
     Stage mPrimaryStage  =   null;
  public static void ImsAnalyzer(String[] args) {
      launch(args);
      
  }
    private TextArea selectTextArea;
  @Override
  public void start(Stage primaryStage) {
      mPrimaryStage = primaryStage;
      primaryStage.setTitle("IMS Analyzer");
      primaryStage.setMinHeight(600);
      primaryStage.setMinWidth(600);
      Group root  = new Group();
      Scene scene = new Scene(root, 600, 600, Color.LIGHTGREY);
      
      Text title =  new Text("IMS Analyzer Tool");
      selectTextArea =  new TextArea("NO DB Selected");
      selectTextArea.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
      selectTextArea.setPrefSize(400,20);
      selectTextArea.setPrefRowCount(1);
      title.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
      
      Image img = new Image("resources/photo.jpg");
      ImageView iv1 = new ImageView(img);
      iv1.setFitHeight(100);
      iv1.setFitWidth(100);
      
      Text lgSoftLab = new Text("India Software Lab");
      lgSoftLab.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
      
      Button select =   new Button("Select");
      select.setPrefSize(100, 40);
      GridPane g1 = new GridPane();
      GridPane g2 = new GridPane();
      g1.setHgap(10);
      g1.setVgap(15);
      g2.setHgap(5);
      g2.setVgap(5);
      GridPane g3 = new GridPane();
      g3.setHgap(10);
      g3.setVgap(15);
      
      select.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
                FilePickerDialog.createFilePickerDialog(ImsAnalyzer.this);
          }
      });
      
      BorderPane mainPane = new BorderPane();
      Text selectHintLable = new Text("Please Select DB to proceed :");
      selectHintLable.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
      
      g1.add(iv1, 6, 2);
      g1.add(lgSoftLab,7, 2);
      g2.add(title, 25, 25);
      GridPane.setHalignment(title, HPos.CENTER);
      g3.add(selectHintLable, 6, 13);
      g3.add(selectTextArea,6,14);
      g3.add(select, 8, 14);
      
      VBox layout = new VBox();
      layout.getChildren().addAll(g1,g2,g3);
        
      mainPane.setCenter(layout);
      root.getChildren().add(mainPane);
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
 
    }

   @Override
    public void OnDBFileSelected(String dbPath) {
        selectTextArea.setText(dbPath);
        IMSAnalyzerDBManager.CreateDBHelper(dbPath);
        Stage s = new Stage();
        PerformanceIndicatorFrame j = new PerformanceIndicatorFrame(dbPath);
        try {
            j.start(s);
        } catch (Exception ex) {
            Logger.getLogger(ImsAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }