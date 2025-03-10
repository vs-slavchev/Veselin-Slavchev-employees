# Employee interview coding task

## Running locally
1. `docker-compose up` in the root of the repo
2. go to [localhost:3000](http://localhost:3000)

## Context
An application that identifies the pair of employees who have worked together on common projects for the longest period of time.

## High level
A web app allows the user to select a CSV with times employees worked on projects.

A backend service processes the data and returns the pairs that worked the longest together.

The web app displays the result in a table.

## Detailed design
### Backend service
Contains:
 - algorithm to find the pairs that worked on the same project the longest
 - handlers of the request containing the CSV
 - parser of the CSV
 
Tech:
 - Spring boot with Spring web
 
Model of response: `Employee ID #1`, `Employee ID #2`, `Project ID`, `Days worked`
 
### Web app
Tech:
 - React

### Infra
The two services are on docker and can be started with docker-compose.

## out of scope
Merging of time periods if there are multiple lines stating an employee worked on a project in different overlapping periods.

## further steps
 - monitor algo performance and optimise if necessary
 - monitor parsing of various date formats and optimise if necessary
