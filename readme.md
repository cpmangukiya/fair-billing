# Fair Billing - Calculate user-wise sessions details

This app calculates and prints users' sessions details with information on number of sessions and total active duration for given log file

The result or error should be visible in the consol/terminal after running the program.

Pre Steps
------------
- Make sure JDK 1.8 is installed and configured in the system
- Compile the code using command : javac FairBilling.java // Found in dir: src/com/company/billing/

How to use ?
------------
- Keep a log file with session details in a desired directory
- Assess the file using command : java FairBilling <file-path>/<file-name>
- Examples,
  - Relative Path : java FairBilling resources/samples/input-sample-1.txt
  - Relative Path : java FairBilling ../../../resources/samples/input-sample-1.txt
  - Relative Path : java FairBilling input-sample.txt
  - Absolute Path : java FairBilling C:/SampleWindowsDirectory/input-sample.txt
  - Absolute Path : java FairBilling /SampleLinuxDirectory/input-sample.txt

Assumptions ans Samples
------------
- Assumption
  - Data in the input will be correctly ordered chronologically, and that all records in the file will be from within a single day (i.e. they will
  not span midnight)
- Sample files : 
  - resources/samples/input-sample-1.txt : This file contains all valid activities
  - resources/samples/input-sample-2.txt : This file contains only few corrupt activities and rest all valid
  - resources/samples/input-sample-3.txt : This file contains data where activities records are not from single day. (Note : This breaks our assumption and produces undesired data)
  - resources/samples/input-sample-4.txt : This file contains no valid activities
  - resources/samples/input-sample-5.txt : This file is empty 