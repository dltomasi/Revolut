# Revolut

The app was made using MVVM architecture and RxJava to implement the Observable Pattern with async operations. Koin for dependency injection <br>

<b>Unit Tests:</b> <br>
All ViewModels and Interactors are fully tested by unit tests. I'm injecting the RxJava Schedulers so I have full control of execution time during tests.

<b>UI tests:</b><br>
Using Espresso and Robot Pattern to improve reading and organization. Note that only the UI is being tested, it's not an integration test since the network calls are being mocked. In order to do an integration test we just need to turn off the mocks.
<br>
https://jakewharton.com/testing-robots/
<br><br>
<b>Flags:</b><br>
Using an api to fetch country info like flag url and currency name. Note that the mapping of currency to country is 1-N as we can have more than 1 country using the same currency. So I created an mapping method to handle those specific cases. <br>
https://restcountries.eu/#api-endpoints-currency

<b>ViewModel:</b><br>
Using jetpack architecture component to make the viewmodel, so the state is kept when rotating the screen.
<br><br>
<b>Error:</b><br>
On any error a screen is displayed with an error msg and a button to retry.
<br><br>
<b>Possible improvements:</b><br>
- Create a retry handler to try to reconnect on error before displayng the error screen. <br>
- Improve layout performace removing nested layouts. Try to make just on level. <br>
- Limit ammount that can be set and apply mask.
