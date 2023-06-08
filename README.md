

TravelGenius

Description

TravelGenius is a comprehensive web application developed to simplify your travel planning and management. Our application integrates various travel-related functionalities such as searching for flights and hotels, as well as keeping track of your expenses using a built-in budget manager.

Our backend is powered by Spring Boot and MongoDB, making use of RESTful APIs to handle requests and persist data. With MongoDB, we are able to handle a large amount of data and its various types, which is essential for a travel application dealing with numerous records of flights, hotels, and user data.

On the front end, we use React to build a dynamic and responsive user interface. React allows us to efficiently update and render components in our application, providing a seamless user experience.

Functionality

With TravelGenius, you can:

Search Flights: This feature allows users to search for flights based on different criteria like departure city, destination city, date, and more. It fetches real-time data, ensuring the information is up-to-date.
Search Hotels: Similar to flight search, users can search for hotels in a specific city for their travel dates. It presents users with a list of available options along with key details to help them make an informed decision.
Budget Manager: This is a personal finance tool integrated into the app. As you add flights and hotels to your itinerary, their costs are automatically added to your budget manager. This feature allows users to track their spending and manage their travel budget effectively.
TravelGenius, with its blend of robust backend and interactive frontend, aims to make travel planning an enjoyable and easy process. From searching for your next flight or hotel to managing your travel budget, everything is now available at your fingertips!


Backend - Spring Boot Application

The backend of this application is developed using Spring Boot, which is a Java-based framework used to create stand-alone, production-grade Spring applications. The project uses MongoDB as the database, providing flexibility and scalability for our data.

To manage dependencies and automate our build, we are using Gradle, a powerful build tool for JVM languages.

When you install the project do a gradle refresh.



################
MongoDB Compass
################

* Introduction

MongoDB Compass is a graphical user interface (GUI) tool that allows you to interact with MongoDB databases. This README provides instructions on how to run MongoDB Compass on your system.

* Prerequisites

Before running MongoDB Compass, ensure that you have the following prerequisites installed on your system:

Operating System: MongoDB Compass is compatible with Windows, macOS, and Linux.
MongoDB: Install MongoDB Community Edition from the official MongoDB website (https://www.mongodb.com/try/download/community).
MongoDB Compass: Download the MongoDB Compass installation package for your operating system from the official MongoDB website (https://www.mongodb.com/try/download/compass).
Installation

Download the MongoDB Compass installation package for your operating system from the MongoDB website.
Run the downloaded installation package and follow the installation wizard instructions.
Once the installation is complete, MongoDB Compass will be installed on your system.
Running MongoDB Compass

* To run MongoDB Compass, follow these steps:

Windows:
Open the Windows Start menu.
Search for "MongoDB Compass" and click on the MongoDB Compass icon to launch the application.
MongoDB Compass will open, and you will see the connection screen.
macOS:
Open the Launchpad or go to the Applications folder.
Locate the MongoDB Compass application and click on it to launch.
MongoDB Compass will open, and you will see the connection screen.

*Connecting to MongoDB

Once MongoDB Compass is running, you can connect to a MongoDB database by following these steps:

In the MongoDB Compass connection screen, enter the connection details for your 

* MongoDB server:
Hostname: The hostname or IP address of your MongoDB server - localhost
Port: The port number on which MongoDB is running (default is 27017).
Authentication: If your MongoDB server requires authentication, check the "Authentication" box and provide the username and password.
SSL: If your MongoDB server is configured to use SSL, check the "SSL" box.
Click on the "Connect" button to establish a connection to the MongoDB server.
Once connected, you will see a list of available databases on the left sidebar. Click on a database to explore its collections and documents.
Conclusion

Congratulations! You have successfully installed and launched MongoDB Compass. You can now connect to your MongoDB server and interact with your databases using the graphical interface provided by MongoDB Compass. Enjoy exploring your data!


#####
React
#####


1. Clone the repository or import to folder '2023b.omer_iny_react':    
git clone https://YOURUSERNAME@bitbucket.org/travelappdevintegrative/2023b.omer_iny_react.git

2. Navigate to the project directory:
   	cd travel-genius
    
3. Install the dependencies :
    	npm install --legacy-peer-deps
    
4. Make sure that the spring application is up and running

5. Start the development server:
    	npm start
