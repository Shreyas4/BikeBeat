//#include <EEPROM.h>
#define NUM_SAMPLES 10
#include <SoftwareSerial.h>
int RX = 0;
int TX = 1;
SoftwareSerial mySerial(RX, TX);
int pin = 7;
unsigned long time;
unsigned long duration;
int count = 0;
float dist=0;
int sum = 0;                    // sum of samples taken
unsigned char sample_count = 0; // current sample number
float voltage = 0.0;            // calculated voltage
String final_string;
void setup()
{
  Serial.begin(9600);
  pinMode(pin, INPUT);
  mySerial.begin(9600);
}

void loop()
{
  while (sample_count < NUM_SAMPLES) {
        sum += analogRead(A0);
        sample_count++;
        //delay(1000);
    }
    voltage = analogRead(A0) * 55.0/1024.0;
  duration = pulseIn(pin, HIGH);
  if(duration > 0){
    count ++;
    if(count%8==0)
  {
    dist=dist+1.727;
   time = millis();
  //mySerial.print(12.0-(voltage));
  //mySerial.write("hi");
  //mySerial.println(time/1000);
  //mySerial.println(dist);
  String send_string3 = String(12.0-(voltage ));
  String send_string1 = String(time);
  String send_string2 = String(dist);
  final_string = String("##"+send_string1 + " " + send_string2 + " " + send_string3 +"##");
  Serial.print(final_string);
  mySerial.print(final_string);
  
  /*
  String str1=String(time);
  String str2=String(dist);
  str1=String(str1+"\t"+str2);*/
  }
  //.write(0,dist);
  }
  //char buff[1000];
  //final_string.toCharArray(buff,100);
  //mySerial.write(buff);
  //float prints= EEPROM.read(0);
  //Serial.print(prints);4
  
  //Serial.println(count);
}
