# ant-colony-optimization

Ant Colony Optimisation is a very versatile algorithm and was chosen to solve travelling salesman type problems due to its efficiency, the quality of the solutions that it produces and the ability to create an engaging GUI. This app takes a plain text file with up to 100 locations placed in successive rows, where the first element is the name, composed of up to ten characters, followed by a space and an int representing its x coordinate, another space and an int representing its y coordinate (eg. mytown 677 890).

The coordinate units are not defined and can be whatever the user decides them to be, however to make viewing possible, limits were set from 0 to 99999. If stripped of its graph component, the algorithm can handle large lists of varied locations.

This algorithm uses dependency injection with guice, multithreading with Executors and SwingWorkers, project object model with Maven and a great visual experience provided by GraphStream.

The GUI was created using Swing, given GraphStream's present incompatibility with JavaFx.



https://user-images.githubusercontent.com/32436981/217868426-3bf9d973-b0e5-47c4-a1dc-aa2f72925205.mp4
