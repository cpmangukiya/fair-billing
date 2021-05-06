# Fair Billing - Calculate user-wise sessions details

This app calculates and prints users' sessions details with information on number of sessions and total active duration for input log file

The result or error should be visible in the consol/terminal after running the program.

Pre Steps
------------
- Make sure JDK 1.8 is installed and configured in the system
- Compile the java files ActivityInfo.java SessionInfo.java FairBilling.java

How to use ?
------------
- Keep a log file with session details in a desired directory
- Assess the file using command : java FairBilling [file-name]
- Examples,
  - java FairBilling ../resources/samples/input-sample-1.txt
  - java FairBilling input-sample.txt
  - java FairBilling C:/folder/resources/samples/input-sample.txt
  

- Alternatively this project can be imported and run into in IDE. ex, IntelliJ etc.


- For quick hands-on :
  - Got to src/main/java
  - Compile the files : javac ActivityInfo.java SessionInfo.java FairBilling.java  
  - Run the program : java FairBilling ../resources/samples/input-sample-1.txt

Assumptions and Samples
------------
- Assumption
  - Data in the input shall be correctly ordered chronologically, and that all records 
    in the file shall be from within a single day (i.e. they will
  not span midnight)
  - There may be invalid or irrelevant data within the file. Therefore, any lines
    that do not contain a valid time-stamp, username and a Start or End marker shall 
    be silently ignored and not included in any calculations.
- Sample files (Inside src/main/resources/samples) : 
  - input-sample-1.txt : This file contains all valid activities
  - input-sample-2.txt : This file contains only few corrupt activities and rest all are valid
  - input-sample-3.txt : This file contains data where activities records are not from single day. (Note : This breaks our assumption and produces undesired data)
  - input-sample-4.txt : This file contains no valid activities
  - input-sample-5.txt : This file is empty 
