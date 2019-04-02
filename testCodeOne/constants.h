//=================CONSTANTS=======================//
int state = 0;
const int POWER = 255;

//these constants will differ between the code for the left and right arduino, this *should* be the only difference
//moved to main file
const int RIGHT = 1;
const int LEFT = 0;

//
const int INSIDE = 2;
const int MIDDLE = 3;
const int OUTSIDE = 4;


//these constants determine the number of motors and power to supply for vibration patterns
const int lowNumMotors = 1;
const int mediumNumMotors = 2;
const int highNumMotors = 3;
const int lowIntensity = POWER;
const int mediumIntensity = POWER;
const int highIntensity = POWER; //can also do digitalWrite(A0, HIGH);
