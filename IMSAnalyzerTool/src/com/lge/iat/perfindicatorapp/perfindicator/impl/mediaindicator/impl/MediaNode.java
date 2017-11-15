/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl;

import com.lge.iat.utils.CallSession;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces.IMediaNode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class MediaNode implements IMediaNode{
    TableView<Integer> table;
    List<String> nameList  = new ArrayList<>(); 
    List<String> numberList  = new ArrayList<>();
    List<String> dateList  = new ArrayList<>();
    List<String> timeList  = new ArrayList<>();
    List<Integer> dirList  = new ArrayList<>();
    ArrayList<CallSession> mCallSessionList = null;
    INodeChangeListener mNodeChangeListener = null;
    public MediaNode(INodeChangeListener nodeChangeListener, ArrayList<CallSession> callSessionList, Stage stage,String dbPath) {
        mNodeChangeListener = nodeChangeListener;
        mCallSessionList = callSessionList;
    }
    @Override
    public Node createNode() {
        runSQL();
        table = new TableView<>();
        for (int i = 0; i < mCallSessionList.size(); i++) {
           CallSession cs = mCallSessionList.get(i);
           int callID = cs.getId();
           
            table.getItems().add(i);
        }
        
        TableColumn<Integer, String> nam = new TableColumn<>("Name");
        nam.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(nameList.get(rowIndex));
        });
        TableColumn<Integer, String> num = new TableColumn<>("Number");
        num.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(numberList.get(rowIndex));
        });
        TableColumn<Integer, String> dat = new TableColumn<>("Date");
        dat.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(dateList.get(rowIndex));
        });
        TableColumn<Integer, String> tim = new TableColumn<>("Time");
        tim.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(timeList.get(rowIndex));
        });
        
        TableColumn<Integer, String> dir = new TableColumn<>("Direction");
        dir.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            int direction = dirList.get(rowIndex);
            String strDir = null;
            if(direction == 1) {
                strDir = "Incoming";
            } else if(direction == 2) {
                strDir = "Outgoing";
            } else {
                strDir = "";
            }
            return new ReadOnlyStringWrapper(strDir);
        });
        
        nam.setMinWidth(130);
        num.setMinWidth(130);
        dat.setMinWidth(170);
        tim.setMinWidth(170);
        table.getColumns().add(nam);
        table.getColumns().add(num);
        table.getColumns().add(dat);
        table.getColumns().add(tim);
        table.getColumns().add(dir);
        table.setEditable(false);
        table.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TablePosition> c1) -> {
          //int i = table.getSelectionModel().getSelectedItem();
         // FStyle(media,i);
         int i = table.getSelectionModel().getSelectedItem();
         mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.CallMedia,i);
      });
        return table;
    }
    
     public void runSQL(){
         for (int i = 0 ; i< mCallSessionList.size(); i++) {
             CallSession callSession = mCallSessionList.get(i);
             if(callSession.getDirection() == 1) {
                nameList.add(callSession.getCallerName());
                numberList.add(callSession.getOrgnNum());
             } else {
                nameList.add(callSession.getCalleeName()); 
                 numberList.add(callSession.getTermNum());
             }
             String dateTime = callSession.getTime();
             String [] dateOrTime = dateTime.split(" ");
             dateList.add(dateOrTime[0]);
             timeList.add(dateOrTime[1]);
             dirList.add(callSession.getDirection());
         }
    }
}
