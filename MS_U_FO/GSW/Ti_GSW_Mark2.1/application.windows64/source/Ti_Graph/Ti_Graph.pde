// (c) Tivole

public class Ti_Graph {
  private int c1, c2, c3, i, x=0, flag=1, speed=8, heightOfGraph, widthOfGraph, xPosOfGraph, yPosOfGraph, fontSize=int(ceil(9.56 * float(width)/1000)), valForMap1, valForMap2;
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
    text(nameOfValue + ":  " + valhist[i] + " " + valSI, xPosOfGraph + ceil(float(width) * 0.0026), yPosOfGraph+heightOfGraph+fontSize + ceil(float(width) * 0.0046));
     
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
