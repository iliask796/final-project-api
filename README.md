# Final Project (Oct. 12 - Oct 31)
This is going to serve as a planning reference for my project. I am planning to work on a Project
Management Tool. I am going to build a Spring Boot Application that will implement an API which will
get data from a mySQL Local Server and provide them to my React App.

## Core Functionality
I am going to focus first on building some core aspects of the project and if these are done and 
there is still time left, then I will move on into extending this functionality.

### Front-End
The primary focus will be to develop a user interface that will resemble the looks and some of the
core functionality of a tool like Hive.
<br> <br>
![hive-example-img](assets/Hive-status-view-board-example-1400x646.png) 
<br><br>
Alternatively some of the UI or functionality can also resemble a tool like Teamwork.
<br><br>
![teamwork-example-img](assets/Teamwork-Board-View-Content-Dashboard-Example.png)
<br><br>
If there is plenty of time the desired end-goal could be something that resembles a tool like Trello.
<br><br>
![trello-example-img](assets/trello-1-1400x951.png)

### ER Diagram
A user has an email, a username and a password. A user can have one workspace. A workspace has a name.
A workspace can contain many task lists. A task list has a name and an order. Each task list consists
of different tasks. A task has a name, a description and an order.
<br><br>
![er-diagram-img](assets/ER_Diagram.png)

### API Spec
#### Users
- POST /users <br> Create a new user.
- GET /users/{id} <br> Get a specific user by his id.
#### Workspaces
- POST /users/{id}/workspaces <br> Create a new workspace for the user of this id.
- GET /users/{id}/workspaces <br> Get the workspace of a user by his id.
- PUT /users/{id}//workspaces <br> Update the workspace of a user by his id.
- DELETE /users/{id}/workspaces <br> Delete the workspace of a user by his id.
#### Tasklists
- POST /workspaces/{id}/tasklists <br> Create a new tasklist for the workspace of this id.
- GET /workspaces/{id}/tasklists <br> Get all tasklists of a workspace by its id.
- PUT /tasklists/{id} <br> Update a tasklist by its id.
- DELETE /tasklists/{id} <br> Delete a tasklist by its id.
#### Tasks
- POST /tasklists/{id}/tasks <br> Create a new task for the tasklist of this id.
- GET /tasklists/{id}/tasks <br> Get all tasks of a tasklist by its id.
- PUT /tasks/{id} <br> Update a task by its id.
- DELETE /tasks/{id} <br> Delete a task by its id.
<br><br>
![endpoints-img](assets/Endpoints.PNG)

### Plan with Hive
I am going to use Hive to manage the progress of my project. This way I can be more organised about
the planning process and at the same time experiment live with a tool similar to what I am developing.
<br><br>
Example:
![hive-in-action-img](assets/Hive-Live-Example.png)
