# Fair Billing - Calculate user-wise sessions details

This app calculates and prints users' sessions details with information on number of sessions and total active duration for given log file

The result or error should be visible in the consol/terminal after feeding valid log file as input.

Pre Steps
------------
- Make sure JDK 1.8 is installed and configured in the system
- Compile the code using command : javac FairBilling.java

How to use ?
------------
- Keep a log file with session details in a desired directory
- Assess the file using command : java FairBilling <file-path>/<file-name>
- Examples,
  - Relative Path : java FairBilling resources/samples/input-sample-1.txt
  - Relative Path : java FairBilling input-sample.txt
  - Absolute Path : java FairBilling C:/SampleWindowsDirectory/input-sample.txt
  - Absolute Path : java FairBilling /SampleLinuxDirectory/input-sample.txt