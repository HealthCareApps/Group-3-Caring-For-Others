# Group-3-Caring-For-Others
Team Members: Nick G., Nicholas A., David G., Genesi, and Gus. 
Topic: Caring for Others

4/19/16 - We have moved to a more specific home care (Physical Therapy). By the end of the week we hope to have the app moved over to reflect Physical Therapy

Some changes made:
    Moved database over to Sqlite for faster access and allow the user to view information without 
    having to be connected to the internet
        Still need to able to connect to the internet in order to see updated logs, edit information, and adding information
        
    App will be available across multiple devices. Meaning user doesn't have to lose or re-enter any information
    if they sign in on another device other then the original device that they've registed in.
    
    AR will not be implemented anymore
    

Changes:

The app will consist of two profiles. There is no patients profile anymore

    Primary CareTaker - The main person taking care of the patient that will
	supply the information about the patient.
	
	Nurse/Family member - Person taking care of the patient, needs access to
	info with permission from the primary caretaker


The primary caretaker will need to put in all the information about the patient 
	- Health Insurance
	- Daily routines
	- Prescriptions
	- Personal info - name, age etc

The Nurse/Family member will have access to all the infomation but not be able to edii
	 - They will have access to a log that will show logs of others and any information
	 that was entered. Routines that they didn't or did do, prescriptions that were not 
	 taken etc..

Alerts/Reminders 
	- The primary caretaker will set up reminders and alerts for routines and prescriptions
	for nurses. (Need to figure out if we can set up nurses timeblocks so they're not alerted if
	they are currently not taking care of patient)


AR Portion idea:

	With the informtation inputted by the primary caretaker VuMark codes can be printed out for the 
	patient and for prescriptions.

	The patient VuMark will display to the user recent logs and other information that a nurse may first look at

	The Prescriptions VuMark will display last time the patient took the medication and any logs that may have
	been put in
