# Water Collector
## About
Simple application made for CRX Markets requirement process. 
It exposes REST API for calculating water remaining after rain on given slice of surface.

## API
### Resources
Sample calls can be explored using Postman collection from root of the repository.
#### Water volume
This resource represents volume of water left on given surface after the rain.
* Method: `GET`
* Path: `/water/volume`
* Headers:
    ```
    Accept: application/json
    ```
* Parameters:
    * `bars` - (required) - comma separated list of integers representing heights of consecutive parts of the surface
* Response:
    ```json5
    {
        "surface": [], // list of integers representing given surface
        "volume": 1 // integer representing volume of water left after the rain
    }
    ```
* Possible errors:
    * `MISSING_PARAMETER` - when `bars` are not specified
    * `INVALID_BAR_HEIGHT` - when one or more bars have negative value
* Example:
  ```
    GET /water/volume?bars=1,2,4,1,2,4
    Accept: application/json
  
    200 OK
    Content-Type: application/json
    {
        "surface": [1, 2, 4, 1, 2, 4],
        "volume": 5
    }
  ```

#### Water volume details
This resource represents details of water's volume and surface state after the rain. 
It can be used for example for generating visual representation of calculations result.

This endpoint uses different, more resource demanding algorithm than the basic endpoint.

* Method: `GET`
* Path: `/water/volume/details`
* Headers:
    ```
    Accept: application/json
    ```
* Parameters:
    * `bars` - (required) - comma separated list of integers representing heights of consecutive parts of the surface
* Response:
    ```json5
    {
        "surface": [], // list of integers representing given surface
        "volume": 1, // integer representing volume of water left after the rain
        "barsVolume": 1, // integer representing volume of bars in the slice
        "emptyVolume": 1, // integer representing volume of empty space after the rain
        "width": 1, // integer representing width of the surface
        "height": 1, // integer representing height of highest bar in the slice
        "stripes": [["EMPTY", "WATER", "BAR"]] // list representing the surface view after the rain 
    }
    ```
* Possible errors:
    * `MISSING_PARAMETER` - when `bars` are not specified
    * `INVALID_BAR_HEIGHT`- when one or more bars have negative value
* Example:
  ```
    GET /water/volume/details?bars=1,2,4,1,2,4
    Accept: application/json
  
    200 OK
    Content-Type: application/json
    {
      "surface": [1, 2, 4, 1, 2, 4],
      "volume": 5,
      "barsVolume": 14,
      "emptyVolume": 5,
      "width": 6,
      "height": 4,
      "stripes": [
        ["EMPTY","EMPTY","EMPTY","BAR"], ["EMPTY","EMPTY","BAR","BAR"], ["BAR","BAR","BAR","BAR"], ["WATER","WATER","WATER","BAR"], ["WATER","WATER","BAR","BAR"], ["BAR","BAR","BAR","BAR"]
      ]
    }
  ```

### Errors
The API reports errors in common format:
```json5
{
  "code": "", // code of the error
  "path": "", // request path
  "message": "", // user-friendly message
  "details": {} // key-value pairs of error-specific data
}
```

## Development
### Testing
Application code is covered by unit & integration tests. Basic e2e tests are implemented in Postman collection.

### CI
Continuous integration of the project is implemented using GitHub Actions. 
Each push triggers tests runs as well as building of jar and Docker image. Only changes with all checks passed can be merged.

## Deployment
This application can be run on every platform supporting JVM and TCP sockets. 
Docker is used as example. Provided Dockerfile shows steps necessary to create an executable for the project. 
It features configurations for two separate environments. Application properties files are included after building the jar, which means configurations can be changed without a need for rebuilding the package.
