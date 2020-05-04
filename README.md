# androidAlarmTest
 This small Android project reproduces the WakeUp problem on Samsung S10.

On the Samsung Galaxy S10 the alarm does not wake the screen or sound an alarm after 1 mins.

Expected Behaviour

Start App

 - Start the timer by pressing alarm button
 - Turn off screen
 - After 1 minutes the device should:
   - sound a loud continuous audible alarm
   - vibrate
   - screen wakes up
   - Alarm frame displayed to user
 

Observed Behaviour

- 4. same as above
- 5. After 1 minutes nothing happens
- 6. After 2-4 minutes the expected behaviour from step 5. occurs
 

Notes

Currently only the S10 has received the Android 10 update.  However the S9 and other devices from Samsung have already started receiving the update.

This was a customer reported issue with many S10 and S10e devices.
