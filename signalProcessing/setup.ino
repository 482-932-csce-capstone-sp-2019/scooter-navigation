// the setup function runs once when you press reset or power the board

void setup() {
  //vibration motors
  pinMode(A2,OUTPUT);
  pinMode(A3,OUTPUT);
  pinMode(A4,OUTPUT);
  //bluetooth LED
  //pinMode(2,OUTPUT);
  //low battery LED
  //pinMode(3,OUTPUT);

  //test vibration
  //pinMode(4,OUTPUT);
  vibe4(4,75);
  vibe4(2,150);
  //test 3 motors
  pinMode(2,OUTPUT);
  pinMode(3,OUTPUT);
  pinMode(4,OUTPUT);
  digitalWrite(2,HIGH);
  delay(250);
  digitalWrite(2,LOW);
  delay(250);
  digitalWrite(3,HIGH);
  delay(250);
  digitalWrite(3,LOW);
  delay(250);
  digitalWrite(4,HIGH);
  delay(250);
  digitalWrite(4,LOW);
  delay(250);

  //Default connection for BT module
  Serial.begin(9600); 
}
