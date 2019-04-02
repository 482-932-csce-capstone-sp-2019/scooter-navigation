//=====================NOTES=======================//
//do i need pull up/down resistors for anything?
//do i need low power LED? is that what the L LED is for? 

//=================HARDWARE========================//
//currently vibration motors should be attached to analog 0-1-2
//  need to check to make sure the motors are correctly positions so they turn on from inside to outside
//Bluetooth LED on digital 2
//Low battery LED on digital 3
//test LED on digital 4

//=================================================//
#include "constants.h"


//======================SIGNALS======================//

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
