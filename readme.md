# Read Me First
The parking system has been implemented using jdk 1.8

The project can be executed by maven

# Assumptions
The decode store has been simplified using Set as the requirement is to check whether the vehicle is in the parking or not. 

And the last reference is not sent when polling the camera. If necessary the Set can be replaced by a List and both entry and exit decodes can be added there. The last reference can be retrieved from the decode store, and can be sent when polling the camera.
