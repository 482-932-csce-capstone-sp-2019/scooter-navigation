//currently vibration motors should be attached to analog 0-1-2

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
const int highIntensity = POWER;

//=================================================//


// the setup function runs once when you press reset or power the board
void setup() 
{
  
}

// the loop function runs over and over again forever
void loop() {
  if (Serial.available() > 0){  //checks if there are characters to read in receive  buffer
    state = Serial.read(); //gets character from buffer
    //rl_off();
    //gl_on(); //Shows that device has connected

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
  }
}

void signalLeft(int intensity, int numberMotors){
  if(LEFT){ //only the LEFT band should do this
    for(int i = 0; i < numberMotors; i++){
      analogWrite(i, intensity);
    }
  }
}

void uturn(int intensity, int numberMotors){
  for(int i = 0; i < numberMotors; i++){
      analogWrite(i, intensity);
    }
}
