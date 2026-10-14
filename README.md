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

- we have changed parameter types for some parameters, since we discovered we have set the type wrong. We have a separate document in this repository named "parameterTypeRevised" listing the revisions. 
- we have removed the parameter "eco": this was a stretch goal and meant to help user make ethical choices, but the criteria was vague and hard to define at the moment with our knowledge - is the vehicle simply eco/non-eco? Can we categorize ecofriendly-ness?
- following the revised types, mileage is a double, transmission is an int (Car.AUTOMATIC = 0, Car.MANUAL = 1) and autopilot is an int level from 0-5, so searchTrans, searchAuto, promptTrans and promptAuto use int instead of String/boolean
- after adding a vehicle, addItem() asks "Add another vehicle? (y/n)" (uses readYesNo) instead of calling itself recursively
- searchResults() stops asking for more filters once nothing matches, and returns a new chain even when no filter is applied, so the inventory is never handed out to be changed

What changed in LinkedChain:

We modified the provided 'LinkedChain'. We added a 'tail' reference so 'add()' puts new entries at the end in O(1). With the original 'add()', the newest vehicle showed up first. Now vehicles are listed in the order they were entered, so we removed 'addToEnd()'. We also made 'LinkedChain' implement 'ChainInterface', which extends "Iterable', and added an 'iterator()', so 'RidePortal' can use for-each loops. 'toString()' now prints one entry per line, as described in our spec. The other methods work the same as before.

What happened to our unknowns:

Our scope listed two things we didn't know how to do:
- GUI. Still open. We didn't build a GUI. The program uses a terminal menu, which is what our MVP asked for.
- What other variables Motorcycle should hold. Partly answered. We settled on a fixed list of motorcycle types (Sport, Cruiser, Touring, Standard, Dirt), and the program checks input against it. But 'Motorcycle' still has no other fields of its own, so this question is still open.

What doesn't work yet:

- Updating a vehicle. Our MVP says users can upload, update or remove vehicles, but right now they can only add and remove them.
- Searching with a value that's too big gives wrong results. Search uses the same prompts as adding a vehicle, so big values get capped. For example, searching for year 2050 finds 2027 vehicles. We don't have a test for this yet.
- The program crashes if the input suddenly ends, for example if the user presses Ctrl-D.
- Some odd inputs get through, like "-0" or "1e3" for numbers.
- None of our stretch goals are built yet (insurance options, extra car features, eco ranking, and importing vehicles from a file).
  
All 150 of our current tests pass.

**Next Steps: what you will do next to extend this prototype toward the final**

- make sure our code can block invalid inputs: we are working on tests so when a user inputs a invalid data, it rejects and prompts again for a valid input
- We identified a bug caused by a small miscommunication between teammates: different method names and parameter types. We will integrate these and finalize these will work in our final program.
- Alper will fix bugs such as different method names and parameters, Nikitha will write testcases, and Andrew will do final debugs and integration. 


--End of work by October 14, 2026--
