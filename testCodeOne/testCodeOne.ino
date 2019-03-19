int state = 0;
const int POWER = 255;

//these constants will differ between the code for the left and right arduino, this *should* be the only difference
const int RIGHT = 1;
const int LEFT = 0;

// the setup function runs once when you press reset or power the board
void setup() 
{
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);
  // vibration motors are ordered from pin 12-9 for left handlebar in order with 12 furthest left
  pinMode(10, OUTPUT);
  pinMode(9, OUTPUT);
  // vibration motors are ordered from pin 7-4 for right handlebar in order with 4 furthest right
  pinMode(6, OUTPUT);
  pinMode(5, OUTPUT);
  /*        LEFT HANDLEBAR                     RIGHT HANDLEBAR
   *        11  10  9   8                        7  6  5  4
   *        L1  L2  L3  L4                       R1 R2 R3 R4
   */
   // initialize pin for speaker
   pinMode(12, OUTPUT);
   // initialize pins for lights
   pinMode(4, OUTPUT);
   pinMode(7, OUTPUT);
   pinMode(8, OUTPUT);
   // turn red light on to show device is on
   //rl_on();
   //beep();
   Serial.begin(9600); // Default connection for BT module
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
      signalRight(-1,-1);
      Serial.println("right low");
    }

    //medium
    if (state == 'r') {
      signalRight(-1,-1);
      Serial.println("right medium");
    }

    //high
    if (state == 'R') {
      signalRight(-1,-1);
      Serial.println("right high");
    }

    //===============LEFT================//
    //low
    if (state == '1') {
      signalRight(-1,-1);
      Serial.println("left low");
    }

    //medium
    if (state == 'l') {
      signalRight(-1,-1);
      Serial.println("left medium");
    }

    //high
    if (state == 'L') {
      signalRight(-1,-1);
      Serial.println("left high");
    }

    //===============UTURN===============//
    //low
    if (state == '2') {
      signalRight(-1,-1);
      Serial.println("uturn low");
    }

    //medium
    if (state == 'u') {
      signalRight(-1,-1);
      Serial.println("uturn medium");
    }

    //high
    if (state == 'U') {
      signalRight(-1,-1);
      Serial.println("uturn high");
    }
  }
}

void signalRight(int intensity, int numberMotors){
  if(RIGHT){  //only the RIGHT band should do this
    
  }
}

void signalLeft(int intensity, int numberMotors){
  if(LEFT){ //only the LEFT band should do this
    
  }
}

void uturn(int intensity, int numberMotors){

}
