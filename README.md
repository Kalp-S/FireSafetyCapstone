# FireSafetyCapstone

## Background
  The Fire Safety system was a 4th year capstone project. The project was based around improving fire safety measures in high density urban environments, especially in high rise buildings. 

## What does the project do?
  This project aims to improve fire safety in urban environments by creating a network of efficient lowe power nodes connected to a centralized gateway using LoRa protocol, a long range, low power iot communication protocol. In emergency fire situations, date from nodes is collected and fed to a Machine Learning algorithm, which generates an escape route that users can follow.
  
 ## How does the project work?
  All nodes on the network transmit sensor data (temperature, CO concentration, relative humidity) to a LoRa gateway in the form of LoRa packets.The LoRa gateway receives LoRa packets, converts them into MQTT packets and forwards MQTT packets to a Node-Red flow, which is used to process the data. The Node-Red flow congregates data from all nodes on the system, and using specific conditions, determines if a fire situation exists or not. In case of a fire, information is forwarded to a machine learning algorithm to generate the best possible escape path for a person. Using an MQTT broker, the escape path information is then forwarded to any number of Android applications, where it is processed and displayed to the user.

Data Flow Model

  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture.png "Data Flow Model")
  

## Screenshots:
  Node-Red Flow #1 (Intake + Preprocessing). We simulated the system with 11 nodes here, which we believe is suitable for a single floor.
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture4.png "Intake and Preprocessing")


  Node-Red Flow #2 (Fire processing + ML)
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture5.png "Fire Processing and Machine Learning")
 
 
 
  Application Home Screen
  
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture2.png "Application Home Screen")
  
  
  Safety Evacuation Screen
  
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture3.png "Fire Escape Route")
  
  
## Required Dependencies
 - Node-Red
 - Python
 - Android Studio
 - Arduino 
