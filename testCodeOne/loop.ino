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

void vibe4(int times, int wait){
  for(int i = 0; i < times; i++){
      digitalWrite(4,HIGH);
      delay(wait);
      digitalWrite(4,LOW);
      delay(wait);
    } 
}
