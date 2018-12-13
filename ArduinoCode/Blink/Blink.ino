int state = 0;
int power = 255;

// the setup function runs once when you press reset or power the board
void setup() {
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
   rl_on();
   beep();
   Serial.begin(9600); // Default connection for BT module
}

// the loop function runs over and over again forever
void loop() {
  if (Serial.available() > 0) {
    state = Serial.read();
    rl_off();
    gl_on(); //Shows that device has connected
    
    //right();
    if (state == '0') {
      right();
      Serial.println("Right Turn");
    }
  
    //left();
    else if (state == '1') {
      left();
      Serial.println("Left Turn");
    }
  
    //right_approach();
    else if (state == '2') {
      right_approach();
      Serial.println("Right Approach");
    }
  
    //left_approach();
    else if (state == '3') {
      left_approach();
      Serial.println("Left Approach");
    }
  
    //cont();
    else if (state == '4') {
      cont();
      Serial.println("Continue");
    }
  
    //arrival();
    else if (state == '5') {
      arrival();
      gl_on();
      Serial.println("Arrival");
    }
  
    //uturn();
    else if (state == '6') {
      uturn();
      Serial.println("U-Turn");
    }

    //beep();
    else if (state == '7') {
      beep();
      Serial.println("Connected");
    }

    //power change
    else if (state == '8') {
      power = 100;
      Serial.println("Low Power");
    }
    else if (state == '9') {
      power = 175;
      Serial.println("Medium Power");
    }
    else if (state == 'a') {
      power = 255;
      Serial.println("High Power");
    }

    //Lights
    else if (state == 'b') {
      gl_on();
      Serial.println("Green Light: ON");
    }
    else if (state == 'c') {
      gl_off();
      Serial.println("Green Light: OFF");
    }
    else if (state == 'd') {
      yl_on();
      Serial.println("Yellow Light: ON");
    }
    else if (state == 'e') {
      yl_off();
      Serial.println("Yellow Light: OFF");
    }
    else if (state == 'f') {
      rl_on();
      Serial.println("Red Light: ON");
    }
    else if (state == 'g') {
      rl_off();
      Serial.println("Red Light: OFF");
    }
  }
}

//Turn right
void right() {
  analogWrite(LED_BUILTIN, power);   // turn the LED on (power is the voltage level)
  analogWrite(6, 0);
  analogWrite(5, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(6, power);
    delay(500);
    analogWrite(6, 0);
    analogWrite(5, power);
    delay(500);
    analogWrite(5, 0);
    delay(125);
  }
}

//Turn right approaching
void right_approach() {
  analogWrite(LED_BUILTIN, power);
  analogWrite(6, 0);
  analogWrite(5, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(6, power);
    analogWrite(5, power);
    delay(125);
    analogWrite(6, 0);
    analogWrite(5, 0);
    delay(125);
  }
}

//Turn left
void left() {
  analogWrite(LED_BUILTIN, power);   // turn the LED on (power is the voltage level)
  analogWrite(10, 0);
  analogWrite(9, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(9, power);
    delay(500);
    analogWrite(9, 0);
    analogWrite(8, power);
    delay(500);
    analogWrite(8, 0);
    delay(125);
  }
}

//Turn left approaching
void left_approach() {
  analogWrite(LED_BUILTIN, power);
  analogWrite(10, 0);
  analogWrite(9, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(10, power);
    analogWrite(9, power);
    delay(125);
    analogWrite(10, 0);
    analogWrite(9, 0);
    delay(125);
  }
}

//Stop
void arrival() {
  analogWrite(LED_BUILTIN, power);
  analogWrite(10, 0);
  analogWrite(9, 0);
  analogWrite(6, 0);
  analogWrite(5, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(10, power);
    analogWrite(9, power);
    analogWrite(6, power);
    analogWrite(5, power);
    delay(250);
    analogWrite(LED_BUILTIN, power);
    analogWrite(10, 0);
    analogWrite(9, 0);
    analogWrite(6, 0);
    analogWrite(5, 0);
    delay(250);
  }
}

void cont() {
  analogWrite(LED_BUILTIN, power);
  analogWrite(10, 0);
  analogWrite(9, 0);
  analogWrite(6, 0);
  analogWrite(5, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(5, power);
    analogWrite(10, power);
    delay(500);
    analogWrite(5, 0);
    analogWrite(10, 0);
    analogWrite(6, power);
    analogWrite(9, power);
    delay(500);
    analogWrite(6, 0);
    analogWrite(9, 0);
    delay(250);
  }
}

//Make U-Turn
void uturn() {
  analogWrite(LED_BUILTIN, power);
  analogWrite(10, 0);
  analogWrite(9, 0);
  analogWrite(6, 0);
  analogWrite(5, 0);
  for(int i = 0; i < 3; i++) {
    analogWrite(LED_BUILTIN, 0);
    analogWrite(9, power);
    analogWrite(6, power);
    delay(500);
    analogWrite(9, 0);
    analogWrite(6, 0);
    analogWrite(5, power);
    analogWrite(10, power);
    delay(500);
    analogWrite(5, 0);
    analogWrite(10, 0);
    delay(250);
  }
}

//Connection Noise
void beep() {
  tone(3, 1000, 1000);
}

//Green Light
void gl_on() {
  digitalWrite(8, HIGH);
}
void gl_off() {
  digitalWrite(8, LOW);
}
//Red Light
void rl_on() {
  digitalWrite(7, HIGH);
}
void rl_off() {
  digitalWrite(7, LOW);
}
//Yellow Light
void yl_on() {
  digitalWrite(4, HIGH);
}
void yl_off() {
  digitalWrite(4, LOW);
}
