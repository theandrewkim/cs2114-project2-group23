# cs2114-project2-group23
Group 23: Alper, Andrew, Nikitha

We are building Ride Match, which is a program that helps users choose which rental vehicle option is right for them. Users can filter out the options by setting preferences, such as vehicle type, (either motorcycles or cars), passenger number, storage size, engine type, etc. Also, users may upload, update, or remove any option, just in case they don’t want or need it anymore. 


**Attribution of Prototype + Tests**

1. Method stubs and class including main method / 2. Complete methods, write test cases / 3. Integration of classes and Testing, debugging if necessary
- Roles above are grouped with similar works and ordered in a way that should be completed
- We will not have methods all completed; just few methods at the moment

Alper: 3. Integration of classes and Testing, debugging if necessary

Andrew: 1. Write method stubs and class including main method

Nikitha: 2. Complete methods, write test cases


**Reflections:  what changed between your specification and what you actually built**

- we have changed parameter types for some parameters, since we discovered we have set the type wrong
- we have removed the parameter "eco": this was meant to help user make ethical choices, but the criteria was quite vague - is the vehicle simply eco/non-eco? Can we categorize?
- following the revised types, mileage is a double, transmission is an int (Car.AUTOMATIC = 0, Car.MANUAL = 1) and autopilot is an int level from 0-5, so searchTrans, searchAuto, promptTrans and promptAuto use int instead of String/boolean
- after adding a vehicle, addItem() asks "Add another vehicle? (y/n)" (uses readYesNo) instead of calling itself recursively
- searchResults() stops asking for more filters once nothing matches, and returns a new chain even when no filter is applied, so the inventory is never handed out to be changed

**Next Steps: what you will do next to extend this prototype toward the final**

- make sure our code can block invalid inputs


--End of work by October 14, 2026--
