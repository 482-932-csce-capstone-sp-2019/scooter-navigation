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

void right(int intensity){
  if(RIGHT){  //only the RIGHT band should do this
      analogWrite(INSIDE,intensity);
      delay(500);
      analogWrite(INSIDE,0);
      analogWrite(MIDDLE,intensity);
      delay(500);
      analogWrite(MIDDLE,0);
      analogWrite(OUTSIDE,intensity);
      delay(500);
      analogWrite(OUTSIDE,0);
  }
}

void left(int intensity){
  if(LEFT){ //only the LEFT band should do this
      analogWrite(INSIDE,intensity);
      delay(500);
      analogWrite(INSIDE,0);
      analogWrite(MIDDLE,intensity);
      delay(500);
      analogWrite(MIDDLE,0);
      analogWrite(OUTSIDE,intensity);
      delay(500);
      analogWrite(OUTSIDE,0);
     
  }
}

void uturn(int intensity){  
  analogWrite(INSIDE,intensity);
  delay(500);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,intensity);
  delay(500);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,intensity);
  delay(500);
  analogWrite(OUTSIDE,0);
}

void dismount(){
  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(200);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(200);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(400);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(400);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(600);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(600);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(800);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(800);
}

void remount(){
  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(800);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(800);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(600);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(600);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(400);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(400);

  analogWrite(INSIDE,255);
  analogWrite(MIDDLE,255);
  analogWrite(OUTSIDE,255);
  delay(200);
  analogWrite(INSIDE,0);
  analogWrite(MIDDLE,0);
  analogWrite(OUTSIDE,0);
  delay(200);
}

void buzz(int power){
  analogWrite(INSIDE,power);
  analogWrite(MIDDLE,power);
  analogWrite(OUTSIDE,power);
}
