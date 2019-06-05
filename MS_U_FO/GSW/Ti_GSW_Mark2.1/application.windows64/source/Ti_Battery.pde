// (c) Tivole

public class Ti_Battery {
private int xPosOfBat, yPosOfBat, widthOfBat, heightOfBat, data, fontSize = int(ceil((float(width)/1000) * 13.5));
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
    data = int(map(data, 0, 100, 0, heightOfBat - 44));
    //rect(xPosOfBat + widthOfBat/2, yPosOfBat + heightOfBat/2 + 9, widthOfBat - 30, heightOfBat - 44, 10, 10, 10, 10);
    rect(xPosOfBat + widthOfBat/2, (yPosOfBat + heightOfBat - 14) - data/2, widthOfBat - 30, data, 10, 10, 10, 10);
  }
  
}
