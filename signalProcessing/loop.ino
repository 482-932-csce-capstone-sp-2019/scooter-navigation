// the loop function runs over and over again forever listening for bluetooth characters in the buffer
void loop() {
  if (Serial.available() > 0){  //checks if there are characters to read in receive  buffer
    state = Serial.read(); //gets character from buffer

    //===============TEST===============//
    if (state == 'f' || state == 'F') {
//      right(lowIntensity,lowNumMotors);
//      digitalWrite(4,HIGH);
//      delay(250);
//      digitalWrite(4,LOW);
//      delay(250);
//      digitalWrite(4,HIGH);
//      delay(250);
//      digitalWrite(4,LOW);
        Serial.println("fF");
        analogWrite(INSIDE,255);
        analogWrite(MIDDLE,255);
        analogWrite(OUTSIDE,255);
        delay(500);
        analogWrite(INSIDE,0);
        analogWrite(MIDDLE,0);
        analogWrite(OUTSIDE,0);
    }

     if (state == 'g' || state == 'G') {
        Serial.println("gG");
        analogWrite(INSIDE,170);
        analogWrite(MIDDLE,170);
        analogWrite(OUTSIDE,170);
        delay(500);
        analogWrite(INSIDE,0);
        analogWrite(MIDDLE,0);
        analogWrite(OUTSIDE,0);
     }

     if (state == 'h' || state == 'H') {
        Serial.println("hH");
        analogWrite(INSIDE,85);
        analogWrite(MIDDLE,85);
        analogWrite(OUTSIDE,85);
        delay(500);
        analogWrite(INSIDE,0);
        analogWrite(MIDDLE,0);
        analogWrite(OUTSIDE,0);
     }
    
    //===============RIGHT===============//
    //low
    if (state == '0') {
      right(lowIntensity);
      Serial.println("right low");
    }

    //medium
    if (state == 'r') {
      right(mediumIntensity);
      Serial.println("right medium");
    }

    //high
    if (state == 'R') {
      right(highIntensity);
      Serial.println("right high");
    }

    //===============LEFT================//
    //low
    if (state == '1') {
      left(lowIntensity);
      Serial.println("left low");
    }

    //medium
    if (state == 'l') {
      left(mediumIntensity);
      Serial.println("left medium");
    }

    //high
    if (state == 'L') {
      left(highIntensity);
      Serial.println("left high");
    }

    //===============UTURN===============//
    //low
    if (state == '2') {
      uturn(lowIntensity);
      Serial.println("uturn low");
    }

    //medium
    if (state == 'u') {
      uturn(mediumIntensity);
      Serial.println("uturn medium");
    }

    //high
    if (state == 'U') {
      uturn(highIntensity);
      Serial.println("uturn high");
    }
  }

  //=============DISMOUNT==============//
  
  // send dismount signal, maybe i should send weaker approaching dismount signals,but that might clog up user attention
  if (state == 'D') {
      dismount();
      Serial.println("dismount");
    }

  //============REMOUNT================//
  
  // send dismount signal
  if (state == 'E') {
      remount();
      Serial.println("remount");
    }
    
  //===============BUZZ================//
  //buzz on
  if (state == 'B') {
      buzz(1);
      Serial.println("Buzz");
    }

  //buzz off
  if (state == 'b') {
      buzz(0);
      Serial.println("stop Buzz");
    }
}

void vibe4(int times, int wait){
  for(int i = 0; i < times; i++){
      digitalWrite(4,HIGH);
      delay(wait);
      digitalWrite(4,LOW);
      delay(wait);
    } 
}
