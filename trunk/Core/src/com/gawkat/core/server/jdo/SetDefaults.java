package com.gawkat.core.server.jdo;

import com.gawkat.core.client.SetDefaultsData;
import com.gawkat.core.client.account.thingstuff.ThingStuffData;
import com.gawkat.core.client.account.thingtype.ThingTypeData;
import com.gawkat.core.client.oauth.Sha1;
import com.gawkat.core.server.ServerPersistence;
import com.gawkat.core.server.jdo.data.ThingJdo;
import com.gawkat.core.server.jdo.data.ThingTypeJdo;

public class SetDefaults {

  private ServerPersistence sp = null;
  


  
  public SetDefaults(ServerPersistence sp) {
    this.sp = sp;
  }
  
  public boolean setDefaults(int defaultType) {
    
    if (defaultType == SetDefaultsData.DEFAULT_THING_TYPES) { // thing types
      setThingTypes();
    
    } else if (defaultType == SetDefaultsData.DEFAULT_THING_APPLICATIONS) { // thing
      createApplication();
    
    } else if (defaultType == SetDefaultsData.DEFAULT_THING_USERS) { // thing
      createUser();
      
    } else if (defaultType == SetDefaultsData.DEFAULT_THINGS) { // things
    	 createApplication();
    	 createUser();
    	 
    } else if (defaultType == SetDefaultsData.DEFAULT_THING_STUFF_TYPES) {
    	createStuffTypes();
    }
    
    return true;
  }
  
  private void createStuffTypes() {
  	
  	
  	//ThingStuffData asd = new ThingStuffData(thingId, thingStuffId, thingStuffTypeId, value, valueBol, valueDouble, valueInt)
	  
  }

	/**
   * set default things
   */
  private void setThingTypes() {
    
    ServerPersistence sp = new ServerPersistence();
    
    ThingTypeData at = new ThingTypeData();
    at.setName("Application");
    
    ThingTypeData bt = new ThingTypeData();
    bt.setName("Person");
    
    ThingTypeData ct = new ThingTypeData();
    ct.setName("Group");
    
    ThingTypeData dt = new ThingTypeData();
    dt.setName("Widget");
    
    ThingTypeData et = new ThingTypeData();
    et.setName("Permission");
    
    ThingTypeData ft = new ThingTypeData();
    ft.setName("Thing Link");
    
    ThingTypeData gt = new ThingTypeData();
    gt.setName("Thing Stuff Template");
    
    ThingTypeJdo a = new ThingTypeJdo();
    a.setData(at);
    a.insert();
    
    ThingTypeJdo b = new ThingTypeJdo();
    b.setData(bt);
    b.insert();
    
    ThingTypeJdo c = new ThingTypeJdo();
    c.setData(ct);
    c.insert();
    
    ThingTypeJdo d = new ThingTypeJdo();
    d.setData(dt);
    d.insert();
    
    ThingTypeJdo e = new ThingTypeJdo();
    e.setData(et);
    e.insert();
    
    ThingTypeJdo f = new ThingTypeJdo();
    f.setData(ft);
    f.insert();
    
    ThingTypeJdo g = new ThingTypeJdo();
    g.setData(gt);
    g.insert();
    
  }
  
  /**
   * set default application
   */
  private void createApplication() {
    
    Sha1 sha = new Sha1();
    
    long thingTypeId = ThingTypeData.TYPE_APPLICATION;
    String key = "demo_application";
    String secret = sha.hex_hmac_sha1("salt", "password");
    
    ThingJdo a = new ThingJdo();
    a.insert(thingTypeId, key, secret);
    
  }
  
  /**
   * set a default user for testing
   */
  private void createUser() {
    Sha1 sha = new Sha1();
    
    long thingTypeId = ThingTypeData.TYPE_USER;
    String key = "demo_user";
    String password = "password";
    String secret = sha.b64_sha1(password);
    //String secret = sha.hex_hmac_sha1("salt", "password"); // would need to do this on the client side too.
    
    ThingJdo a = new ThingJdo();
    a.insert(thingTypeId, key, secret);
  }
  
  
}
