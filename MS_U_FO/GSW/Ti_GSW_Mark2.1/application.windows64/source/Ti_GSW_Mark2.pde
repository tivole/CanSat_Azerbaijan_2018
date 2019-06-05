// (c) Tivole

import processing.serial.*;
import controlP5.*;

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


void setup() {
  // Window size
  fullScreen();
  background(0);
  surface.setTitle("Ground Station | MS_U_FO");
  
  WIDTH = float(width);
  HEIGHT = float(height);
  GOLD = WIDTH/1000;

  // Serial port connection
  mySerial = new Serial(this, Serial.list()[0], 9600);

  // Command line
  font = createFont("arial", int(ceil(11.25*GOLD)));
  cp5 = new ControlP5(this);
  cp5.addTextfield("Command line").setPosition(int(ceil(0.804*WIDTH)), int(ceil(0.106*HEIGHT))).setSize(int(ceil(0.1822*WIDTH)), int(ceil(0.055555*HEIGHT))).setFont(font).setAutoClear(true);
  cp5.addBang("Submit").setPosition(int(ceil(0.804*WIDTH)) - 1, int(ceil(0.162*HEIGHT))).setSize(int(ceil(0.18333*WIDTH)), int(ceil(0.037*HEIGHT))).getCaptionLabel().setFont(createFont("arial", int(ceil(9.0*GOLD)))).align(cp5.CENTER, cp5.CENTER);

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
  GRPH_Altitude = new Ti_Graph(int(ceil(WIDTH*0.013)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "ALTITUDE", 430, 0, 255, 0, 255, "m");
  GRPH_Pressure = new Ti_Graph(int(ceil(WIDTH*0.013)), int(ceil(HEIGHT*0.3009)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "PRESSURE", 130000, 90000, 0, 255, 131, "Pa");
  GRPH_Temperature = new Ti_Graph(int(ceil(WIDTH*0.222)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "TEMPERATURE", 48, 5, 255, 255, 0, "'C");
  GRPH_Voltage = new Ti_Graph(int(ceil(WIDTH*0.222)), int(ceil(HEIGHT*0.3009)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "VOLTAGE", 15, 0, 255, 0, 0, "V");
  GRPH_Humidity = new Ti_Graph(int(ceil(WIDTH*0.431)), int(ceil(HEIGHT*0.3009)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "HUMIDITY", 110, 0, 0, 255, 255, "%");
  GRPH_Speed = new Ti_Graph(int(ceil(WIDTH*0.431)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.19635)), int(ceil(HEIGHT*0.21574)), "SPEED", 11, 0, 0, 255, 0, "m/s");
}

void draw() {
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
       TLM_TeamID = int(telemetry[0]);
       TLM_MissionTime = int(telemetry[1]);
       TLM_PacketCount = int(telemetry[2]);
       TLM_Altitude = float(telemetry[3]);
       TLM_Pressure = float(telemetry[4]);
       TLM_Temperature = float(telemetry[5]);
       TLM_Voltage = float(telemetry[6]);
       TLM_GpsTime = telemetry[7];
       TLM_GpsLatitude = float(telemetry[8]);
       TLM_GpsLongtitude = float(telemetry[9]);
       TLM_GpsSatellites = int(telemetry[10]);
       TLM_GpsSpeed = float(telemetry[11]);
       TLM_Humidity = float(telemetry[12]);
     }else if(telemetry.length == 1) {
       // Stage is 0
       StageOfSatellite = 0;
       isNewData = 1;
       // Initialization of telemtry (altitude) data
       TLM_Altitude = float(telemetry[0]);
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
      Battery = new Ti_Battery(int(ceil(WIDTH*0.6463)), int(ceil(HEIGHT*0.05)), int(ceil(WIDTH*0.13)), int(ceil(HEIGHT*0.408)), int(map(TLM_Voltage, 0, 13, 0, 100)));
      Battery.drawBattery();
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(int(ceil(WIDTH*0.633)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.15625)), int(ceil(HEIGHT*0.493)));
      
      // Command line
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.19791)), int(ceil(HEIGHT*0.254)));
      line(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.0925)), int(ceil(WIDTH*0.9947)), int(ceil(HEIGHT*0.0925)));
      fill(255);
      textFont(createFont("Arial", int(ceil(GOLD*16.87))));
      text("COMMAND LINE", int(ceil(WIDTH*0.8333)), int(ceil(HEIGHT*0.06944)));
      textFont(createFont("Arial", int(ceil(GOLD*9.0))));
      text("Last Value: " + com, int(ceil(WIDTH*0.807)), int(ceil(HEIGHT*0.2407)));
      text("Last Command Time: " + h + ":" + mi + ":" + se + "   [ " + d + "." + m + "." + y + " ]", int(ceil(WIDTH*0.8072)), int(ceil(HEIGHT*0.259)));
      
      // Altitude window
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.2916)), int(ceil(WIDTH*0.1979)), int(ceil(HEIGHT*0.225)));
      line(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.36111)), int(ceil(WIDTH*0.9947)), int(ceil(HEIGHT*0.36111)));
      fill(255);
      textFont(createFont("Arial", int(ceil(16.875*GOLD))));
      text("ALTITUDE", int(ceil(WIDTH*0.851)), int(ceil(HEIGHT*0.337)));
      textFont(createFont("Arial", int(ceil(30.93*GOLD))));
      fill(0, 255, 255);
      text(str(TLM_Altitude), int(ceil(WIDTH*0.8593)), int(ceil(HEIGHT*0.4537)));
      
      // Incoming DATA
      font = loadFont("ArialMT-48.vlw");
      textAlign(LEFT);
      textFont(font);
      textSize(int(ceil(22.5*GOLD)));
      fill(255);
      text("INCOMING DATA FROM TEAM #" + str(TLM_TeamID), int(ceil(WIDTH*0.01822)), int(ceil(HEIGHT*0.6296)));
      
        // First Column
      fill(190);
      textSize(int(ceil(16.87*GOLD)));
      text("Mission time:        " + str(TLM_MissionTime), int(ceil(WIDTH*0.01822)), int(ceil(HEIGHT*0.72222)));
      text("Packet Count:      " + str(TLM_PacketCount), int(ceil(WIDTH*0.01822)), int(ceil(HEIGHT*0.7962)));
      text("Altitude:                  " + str(TLM_Altitude), int(ceil(WIDTH*0.01822)), int(ceil(HEIGHT*0.8611))); 
      text("Voltage:                  " + str(TLM_Voltage), int(ceil(WIDTH*0.01822)), int(ceil(HEIGHT*0.9259)));
        // Second Column
      text("Pressure:         " + str(TLM_Pressure), int(ceil(WIDTH*0.2369)), int(ceil(HEIGHT*0.72222)));
      text("Temperature:     " + str(TLM_Temperature), int(ceil(WIDTH*0.2369)), int(ceil(HEIGHT*0.7962)));
      text("Humidity:               " + str(TLM_Humidity), int(ceil(WIDTH*0.2369)), int(ceil(HEIGHT*0.8611))); 
      text("Speed:                   " + str(TLM_GpsSpeed), int(ceil(WIDTH*0.2369)), int(ceil(HEIGHT*0.9259)));      
        // Third Column
      text("GPS latitude:          " + str(TLM_GpsLatitude), int(ceil(WIDTH*0.4557)), int(ceil(HEIGHT*0.72222)));
      text("GPS longtitude:      " + str(TLM_GpsLongtitude), int(ceil(WIDTH*0.4557)), int(ceil(HEIGHT*0.7962)));
      text("GPS satellites:         " + str(TLM_GpsSatellites), int(ceil(WIDTH*0.4557)), int(ceil(HEIGHT*0.8611))); 
      text("GPS time:        " + TLM_GpsTime, int(ceil(WIDTH*0.4557)), int(ceil(HEIGHT*0.9259)));
      
      // Board for Incoming DATA
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(int(ceil(WIDTH*0.013)), int(ceil(HEIGHT*0.5694)), int(ceil(WIDTH*0.7135)), int(ceil(HEIGHT*0.38888)));
      line(int(ceil(WIDTH*0.013)), int(ceil(HEIGHT*0.6574)), int(ceil(WIDTH*0.726562)), int(ceil(HEIGHT*0.6574)));
      line(int(ceil(WIDTH*0.2057)), int(ceil(HEIGHT*0.6574)), int(ceil(WIDTH*0.2057)), int(ceil(HEIGHT*0.958333)));
      line(int(ceil(WIDTH*0.4296)), int(ceil(HEIGHT*0.6574)), int(ceil(WIDTH*0.4296)), int(ceil(HEIGHT*0.958333)));
      
      // Drawing Logo
      MSUFO_Logo = loadImage("logo.png");
      image(MSUFO_Logo, int(ceil(WIDTH*0.773)), int(ceil(HEIGHT*0.5879)), int(ceil(WIDTH*0.1979)), int(ceil(HEIGHT*0.3518)));
      stroke(strkclr);
      noFill();
      rectMode(CORNER);
      rect(int(ceil(WIDTH*0.75)), int(ceil(HEIGHT*0.5694)), int(ceil(WIDTH*0.244)), int(ceil(HEIGHT*0.38888)));
      
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
    rect(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.1979)), int(ceil(HEIGHT*0.493)));
    rect(int(ceil(WIDTH*0.013)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.1979)), int(ceil(HEIGHT*0.255)));
    
    // Draw altitude graph
    GRPH_Altitude.setValue(TLM_Altitude);
    GRPH_Altitude.drawGraph();
    
    // Command line
    stroke(strkclr);
    noFill();
    rectMode(CORNER);
    rect(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.023)), int(ceil(WIDTH*0.1979)), int(ceil(HEIGHT*0.2546)));
    line(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.0925)), int(ceil(WIDTH*0.9947)), int(ceil(HEIGHT*0.0925)));
    fill(255);
    textFont(createFont("Arial", int(ceil(GOLD*16.87))));
    text("COMMAND LINE", int(ceil(WIDTH*0.83333)), int(ceil(HEIGHT*0.069)));
    textFont(createFont("Arial", int(ceil(GOLD*9.0))));
    text("Last Value: " + com, int(ceil(WIDTH*0.807)), int(ceil(HEIGHT*0.2407)));
    text("Last Command Time: " + h + ":" + mi + ":" + se + "   [ " + d + "." + m + "." + y + " ]", int(ceil(WIDTH*0.807)), int(ceil(HEIGHT*0.259)));
    
    // Altitude window
    stroke(strkclr);
    noFill();
    rectMode(CORNER);
    rect(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.291666)), int(ceil(WIDTH*0.1979)) ,int(ceil(HEIGHT*0.225)));
    line(int(ceil(WIDTH*0.7968)), int(ceil(HEIGHT*0.36111)), int(ceil(WIDTH*0.9947)), int(ceil(HEIGHT*0.36111)));
    fill(255);
    textFont(createFont("Arial", int(ceil(GOLD*16.87))));
    text("ALTUTUDE", int(ceil(WIDTH*0.851)), int(ceil(HEIGHT*0.337)));
    textFont(createFont("Arial", int(ceil(GOLD*30.93))));
    fill(0, 255, 255);
    text(str(TLM_Altitude), int(ceil(WIDTH*0.8593)), int(ceil(HEIGHT*0.4537)));
  }
}

void controlEvent(ControlEvent theEvent) {
 if (theEvent.isAssignableFrom(Textfield.class)) {
  Submit(); 
 }
}

void Submit() {
  y = year();
  m = month();
  d = day();
  h = hour();
  mi = minute();
  se = second();
    
  com = cp5.get(Textfield.class,"Command line").getText(); 
  mySerial.write(com);
}
