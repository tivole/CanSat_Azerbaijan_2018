import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Ti_GSW_Mark2 extends PApplet {

// (c) Tivole




// Varailable initialization
Serial mySerial;
ControlP5 cp5;

PFont font;
boolean isFirst = true;
String telemetry[] = new String[13], TLM_GpsTime = "21.07.2018.02:30:12", com = " ";
int strkclr = 180, y = 0, m = 0, d = 0, h = 0, mi = 0, se = 0, StageOfSatellite = 0, isNewData = 0, TLM_TeamID = 0, TLM_MissionTime = 0, TLM_PacketCount = 0, TLM_GpsSatellites = 0;
float TLM_Altitude = 0, WIDTH, HEIGHT, GOLD, TLM_Pressure = 90000, TLM_Temperature = 5, TLM_Voltage = 0, TLM_GpsLatitude = 0, TLM_GpsLongtitude = 0, TLM_GpsSpeed = 0, TLM_Humidity = 0;
PImage MSUFO_Logo;
Table TLM_Table;
TableRow newRow;


// Initialization Graphs
Ti_Graph GRPH_Altitude;
Ti_Graph GRPH_Pressure;
Ti_Graph GRPH_Temperature;
Ti_Graph GRPH_Voltage;
Ti_Graph GRPH_Humidity;
Ti_Graph GRPH_Speed;

// Initialization Battery
Ti_Battery Battery;


public void setup() {
  // Window size
  
  background(0);
  surface.setTitle("Ground Station | MS_U_FO");
  
  WIDTH = PApplet.parseFloat(width);
  HEIGHT = PApplet.parseFloat(height);
  GOLD = WIDTH/1000;

  // Serial port connection
  mySerial = new Serial(this, Serial.list()[0], 9600);

  // Command line
  font = createFont("arial", PApplet.parseInt(ceil(11.25f*GOLD)));
  cp5 = new ControlP5(this);
  cp5.addTextfield("Command line").setPosition(PApplet.parseInt(ceil(0.804f*WIDTH)), PApplet.parseInt(ceil(0.106f*HEIGHT))).setSize(PApplet.parseInt(ceil(0.1822f*WIDTH)), PApplet.parseInt(ceil(0.055555f*HEIGHT))).setFont(font).setAutoClear(true);
  cp5.addBang("Submit").setPosition(PApplet.parseInt(ceil(0.804f*WIDTH)) - 1, PApplet.parseInt(ceil(0.162f*HEIGHT))).setSize(PApplet.parseInt(ceil(0.18333f*WIDTH)), PApplet.parseInt(ceil(0.037f*HEIGHT))).getCaptionLabel().setFont(createFont("arial", PApplet.parseInt(ceil(9.0f*GOLD)))).align(cp5.CENTER, cp5.CENTER);

  // Writing the Table
  TLM_Table = new Table();
  TLM_Table.addColumn("Team Id");
  TLM_Table.addColumn("Mission Time");
  TLM_Table.addColumn("Packet Count");
  TLM_Table.addColumn("Altitude");
  TLM_Table.addColumn("Pressure");
  TLM_Table.addColumn("Temperature");
  TLM_Table.addColumn("Voltage");
  TLM_Table.addColumn("GPS Time");
  TLM_Table.addColumn("GPS Latitude");
  TLM_Table.addColumn("GPS Longtitude");
  TLM_Table.addColumn("Speed");
  TLM_Table.addColumn("GPS Satellites");
  TLM_Table.addColumn("Humidity");
 
  // Initialization Graphs
  GRPH_Altitude = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.013f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "ALTITUDE", 430, 0, 255, 0, 255, "m");
  GRPH_Pressure = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.013f)), PApplet.parseInt(ceil(HEIGHT*0.3009f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "PRESSURE", 130000, 90000, 0, 255, 131, "Pa");
  GRPH_Temperature = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.222f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "TEMPERATURE", 48, 5, 255, 255, 0, "'C");
  GRPH_Voltage = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.222f)), PApplet.parseInt(ceil(HEIGHT*0.3009f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "VOLTAGE", 15, 0, 255, 0, 0, "V");
  GRPH_Humidity = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.431f)), PApplet.parseInt(ceil(HEIGHT*0.3009f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "HUMIDITY", 110, 0, 0, 255, 255, "%");
  GRPH_Speed = new Ti_Graph(PApplet.parseInt(ceil(WIDTH*0.431f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.19635f)), PApplet.parseInt(ceil(HEIGHT*0.21574f)), "SPEED", 11, 0, 0, 255, 0, "m/s");
}

public void draw() {
   // Getting telemetry from Serial port
  if(mySerial.available() > 10) {
   String myString = mySerial.readStringUntil('\n');
   if(myString != null) {
     telemetry = split(myString, ',');   
     if(telemetry.length == 13) {
       // Stage is 1
       StageOfSatellite = 1;
       isNewData = 1;
       // Initialization of telemtry data
       TLM_TeamID = PApplet.parseInt(telemetry[0]);
       TLM_MissionTime = PApplet.parseInt(telemetry[1]);
       TLM_PacketCount = PApplet.parseInt(telemetry[2]);
       TLM_Altitude = PApplet.parseFloat(telemetry[3]);
       TLM_Pressure = PApplet.parseFloat(telemetry[4]);
       TLM_Temperature = PApplet.parseFloat(telemetry[5]);
       TLM_Voltage = PApplet.parseFloat(telemetry[6]);
       TLM_GpsTime = telemetry[7];
       TLM_GpsLatitude = PApplet.parseFloat(telemetry[8]);
       TLM_GpsLongtitude = PApplet.parseFloat(telemetry[9]);
       TLM_GpsSatellites = PApplet.parseInt(telemetry[10]);
       TLM_GpsSpeed = PApplet.parseFloat(telemetry[11]);
       TLM_Humidity = PApplet.parseFloat(telemetry[12]);
     }else if(telemetry.length == 1) {
       // Stage is 0
       StageOfSatellite = 0;
       isNewData = 1;
       // Initialization of telemtry (altitude) data
       TLM_Altitude = PApplet.parseFloat(telemetry[0]);
     }else {
        isNewData = 0; 
     }
   }else {
      isNewData = 0; 
   }
  }else {
   isNewData =  0;
  }
  
  // Drawing data
  if(StageOfSatellite == 1 && isNewData == 1 || isFirst) {
      background(0);
      // Set Graph Values
      GRPH_Altitude.setValue(TLM_Altitude);
      GRPH_Pressure.setValue(TLM_Pressure);
      GRPH_Temperature.setValue(TLM_Temperature);
      GRPH_Voltage.setValue(TLM_Voltage);
      GRPH_Humidity.setValue(TLM_Humidity);
      GRPH_Speed.setValue(TLM_GpsSpeed);
      
      // Draw Graph
      GRPH_Altitude.drawGraph();
      GRPH_Pressure.drawGraph();
      GRPH_Temperature.drawGraph();
      GRPH_Voltage.drawGraph();
      GRPH_Humidity.drawGraph();
      GRPH_Speed.drawGraph();
      
      // Draw Battery
      Battery = new Ti_Battery(PApplet.parseInt(ceil(WIDTH*0.6463f)), PApplet.parseInt(ceil(HEIGHT*0.05f)), PApplet.parseInt(ceil(WIDTH*0.13f)), PApplet.parseInt(ceil(HEIGHT*0.408f)), PApplet.parseInt(map(TLM_Voltage, 0, 13, 0, 100)));
      Battery.drawBattery();
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(PApplet.parseInt(ceil(WIDTH*0.633f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.15625f)), PApplet.parseInt(ceil(HEIGHT*0.493f)));
      
      // Command line
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.19791f)), PApplet.parseInt(ceil(HEIGHT*0.254f)));
      line(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.0925f)), PApplet.parseInt(ceil(WIDTH*0.9947f)), PApplet.parseInt(ceil(HEIGHT*0.0925f)));
      fill(255);
      textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*16.87f))));
      text("COMMAND LINE", PApplet.parseInt(ceil(WIDTH*0.8333f)), PApplet.parseInt(ceil(HEIGHT*0.06944f)));
      textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*9.0f))));
      text("Last Value: " + com, PApplet.parseInt(ceil(WIDTH*0.807f)), PApplet.parseInt(ceil(HEIGHT*0.2407f)));
      text("Last Command Time: " + h + ":" + mi + ":" + se + "   [ " + d + "." + m + "." + y + " ]", PApplet.parseInt(ceil(WIDTH*0.8072f)), PApplet.parseInt(ceil(HEIGHT*0.259f)));
      
      // Altitude window
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.2916f)), PApplet.parseInt(ceil(WIDTH*0.1979f)), PApplet.parseInt(ceil(HEIGHT*0.225f)));
      line(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.36111f)), PApplet.parseInt(ceil(WIDTH*0.9947f)), PApplet.parseInt(ceil(HEIGHT*0.36111f)));
      fill(255);
      textFont(createFont("Arial", PApplet.parseInt(ceil(16.875f*GOLD))));
      text("ALTITUDE", PApplet.parseInt(ceil(WIDTH*0.851f)), PApplet.parseInt(ceil(HEIGHT*0.337f)));
      textFont(createFont("Arial", PApplet.parseInt(ceil(30.93f*GOLD))));
      fill(0, 255, 255);
      text(str(TLM_Altitude), PApplet.parseInt(ceil(WIDTH*0.8593f)), PApplet.parseInt(ceil(HEIGHT*0.4537f)));
      
      // Incoming DATA
      font = loadFont("ArialMT-48.vlw");
      textAlign(LEFT);
      textFont(font);
      textSize(PApplet.parseInt(ceil(22.5f*GOLD)));
      fill(255);
      text("INCOMING DATA FROM TEAM #" + str(TLM_TeamID), PApplet.parseInt(ceil(WIDTH*0.01822f)), PApplet.parseInt(ceil(HEIGHT*0.6296f)));
      
        // First Column
      fill(190);
      textSize(PApplet.parseInt(ceil(16.87f*GOLD)));
      text("Mission time:        " + str(TLM_MissionTime), PApplet.parseInt(ceil(WIDTH*0.01822f)), PApplet.parseInt(ceil(HEIGHT*0.72222f)));
      text("Packet Count:      " + str(TLM_PacketCount), PApplet.parseInt(ceil(WIDTH*0.01822f)), PApplet.parseInt(ceil(HEIGHT*0.7962f)));
      text("Altitude:                  " + str(TLM_Altitude), PApplet.parseInt(ceil(WIDTH*0.01822f)), PApplet.parseInt(ceil(HEIGHT*0.8611f))); 
      text("Voltage:                  " + str(TLM_Voltage), PApplet.parseInt(ceil(WIDTH*0.01822f)), PApplet.parseInt(ceil(HEIGHT*0.9259f)));
        // Second Column
      text("Pressure:         " + str(TLM_Pressure), PApplet.parseInt(ceil(WIDTH*0.2369f)), PApplet.parseInt(ceil(HEIGHT*0.72222f)));
      text("Temperature:     " + str(TLM_Temperature), PApplet.parseInt(ceil(WIDTH*0.2369f)), PApplet.parseInt(ceil(HEIGHT*0.7962f)));
      text("Humidity:               " + str(TLM_Humidity), PApplet.parseInt(ceil(WIDTH*0.2369f)), PApplet.parseInt(ceil(HEIGHT*0.8611f))); 
      text("Speed:                   " + str(TLM_GpsSpeed), PApplet.parseInt(ceil(WIDTH*0.2369f)), PApplet.parseInt(ceil(HEIGHT*0.9259f)));      
        // Third Column
      text("GPS latitude:          " + str(TLM_GpsLatitude), PApplet.parseInt(ceil(WIDTH*0.4557f)), PApplet.parseInt(ceil(HEIGHT*0.72222f)));
      text("GPS longtitude:      " + str(TLM_GpsLongtitude), PApplet.parseInt(ceil(WIDTH*0.4557f)), PApplet.parseInt(ceil(HEIGHT*0.7962f)));
      text("GPS satellites:         " + str(TLM_GpsSatellites), PApplet.parseInt(ceil(WIDTH*0.4557f)), PApplet.parseInt(ceil(HEIGHT*0.8611f))); 
      text("GPS time:        " + TLM_GpsTime, PApplet.parseInt(ceil(WIDTH*0.4557f)), PApplet.parseInt(ceil(HEIGHT*0.9259f)));
      
      // Board for Incoming DATA
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(PApplet.parseInt(ceil(WIDTH*0.013f)), PApplet.parseInt(ceil(HEIGHT*0.5694f)), PApplet.parseInt(ceil(WIDTH*0.7135f)), PApplet.parseInt(ceil(HEIGHT*0.38888f)));
      line(PApplet.parseInt(ceil(WIDTH*0.013f)), PApplet.parseInt(ceil(HEIGHT*0.6574f)), PApplet.parseInt(ceil(WIDTH*0.726562f)), PApplet.parseInt(ceil(HEIGHT*0.6574f)));
      line(PApplet.parseInt(ceil(WIDTH*0.2057f)), PApplet.parseInt(ceil(HEIGHT*0.6574f)), PApplet.parseInt(ceil(WIDTH*0.2057f)), PApplet.parseInt(ceil(HEIGHT*0.958333f)));
      line(PApplet.parseInt(ceil(WIDTH*0.4296f)), PApplet.parseInt(ceil(HEIGHT*0.6574f)), PApplet.parseInt(ceil(WIDTH*0.4296f)), PApplet.parseInt(ceil(HEIGHT*0.958333f)));
      
      // Drawing Logo
      MSUFO_Logo = loadImage("logo.png");
      image(MSUFO_Logo, PApplet.parseInt(ceil(WIDTH*0.773f)), PApplet.parseInt(ceil(HEIGHT*0.5879f)), PApplet.parseInt(ceil(WIDTH*0.1979f)), PApplet.parseInt(ceil(HEIGHT*0.3518f)));
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(PApplet.parseInt(ceil(WIDTH*0.75f)), PApplet.parseInt(ceil(HEIGHT*0.5694f)), PApplet.parseInt(ceil(WIDTH*0.244f)), PApplet.parseInt(ceil(HEIGHT*0.38888f)));
      
      // Writing the table
      
      newRow = TLM_Table.addRow();
      newRow.setInt("Team Id", TLM_TeamID);
      newRow.setInt("Mission Time", TLM_MissionTime);
      newRow.setInt("Packet Count", TLM_PacketCount);
      newRow.setFloat("Altitude", TLM_Altitude);
      newRow.setFloat("Pressure", TLM_Pressure);
      newRow.setFloat("Temperature", TLM_Temperature);
      newRow.setFloat("Voltage", TLM_Voltage);
      newRow.setString("GPS Time", TLM_GpsTime);
      newRow.setFloat("GPS Latitude", TLM_GpsLatitude);
      newRow.setFloat("GPS Longtitude", TLM_GpsLongtitude);
      newRow.setFloat("Speed", TLM_GpsSpeed);
      newRow.setInt("GPS Satellites", TLM_GpsSatellites);
      newRow.setFloat("Humidity", TLM_Humidity);
      
      saveTable(TLM_Table, "data/2253_YHS_2018.csv");
      
      
      isFirst = false;
      
  }else if(StageOfSatellite == 0 && isNewData == 1) {
    // Background
    stroke(0);
    fill(0);
    rectMode(CORNER);
    rect(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.1979f)), PApplet.parseInt(ceil(HEIGHT*0.493f)));
    rect(PApplet.parseInt(ceil(WIDTH*0.013f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.1979f)), PApplet.parseInt(ceil(HEIGHT*0.255f)));
    
    // Draw altitude graph
    GRPH_Altitude.setValue(TLM_Altitude);
    GRPH_Altitude.drawGraph();
    
    // Command line
    stroke(strkclr);
    noFill();
    rectMode(CORNER);
    rect(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.023f)), PApplet.parseInt(ceil(WIDTH*0.1979f)), PApplet.parseInt(ceil(HEIGHT*0.2546f)));
    line(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.0925f)), PApplet.parseInt(ceil(WIDTH*0.9947f)), PApplet.parseInt(ceil(HEIGHT*0.0925f)));
    fill(255);
    textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*16.87f))));
    text("COMMAND LINE", PApplet.parseInt(ceil(WIDTH*0.83333f)), PApplet.parseInt(ceil(HEIGHT*0.069f)));
    textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*9.0f))));
    text("Last Value: " + com, PApplet.parseInt(ceil(WIDTH*0.807f)), PApplet.parseInt(ceil(HEIGHT*0.2407f)));
    text("Last Command Time: " + h + ":" + mi + ":" + se + "   [ " + d + "." + m + "." + y + " ]", PApplet.parseInt(ceil(WIDTH*0.807f)), PApplet.parseInt(ceil(HEIGHT*0.259f)));
    
    // Altitude window
    stroke(strkclr);
    noFill();
    rectMode(CORNER);
    rect(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.291666f)), PApplet.parseInt(ceil(WIDTH*0.1979f)) ,PApplet.parseInt(ceil(HEIGHT*0.225f)));
    line(PApplet.parseInt(ceil(WIDTH*0.7968f)), PApplet.parseInt(ceil(HEIGHT*0.36111f)), PApplet.parseInt(ceil(WIDTH*0.9947f)), PApplet.parseInt(ceil(HEIGHT*0.36111f)));
    fill(255);
    textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*16.87f))));
    text("ALTUTUDE", PApplet.parseInt(ceil(WIDTH*0.851f)), PApplet.parseInt(ceil(HEIGHT*0.337f)));
    textFont(createFont("Arial", PApplet.parseInt(ceil(GOLD*30.93f))));
    fill(0, 255, 255);
    text(str(TLM_Altitude), PApplet.parseInt(ceil(WIDTH*0.8593f)), PApplet.parseInt(ceil(HEIGHT*0.4537f)));
  }
}

public void controlEvent(ControlEvent theEvent) {
 if (theEvent.isAssignableFrom(Textfield.class)) {
  Submit(); 
 }
}

public void Submit() {
  y = year();
  m = month();
  d = day();
  h = hour();
  mi = minute();
  se = second();
    
  com = cp5.get(Textfield.class,"Command line").getText(); 
  mySerial.write(com);
}
// (c) Tivole

public class Ti_Battery {
private int xPosOfBat, yPosOfBat, widthOfBat, heightOfBat, data, fontSize = PApplet.parseInt(ceil((PApplet.parseFloat(width)/1000) * 13.5f));
  PImage btr;
  
  private int colors[][] = {
                              {255, 0, 0},
                              {213, 85, 0},
                              {226, 115, 0},
                              {215, 143, 0},
                              {208, 212, 0},
                              {172, 187, 1},
                              {158, 187, 1},
                              {121, 190, 0},
                              {80, 190, 0},
                              {60, 255, 0},
                           };
  
  public Ti_Battery(int xPosOfBat, int yPosOfBat, int widthOfBat, int heightOfBat, int data) {
    this.xPosOfBat = xPosOfBat;
    this.yPosOfBat = yPosOfBat;
    this.widthOfBat = widthOfBat;
    this.heightOfBat = heightOfBat;
    this.data = data;
  }
  
  public void drawBattery() {
    noFill();
    stroke(255);
    btr = loadImage("battery2.png");
    image(btr, xPosOfBat, yPosOfBat, widthOfBat, heightOfBat);
    
    // 100 %
    textFont(createFont("Arial-BoldMT-48.vlw", fontSize));
    textSize(fontSize);
    fill(255);
    text(data + " %", xPosOfBat + widthOfBat/2 - fontSize - 5, yPosOfBat + heightOfBat + fontSize + 3);
    
    // Battery
    fill(colors[(data/10) % 10][0], colors[(data/10) % 10][1], colors[(data/10) % 10][2] );
    noStroke();
    rectMode(CENTER);
    data = PApplet.parseInt(map(data, 0, 100, 0, heightOfBat - 44));
    //rect(xPosOfBat + widthOfBat/2, yPosOfBat + heightOfBat/2 + 9, widthOfBat - 30, heightOfBat - 44, 10, 10, 10, 10);
    rect(xPosOfBat + widthOfBat/2, (yPosOfBat + heightOfBat - 14) - data/2, widthOfBat - 30, data, 10, 10, 10, 10);
  }
  
}
// (c) Tivole

public class Ti_Graph {
  private int c1, c2, c3, i, x=0, flag=1, speed=8, heightOfGraph, widthOfGraph, xPosOfGraph, yPosOfGraph, fontSize=PApplet.parseInt(ceil(9.56f * PApplet.parseFloat(width)/1000)), valForMap1, valForMap2;
  private PFont font;
  private String nameOfValue, valSI;
  private float valhist[] = new float[10000], moduleValue;
  public Ti_Graph(int xPosOfGraph, int yPosOfGraph, int widthOfGraph, int heightOfGraph, String nameOfValue, int valForMap1, int valForMap2, int c1, int c2, int c3, String valSI) {
    this.xPosOfGraph = xPosOfGraph;
    this.yPosOfGraph = yPosOfGraph;
    this.widthOfGraph = widthOfGraph;
    this.heightOfGraph = heightOfGraph;
    this.nameOfValue = nameOfValue;
    this.valForMap1 = valForMap1;
    this.valForMap2 = valForMap2;
    this.c1 = c1;
    this.c2 = c2;
    this.c3 = c3;
    this.valSI = valSI;
  }

  public void setValue(float moduleValue) {
    this.moduleValue = moduleValue; 
  }

  public void drawGraph() {    
    valhist[x] = moduleValue;
    if(flag==1) x+=speed;
    valhist[x] = moduleValue;
    
    stroke(180);
    noFill();
    beginShape();
    for(i=0; i < x; i+=speed) {
     float y = map(valhist[i], valForMap2, valForMap1, heightOfGraph, 0);
     stroke(c1, c2, c3);
     vertex(i + xPosOfGraph, y + yPosOfGraph);
    }
    endShape();
    
    font = loadFont("ArialMT-48.vlw"); 
    textFont(font); 
    textSize(fontSize);
    fill(255);
    text(nameOfValue + ":  " + valhist[i] + " " + valSI, xPosOfGraph + ceil(PApplet.parseFloat(width) * 0.0026f), yPosOfGraph+heightOfGraph+fontSize + ceil(PApplet.parseFloat(width) * 0.0046f));
     
    noFill();
    stroke(180);
    rectMode(CORNER);
    rect(xPosOfGraph, yPosOfGraph, widthOfGraph, heightOfGraph);
    
    if(x > widthOfGraph) {
       for (int i=0; i<x; i+=speed) {
        valhist[i] = valhist[i+speed];
       }
       flag = 0;
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "Ti_GSW_Mark2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
