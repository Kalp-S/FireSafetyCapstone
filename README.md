# FireSafetyCapstone

## Background
  The Fire Safety system was a 4rth year capstone project. The project was based around improving fire safety measures in high density urban environments, especially in high rise buildings. 

## What does the project do?
  This project aims to improve fire safety in urban environments by creating a network of nodes connected to a centralized gateway using LoRa protocol, a long range iot communication protocol. In emergency fire situations, date from nodes is fed to a Machine Learning algorithm, which generates an escape route that users can follow.
  
 ## How does the project work?
  All nodes on the network transmit sensor data (temperature, CO concentration, relative humidity) to a LoRa gateway in the form of LoRa packets.The LoRa gateway receives LoRa packets, converts them into MQTT packets and forwards MQTT packets to a Node-Red flow, which is used to process the data. The Node-Red flow congregates data from all nodes on the system, and using certain conditions determines if a fire situation exists or not. In case of a fire, information is forwarded to a machine learning algorithm to generate the best possible escape path for a person. Using an MQTT broker, the escape path information is then forwarded to the Android application, where it is processed and displayed to the user.

Data Flow Model

  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture.png "Data Flow Model")
  

## Screenshots:
  Application Home Screen
  
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture2.png "Application Home Screen")
  
  Fire Escape Route Screen
  
  ![alt text](https://raw.githubusercontent.com/Kalp-S/FireSafetyCapstone/master/Pictures/picture3.png "Fire Escape Route")
  
  
## Required Dependencies
 - Node-Red
 - Python
 - Android Studio
 - Arduino 
