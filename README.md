# Save My Ship Application

Save My Ship (SMS) is an application which helps users in emergency situations. The application requires the distressed user to just click a distress button (a signal signifying that the user is in trouble and needs help) and rest assured, having the faith that he/she will receive some kind of help. 

### Installation Steps
---

The folder Jethwani_Mohita_A1.zip contains a folder Web Services PA1 which needs to be imported in Eclipse.
To import the folder perform the following steps-

Go to eclipse---> File---> Import---> Under General---> Select Existing Projects into Workspace---> Next---> Browse to the Web Services folder---> Under options check against "Copy projects into workspace"---> Finish.

Before executing the code, please enter the contact numbers to which the location of the user needs to be sent in the form of a text message.

The contact numbers need to be entered in Client.java file. Shown below is a code snippet that needs to be changed. 

emergencyContactList is an array of contact numbers. The numbers need to be added in this array.

Example: emergencyContactList.add("+15854858455");
```
//Add the contact numbers here and uncomment the lines where the numbers are entered
emergencyContactList.add("+1CONTINUE....AN EXAMPLE GIVEN ABOVE");
emergencyContactList.add("+1CONTINUE...");
emergencyContactList.add("+1CONTINUE...");
```
Now the project can be executed.

Application.java is the main program which needs to be executed. 
Just run Application.java and click the distress button. The jar files have been stored in lib folder in the Web Services P1 folder. Add the jar files to the path if needed. 
