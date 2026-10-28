The app primary goal is to make a http request to an endpoint and show formatted message depending on the response.

## Functionalities required

1. one config button navigating user to cofigure menu when no config exist.
2. In the config page, user can set an endpoint and the http request type, get/post, and parameters for the json payload, and criteria for success/failure message response [ a contains string, if the response contains that string is success, so it should accept json key path, where to look for in the response and what content match means success]
3. The required paramtere for the endpoint are:
single input str fiels: "userId" , "userEmail"
then a +/- button to add/remove "lat", "long" pair for location/locations
4. when user will click on plus, it will ask the user to set "lat", "long" against a location name

5. user can add minimum one or more lat,long for different locations by pressing +, or remove them be pressing delete icon present in these location name,
6. a save button will persist this config even when the app is reopend after closing
7. saving the config will bring them to the home screen where this configured location names will appear in a map with a button, and click of the button will send configured http request, a groow up from bottom screen will have  success/ failure confirmation along with a small button to see raw response and status code, network failure status,
8. A log history of past send request of last 7 days.
9. one vital requirement is that the set lat,long should not be used, a random +/- within 100 meters of that lat long should be used in the request, and tha will be logged in log.