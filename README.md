# Smart Agriculture System
## System structure
![Screenshot 2024-09-30 231607](https://github.com/user-attachments/assets/1ba7cdd7-d9cc-42a8-ad12-7288ca55c272)

- **Central Station:** functions as the core router, where incoming raw data is analyzed, stored and make decision of the system
- **Sensor Nodes:** responsible for collecting environmental parameters
- **Motor nodes:** execute commands from Central Station such as watering, heatting, ..

## Hardwawre and Networking
![image](https://github.com/user-attachments/assets/35881707-7647-4be3-8546-b43baa236f5e)

- **Sensor Node & Motor Node:** uses `KIT EK2703A` (Silicon lab kit) as MCU, actuators will be introduced detail in report.
- **Central Station:** utilizes `Ras Pi-4` which supports internet accessibility as a gateway of whole system.
- **Application:** where user can interact with the system, can manage and observe their green field.
- **Cloud:** here the system deploys a host for database, also supply API to interact between hardwares and user application.

## Features
- User interface (parameter configurable, operating screen with nodes)
- Mananging accounts (password encryption, notifications)
- Automatic device pairing
- Encrypt data on transmiting line
- Save power mode
- Manual mode and auto mode


## Results
![image](https://github.com/user-attachments/assets/389ac458-c3d1-4be6-9c51-fbc44540b60a)


## User guide
