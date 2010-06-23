package org.gonevertical.core.client.ui.admin.thingstufftype;

import org.gonevertical.core.client.ClientPersistence;
import org.gonevertical.core.client.global.DeleteDialog;
import org.gonevertical.core.client.global.EventManager;
import org.gonevertical.core.client.global.Global_ListBox;
import org.gonevertical.core.client.rpc.RpcCore;
import org.gonevertical.core.client.rpc.RpcCoreServiceAsync;
import org.gonevertical.core.client.ui.Row;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ThingStuffType extends Composite implements ChangeHandler, ClickHandler {

  private ClientPersistence cp = null;
  
  private RpcCoreServiceAsync rpc = null;
  
  private DeleteDialog wdelete = new DeleteDialog();
  
  private Row pWidget = new Row();
  
  private FlowPanel pCount = new FlowPanel();
 
  private HorizontalPanel hpName = new HorizontalPanel();
  private FlowPanel fpName = new FlowPanel();
  private TextBox tbName = new TextBox();
  
  private HorizontalPanel hpDelete = new HorizontalPanel();
  private PushButton bDelete = new PushButton("X");
   
  private ThingStuffTypeData thingStuffTypeData = null;
  
  private ListBox lbValueType = new ListBox();
  
  private boolean edit = false;
  
  private int row = 0;
  
  private int changeEvent = 0;
  
  public ThingStuffType(ClientPersistence cp) {
    this.cp = cp;
    
    hpName.add(tbName);
    hpName.add(fpName);
    
    hpDelete.add(bDelete);
    
    initWidget(pWidget);
    
    tbName.setVisible(false);
    fpName.setVisible(true);
    
    pWidget.addChangeHandler(this);
    bDelete.addClickHandler(this);
    
    rpc = RpcCore.initRpc();
  }

  public void setData(int row, ThingStuffTypeData thingStuffTypeData) {
    this.row = row;
    this.thingStuffTypeData = thingStuffTypeData;
    pWidget.setRow(row);
    draw();
  }
  
  private void draw() {
    
    pWidget.clear();
    fpName.clear();
    
    // C0: count
    pCount.add(new HTML(Integer.toString(row)));
    
    // C1: Id
    HTML pId = new HTML(Long.toString(thingStuffTypeData.getId()));
    
    // C2: name
    String name = thingStuffTypeData.getName();
    fpName.add(new HTML(name));
    
    // C3: value type
    
    // C4: delete button
    
    pWidget.add(pCount);
    pWidget.add(pId);
    pWidget.add(hpName);
    pWidget.add(lbValueType);
    pWidget.add(hpDelete);
   
    drawLb();
  }
  
  private void drawLb() {
  
  	lbValueType.addItem("Link", Integer.toString(ThingStuffTypeData.VT_LINK));
  	
  	lbValueType.addItem("Text", Integer.toString(ThingStuffTypeData.VT_STRING));
    lbValueType.addItem("Checkbox", Integer.toString(ThingStuffTypeData.VT_BOOLEAN));
    lbValueType.addItem("Decimal", Integer.toString(ThingStuffTypeData.VT_STRING));
    lbValueType.addItem("Number", Integer.toString(ThingStuffTypeData.VT_INT));
    
    lbValueType.addItem("Large Text Box", Integer.toString(ThingStuffTypeData.VT_STRING_LARGE));
    lbValueType.addItem("Text Case Sensitive", Integer.toString(ThingStuffTypeData.VT_STRING_CASE));
    lbValueType.addItem("Large Text Area Case Sensitive", Integer.toString(ThingStuffTypeData.VT_STRING_LARGE_CASE));
    
    lbValueType.addItem("HTML", Integer.toString(ThingStuffTypeData.VT_HTML));
    lbValueType.addItem("URL", Integer.toString(ThingStuffTypeData.VT_URL));
    lbValueType.addItem("Email", Integer.toString(ThingStuffTypeData.VT_EMAIL));
    lbValueType.addItem("Phone", Integer.toString(ThingStuffTypeData.VT_PHONE));
    lbValueType.addItem("Money", Integer.toString(ThingStuffTypeData.VT_STRING));
    
    lbValueType.addItem("File", Integer.toString(ThingStuffTypeData.VT_FILE));
    
    Global_ListBox.setSelected(lbValueType, thingStuffTypeData.getValueTypeId());
  }
  
  public Row getRow() {
    return pWidget;
  }
  
  public void edit(boolean b) {
    if (b == true) {
      tbName.setText(thingStuffTypeData.getName());
      tbName.setVisible(true);
      fpName.setVisible(false);
    } else if (b == false) {
      String name = tbName.getText();
      thingStuffTypeData.setName(name);
      fpName.clear();
      fpName.add(new HTML(name));
      tbName.setVisible(false);
    }
  }

  public ThingStuffTypeData getData() {
    thingStuffTypeData.setName(tbName.getText().trim());
    thingStuffTypeData.setValueTypeId(Global_ListBox.getSelectedValue(lbValueType));
    return thingStuffTypeData;
  }
  
  private void delete() {
    wdelete.center();
    wdelete.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        DeleteDialog dd = (DeleteDialog) event.getSource();
        int changeEvent = dd.getChangeEvent();
        if (changeEvent == EventManager.DELETE_YES && thingStuffTypeData.getStuffTypeId() > 0) {
          deleteRpc();
        } else {
          //deleteIt(false);
        }
      }
    });
  }
  
  private void deleteIt(boolean b) {
    if (b == true) {
      this.removeFromParent();
    } else {
      Window.alert("Wasn't able to delete thingType. Please try again?");
    }
  }
  
  public void onChange(ChangeEvent event) {
    Widget sender = (Widget) event.getSource();
    if (sender == pWidget) {
      int changeEvent = pWidget.getChangeEvent();
      if (changeEvent == EventManager.ROW_OVER) {
        edit(true);
        fireChange(changeEvent);
      } else if (changeEvent == EventManager.ROW_OUT) {
        edit(false);
        fireChange(changeEvent);
      }
    }
    
  }
  
  public int getChangeEvent() {
    return changeEvent;
  }
  
  private void fireChange(int changeEvent) {
    this.changeEvent = changeEvent;
    NativeEvent nativeEvent = Document.get().createChangeEvent();
    ChangeEvent.fireNativeEvent(nativeEvent, this);
  }
  
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  public void onClick(ClickEvent event) {
  
    Widget sender = (Widget) event.getSource();
    if (sender == bDelete) {
      delete();
    }
    
  }
  
  private void deleteRpc() {
   
    rpc.deleteThingStuffType(cp.getAccessToken(), thingStuffTypeData, new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean b) {
        deleteIt(b);
      }
      public void onFailure(Throwable caught) { 
      	cp.setRpcFailure(caught);
      }
    });
    
  }

}