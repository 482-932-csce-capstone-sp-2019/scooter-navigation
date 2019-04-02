//=====================NOTES=======================//
//do i need pull up/down resistors for anything?
//do i need low power LED? is that what the L LED is for? 

//=================HARDWARE========================//
//currently vibration motors should be attached to analog 0-1-2
//  need to check to make sure the motors are correctly positions so they turn on from inside to outside
//Bluetooth LED on digital 2
//Low battery LED on digital 3
//test LED on digital 4

//=================CONSTANTS=======================//
int state = 0;
const int POWER = 255;

//these constants will differ between the code for the left and right arduino, this *should* be the only difference
const int RIGHT = 1;
const int LEFT = 0;

//these constants determine the number of motors and power to supply for vibration patterns
const int lowNumMotors = 1;
const int mediumNumMotors = 2;
const int highNumMotors = 3;
const int lowIntensity = POWER;
const int mediumIntensity = POWER;
const int highIntensity = POWER; //can also do digitalWrite(A0, HIGH);

//=================================================//


// the setup function runs once when you press reset or power the board
void setup() 
{
  //vibration motors
  pinMode(A0,OUTPUT);
  pinMode(A1,OUTPUT);
  pinMode(A2,OUTPUT);
  //bluetooth LED
  pinMode(2,OUTPUT);
  //low battery LED
  pinMode(3,OUTPUT);

  //test LED
  pinMode(4,OUTPUT);
  digitalWrite(4,HIGH);
  delay(250);
  digitalWrite(4,LOW);

  //Default connection for BT module
  Serial.begin(9600); 
}

// the loop function runs over and over again forever
void loop() {
  if (Serial.available() > 0){  //checks if there are characters to read in receive  buffer
    state = Serial.read(); //gets character from buffer

    //===============TEST===============//
    if (state == 'f' || state == 'F') {
      signalRight(lowIntensity,lowNumMotors);
      digitalWrite(4,HIGH);
      delay(250);
      digitalWrite(4,LOW);
      delay(250);
      digitalWrite(4,HIGH);
      delay(250);
      digitalWrite(4,LOW);
    }
    
    //===============RIGHT===============//
    //low
    if (state == '0') {
      signalRight(lowIntensity,lowNumMotors);
      Serial.println("right low");
    }

    //medium
    if (state == 'r') {
      signalRight(mediumIntensity,mediumNumMotors);
      Serial.println("right medium");
    }

    //high
    if (state == 'R') {
      signalRight(highIntensity,highNumMotors);
      Serial.println("right high");
    }

    //===============LEFT================//
    //low
    if (state == '1') {
      signalRight(lowIntensity,lowNumMotors);
      Serial.println("left low");
    }

    //medium
    if (state == 'l') {
      signalRight(mediumIntensity,mediumNumMotors);
      Serial.println("left medium");
    }

    //high
    if (state == 'L') {
      signalRight(highIntensity,highNumMotors);
      Serial.println("left high");
    }

    //===============UTURN===============//
    //low
    if (state == '2') {
      signalRight(lowIntensity,lowNumMotors);
      Serial.println("uturn low");
    }

    //medium
    if (state == 'u') {
      signalRight(mediumIntensity,mediumNumMotors);
      Serial.println("uturn medium");
    }

    //high
    if (state == 'U') {
      signalRight(highIntensity,highNumMotors);
      Serial.println("uturn high");
    }
  }
}

void signalRight(int intensity, int numberMotors){
  if(RIGHT){  //only the RIGHT band should do this
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, intensity);
    }
    delay(125);
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, 0);
    }
  }
}

void signalLeft(int intensity, int numberMotors){
  if(LEFT){ //only the LEFT band should do this
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, intensity);
    }
    delay(125);
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, 0);
    }
  }
}

void uturn(int intensity, int numberMotors){
  for(int i = 0; i < numberMotors; i++){
      analogWrite(i, intensity);
    }
    delay(125);
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, 0);
    }
}
