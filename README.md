# Suggest Nearby Cities REST API

This REST API suggests nearby cities based on the name of the location, its latitude, and longitude.

## Usage

**REST API Consumption Validation URL**

**Type:** GET

https://springgcp-396905.nn.r.appspot.com/suggestions?q=london&lat=42.98339&lon=-81.23304

**URL Parameter Values**

- `q`: Location name (String)
- `lat`: Latitude (Float > 0)
- `lon`: Longitude (Float < 0)

**Response Format**

The response generated by the API consumption will have a format similar to the following:

```json
[
    {
        "name": "london : America/Toronto",
        "latitude": 42.98339,
        "longitude": -81.23304,
        "score": 1.0
    },
    {
        "name": "london : America/New_York",
        "latitude": 39.88645,
        "longitude": -83.44825,
        "score": 0.002552867223175697
    },
    {
        "name": "london : America/New_York",
        "latitude": 37.12898,
        "longitude": -84.08326,
        "score": 0.0014376255204344688
    }
]

```

Or without responses:

  https://springgcp-396905.nn.r.appspot.com/suggestions?q=SomeRandomCityInTheMiddleOfNowhere&lat=0&lon=0

```
  []
```

Explanation

In each nesting of variables, we have the following variables:

name: Name of the location most closely related to the location entered by the user in the q variable in the URL.

latitude: Latitude

longitude: Longitude

score: The confidence level indicating whether the location entered by the user is correct or not.

This REST API is used to detect the closest locations with points of interest for a user and works as follows:

There is a database with names of locations and their respective latitude and longitude, containing thousands of points in Canada and the United States of America. While the service primarily focuses on North American users, the database can accommodate locations with points of interest worldwide. In the example provided, a user looking for a location called "london" at the coordinates 42.98339 latitude and -81.23304 longitude had a successful match with their query. Additionally, 2 more locations were found, but in different areas. The scores indicate that the relationship of the location name with the latitude and longitude entered by the user does not match other locations. This computational model automatically learns that the user's known location is in "Canada."

This was accomplished using an algorithm based on two feature extraction stages. The first stage employed the Cosine function, and the second stage utilized the SemiVersine function. The structure of the REST API is explained in detail below.

Feature Extraction
Taking into account the structure of the database named "cities_canada-usa.tsv," the first stage of feature extraction involves two techniques:

1. Cosine:
In this first stage, a Machine Learning technique widely used in the Natural Language Processing area is used, mainly for the analysis, search and retrieval of information, which is known as "Cosine Similarity", with this measure of similarity the location name entered by the user (variable "q") and is made up of the names of all the locations in the database (cities_canada-usa.tsv) and if 1 or more locations are found with related names (similar or identical ) ) are retrieved, otherwise nothing is retrieved. At the same time, their respective variables "lat" and "lon" are also recovered, which are transformed by a function in the next stage.

Learn more about Cosine Similarity: https://es.wikipedia.org/wiki/Similitud_coseno#:~:text=El%20Coseno%20Suave%E2%80%8B%20es,similitud%20entre%20pares%20de%20caracter%C3%ADsticas

2. Semiversene:
In this stage, the Semiversene formula is used, which takes "the radius of the earth of 6371kms" as a frame of reference. It uses a function to automatically generate the degree of similarity/difference between the latitude and longitude entered by the user and the latitude and longitude of the locations retrieved in the previous stage (cosine).

Learn more about Semiversene: https://es.wikipedia.org/wiki/F%C3%B3rmula_del_semiverseno

The project directory tree is as follows:
```
Suggests
|________ src/main/java
|______________________ com.example.demo
|_______________________________ CosineSimilartyCalculator.java
|_______________________________ HaversineDistanceCalculator.java
|_______________________________ SuggestionsController.java
|_______________________________ SuggestsApplication.java
|_______________________________ TermFrequencyCalculator.java
|
|________ src/main/resources
|______________________ cities_canada-usa.tsv
|
|________ src/test/java
|______________________ com.example.demo
|_______________________________ SuggestsApplicationTests.java

```

**Suggests Application**:
Project main class:

**TermFrequencyCalculator**:
Before generating the Cosine Similarity between the city (q) and the cities of the database.tsv, the corresponding text strings must be transformed into vectors. This calculation cannot be performed on text strings, but it can be done if they are represented numerically.

**CosineSimilartyCalculator**:
Once the city names are represented as vectors, they can be compared by Cosine Similarity using this class. If cities related to the city entered by the user are found in the .tsv file, they are recovered and displayed.

**HaversineDistanceCalculator**:
After retrieving the related locations, the Haversine Distance is generated between the latitude and longitude entered by the user in the URL (value q) and the latitudes and longitudes of each retrieved location. The results are ordered in descending order, starting with the one with the greatest similarity and ending with the least similarity. A similarity of 1 (100%) means that the location is completely valid and can be trusted. A similarity closer to 0 indicates little probability of being related to the user's intentions and is not trusted to be the verified location.

**SuggestionsController:
This controller is used to load the data bank (cities_canada-usa.tsv). It first executes the Cosine calculation and then the Semiversene calculation.**

**cities_canada-usa.tsv:**
This is a data bank with location names, latitude, and longitude.

**Suggests Application Tests:**
Unit tests.

## Testing

The tests that were carried out were the following:

1. **Unit tests**:
   Junit was used for these tests, including 6 locations. The first 3 locations have a confidence level of 100%, while the last 3 locations have little confidence, tending towards 0.

2. **Integration tests**:
   POSTMAN was used for these tests, specifically testing the same 6 locations used in the unit tests. These locations were consumed using the GET method. The following URLs correspond to the data recovery from the unit tests:


    - Suggestions for London:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=london&lat=42.98339&lon=-81.23304](https://springgcp-396905.nn.r.appspot.com/suggestions?q=london&lat=42.98339&lon=-81.23304)

   - Suggestions for Ajax:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=ajax&lat=43.85012&lon=-79.03288](https://springgcp-396905.nn.r.appspot.com/suggestions?q=ajax&lat=43.85012&lon=-79.03288)

   - Suggestions for Bel Air:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=bel%20air&lat=39.53594&lon=-76.34829](https://springgcp-396905.nn.r.appspot.com/suggestions?q=bel%20air&lat=39.53594&lon=-76.34829)

   - Suggestions for Kingston:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=kingston&lat=39.53594&lon=-76.34829](https://springgcp-396905.nn.r.appspot.com/suggestions?q=kingston&lat=39.53594&lon=-76.34829)

   - Suggestions for Lyndon:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=lyndon&lat=39.53594&lon=-76.34829](https://springgcp-396905.nn.r.appspot.com/suggestions?q=lyndon&lat=39.53594&lon=-76.34829)

   - Suggestions for Berlin:
     URL: [https://springgcp-396905.nn.r.appspot.com/suggestions?q=berlin&lat=39.53594&lon=-76.34829](https://springgcp-396905.nn.r.appspot.com/suggestions?q=berlin&lat=39.53594&lon=-76.34829)


Both the unit tests and integration tests produced consistent results, as the algorithm accurately generates responses based on each query.

After 4 years as a software developer and practicing in the fields of information technology and scientific computing, I have gradually managed to develop a concept that we have named "Computer Aided Cognition" which is a paradigm/approach for solve computational problems using service-oriented architecture in combination with machine learning and neural networks, with which it is possible to automate decision making by making a more robust REST API in terms of complexity of algorithmic resolution, orienting the results to the optimization of the different types of tests that guarantee the reliability of a program.

We consider Computer Aided Cognition a successor to Artificial Intelligence, which is barely in its first days of existence, which is why it is under development and is currently a concept that I personally use within the technology industry for solving of problems in companies in which I provide my services, in the future the concepts of Computer Aided Cognition will be released by a GNU license because it is currently in an initial stage of development and requires extensive work where in these dates experimentation is the main stage. If you want to know more about computer-assisted cognition, you can contact our team at the following URL:
https://portafolioarmandorojas.000webhostapp.com/#contact