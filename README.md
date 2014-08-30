# San Francisco Movies

## Introduction

San Francisco Movies is a website at http://www.sfmovies.org that shows the movies that were filmed in San Francisco, CA on a map.

Users may browse the map of movie locations or use the search bar. The movies are visualized via a movie marker on the map. Clicking the markers will open an information window that lists detailed description of the movie, as well as links to watch online, purchase DVD, view on Wikipedia, tweet about it and a direct link for sharing the movie.

## Features

* Map: Users will see a marker for each location where a movie was filmed.
* Search bar: Users may search a movie based on its attributes such as title, actors, director, production company, and writer.
* Movie details: Users may click to a movie marker to view full information about the movie, as well as a link to watch it online, buy the DVD on Amazon.com, a permenant link for sharing, tweet about it, and read about the movie on Wikipedia or IMDb.

## Architecture

sfmovies.org is based on MVC design pattern and, it consists of 3 pieces:

* Frontend: A website or mobile app that the user can interact with.
* REST service: A web service that is used by the frontend to query data.
* Data store: A storage for the movies.

The architecture diagram can be found at https://docs.google.com/presentation/d/1-vqyHcqOb95hJMUxV5XueJW0Lh3EYYtH9FPZ6MVliRM/edit?usp=sharing

### Frontend

Currently the project has only a web interface but the architecture permits to create seemless mobile apps with no extra work in other components.

The web interface retrieves the data from the backend service via Ajax, and it formats the results via jQuery and CSS. The user is provided a map view and a search bar. In the mobile web version, the view is full screen, while in the desktop version, the user may choose between full screen and a visually enhanced theatre view.

Upon loading the web page, the page retrieves the list of movies in the current map view and places them into markers. If a particular location contains a single marker, clicking the marker displays the full movie information in an information window. If there are multiple markers at the same location, clicking such a marker will present additional markers where each marker opens its own information window. Dragging the map view causes the movie markers to be reloaded for the new map bounds.

The search bar allows the user to search in movie attributes such as the title, the actors, the production company, etc. Each possible result is prepended with the type of the search result, e.g. searching for "lint" would have a result like "Clint Eastwood (actor)" as well as other possible matches. The user may click the search result to load the appropriate result, e.g. clicking "Clint Eastwood (actor)" will load only the movies where Clint Eastwood acted.

### REST service

The REST service is a JSON/HTTP web service that runs on AWS Elastic Beanstalk, which enables autoscaling the service based on the load. The service is based on Spring MVC framework.

Upon the service start, the service retrieves the movie data from the data storage. All endpoints are prefixed with /v1/movies.

#### Customer facing interface

These methods were called by a frontend service such as the website or a mobile app.

##### /v1/movies/search
Takes a search string as an input and returns a list of search results, each containing a matching item and its type (e.g. actor, director, etc.)

##### /v1/movies/all
Takes the south west and north east corner coordinates of the view and returns a list of movie header (ID, title, locations) in the given boundries.

##### /v1/movies/movieId/{movieId}
Returns the movie header for a given movie ID. Used in deep linking (e.g. http://www.sfmovies.org/?movieId=ec772a74-87d9-4d2e-b889-96ab632641d9)

##### /v1/movies/movieDetail/{movieId}
Returns all the information for a given movie ID. Used to asynchronously fill the movie information window.

##### /v1/movies/actor/{actor}
Returns all the movies in which the parameter was an actor.

##### /v1/movies/director/{director}
Returns all the movies in which the parameter was the director.

##### /v1/movies/distributor/{distributor}
Returns all the movies in which the parameter was the distributor.

##### /v1/movies/productionCompany/{productionCompany}
Returns all the movies in which the parameter was the production company.

##### /v1/movies/releaseYear/{releaseYear}
Returns all the movies that were filmed in the given year.

##### /v1/movies/title/{title}
Returns all the movies with matching title.

##### /v1/movies/writer/{writer}
Returns all the movies in which the parameter was the writer.

#### Internal interface

##### /v1/movies/newMovie
Adds a formatted movie to the cache. If the movie does not exists, it creates a new entry, otherwise it adds the movie locations that were previously unknown.

##### /v1/movies/newMovies
Performs the same operation of /v1/movies/newMovie for a list of movies.

##### /v1/movies/newRawMovie
Adds an unformatted movie (from sfdata.org) to the cache. It converts the movie format by organizing actors and calculating the movie location coordianates. If the movie does not exists, it creates a new entry, otherwise it adds the movie locations that were previously unknown.

##### /v1/movies/newRawMovies
Performs the same operation of /v1/movies/newRawMovies for a list of movies.

##### /v1/movies/clear
Clears the cache.

##### /v1/movies/reloadCdnData
Clears the cache and reloads the data from the data source.

##### /v1/movies/printMovies
Lists all the movies.

### Data store

San Francisco Movies uses a CDN for a data storage. The CDN holds a list of formatted movies in a JSON file which gets loaded into the REST interface during initialization.

The movie service has a scheduled task to regenerate the movie list periodically and upload it to the CDN location. Upon then, the data will be reloaded in REST services.

I considered this approach better than a classic database/hibernate solution because of the following reasons:

* The data set is limited and the data is unlikely to change: The number of movies that were filmed in San Francisco is around 1000, and the list is unlikely to grow rapidly because filming a movie is a long term investment. Therefore the data can be considered almost static.
* The number of queries are high: The REST service will be hit extensively by the frontend interfaces. Having all the data preloaded into the web service allows the web service to respond the requests faster.
* Extensibility: The architectural choice between using a CDN vs a database can be switched easily. A future development effort on connecting a database via Hibernate on Cache.java class would make the REST interface use a database without changing other parts of the system.

## Data model

The data that is found on sfdata.org is in this format:
```
{  
   "title":"Bullitt",
   "actor_1":"Steve McQueen",
   "locations":"Taylor & Vallejo Streets (Russian Hill)",
   "release_year":"1968",
   "production_company":"Warner Brothers / Seven Arts\nSeven Arts",
   "distributor":"Warner Brothers",
   "actor_2":"Jacqueline Bisset",
   "writer":"Alan R. Trustman",
   "director":"Peter Yates",
   "actor_3":"Don Harvey"
},
{  
   "title":"Bullitt",
   "actor_1":"Steve McQueen",
   "locations":"SF General Hospital Center (1001 Potrero Avenue, Potrero Hill)",
   "fun_facts":"SF General Hospital is the only Level I Trauma Center serving San Francisco and northern San Mateo County. ",
   "release_year":"1968",
   "production_company":"Warner Brothers / Seven Arts\nSeven Arts",
   "distributor":"Warner Brothers",
   "actor_2":"Jacqueline Bisset",
   "writer":"Alan R. Trustman",
   "director":"Peter Yates"
}
[...]
```

I noticed several optimization opportunities in this format:


* Movie ID: The original array lacked an ID, however none of the information is unique enough to identify the movie (e.g. there can be multiple movies with the same title.)
* Actors: Actors stored in separate entries (actor_1, actor_2, actor_3), which could be combined into an actor array.
* Movie locations: If the movie is shot in multiple locations, there was a new entry for each location. All these records could be merged into a single movie information where the locations are stored in an array.
* Address: The original record set provided a casual address format that could be replaced with a full formatted address information (house number, street number, city, state, country).


Therefore I decided to store the information in the following format:
```
{  
   "movieSummary":{  
      "id":"ab1cb3c1-3203-4b82-bc02-dedff99bc513",
      "title":"Bullitt",
      "movieLocations":[  
         {  
            "latitude":37.8027632,
            "longitude":-122.4137383,
            "formattedAddress":"Columbus Avenue & Lombard Street, San Francisco, CA 94133, USA",
            "funFacts":null
         },
         {  
            "latitude":37.7482194,
            "longitude":-122.4182013,
            "formattedAddress":"Cesar Chavez Street & Mission Street, San Francisco, CA 94110, USA",
            "funFacts":null
         },
         {  
            "latitude":37.8023318,
            "longitude":-122.4496731,
            "formattedAddress":"U.S. 101, San Francisco, CA, USA",
            "funFacts":null
         },
         {  
            "latitude":37.7976826,
            "longitude":-122.432994,
            "formattedAddress":"2040 Union Street, San Francisco, CA 94123, USA",
            "funFacts":null
         },
         {  
            "latitude":37.793087,
            "longitude":-122.41047,
            "formattedAddress":"1000 Mason Street, San Francisco, CA 94108, USA",
            "funFacts":null
         },
         {  
            "latitude":37.748038,
            "longitude":-122.4132364,
            "formattedAddress":"Cesar Chavez, San Francisco, CA, USA",
            "funFacts":null
         }
      ]
   },
   "actors":[  
      "Steve McQueen",
      "Jacqueline Bisset",
      "Dermot Mulroney"
   ],
   "director":"Peter Yates",
   "distributor":"Warner Brothers",
   "productionCompany":"Warner Brothers / Seven Arts\nSeven Arts",
   "releaseYear":1968,
   "writer":"Alan R. Trustman"
}
```
