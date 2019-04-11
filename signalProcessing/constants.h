//D3, D5, D6, D9, D10 and D11 are PWM-capable.

//=================CONSTANTS=======================//
int state = 0;
const int POWER = 255;

//these constants will differ between the code for the left and right arduino, this *should* be the only difference
//moved to main file
const int RIGHT = 1;
const int LEFT = 0;

//
const int INSIDE = 9;
const int MIDDLE = 10;
const int OUTSIDE = 11;


//these constants determine the number of motors and power to supply for vibration patterns
const int lowNumMotors = 1;
const int mediumNumMotors = 2;
const int highNumMotors = 3;
const int lowIntensity = 85;
const int mediumIntensity = 170;
const int highIntensity = 255; //can also do digitalWrite(A0, HIGH);
