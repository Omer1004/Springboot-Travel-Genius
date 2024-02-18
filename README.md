# TravelGenius: A Comprehensive Travel Planning App

Welcome to the GitHub repository of **TravelGenius**, a cutting-edge travel planning application designed to revolutionize the way you plan your trips. Our app is built using a robust stack including Java, Spring Boot for the backend, React for the frontend, and MongoDB for the database. This combination ensures a seamless, efficient, and scalable solution for all your travel planning needs.

## Project Structure

TravelGenius's codebase is divided into two separate repositories to maintain a clear separation between the frontend and backend codebases. This approach allows for more focused development and easier maintenance of each part of the application.

- **Frontend Repository**: The frontend, built with React, provides a dynamic and responsive user interface for planning your travels. Access the frontend repository [here](https://github.com/Omer1004/Travel-Genuis-React/tree/main).
- **Backend Repository**: The backend, powered by Spring Boot, handles data management, API integrations, and server-side logic. Access the backend repository [here](https://github.com/Omer1004/Springboot-Travel-Genius).

## Features

TravelGenius combines three powerful mini-applications to offer an unparalleled user experience:

1. **Flight Booking**: Seamlessly integrated with SkyScanner, enabling users to find and book flights directly within the app.
2. **Hotel Reservations**: Partnered with Booking.com, this feature allows users to reserve accommodations that fit their budget and preferences.
3. **Budget Planner**: A unique tool where users can input their total trip budget, and TravelGenius suggests the best flight and hotel options within that budget. It includes customizable options for allocating more budget to either flights or hotels.

## Getting Started

To get started with TravelGenius, clone the respective repositories and ensure you have the following prerequisites installed:

- Java JDK 11 or later
- Node.js and npm for running the React frontend
- MongoDB installed locally or access to a MongoDB database

### Backend Setup

1. Navigate to the backend directory: `cd backend`
2. Use Maven to install the dependencies: `mvn clean install`
3. Run the Spring Boot application: `mvn spring-boot:run`

### Frontend Setup

1. Navigate to the frontend directory: `cd frontend`
2. Install the required npm packages: `npm install`
3. Start the React application: `npm start`
4. The app will be available at `http://localhost:3000`

## Usage

- Start by setting your travel budget in the Budget Planner.
- Explore flight options and book directly.
- Browse and reserve hotels.
- Adjust your budget preferences as needed to find the perfect balance for your trip.

## Contributing

We welcome contributions to TravelGenius! If you have suggestions for improvements or bug fixes, please feel free to make a pull request or open an issue.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Thanks to TripAdvisor APIs for integration.
- You may need to enter your own api keys 
- React Input Range for the budget slider component.
- React Bootstrap for UI components.

Enjoy planning your trip with **TravelGenius**, your ultimate travel companion!
