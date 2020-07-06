# mcss-assignment2-daisyworld
The Daisyworld model is based on the “Gaia Hypothesis”, which states that the Earth is a single and self-regulating system. In particular, this model will explore how the temperature could affect living organisms: white and black daisies.

## Running the program

1. Clean the project
`del /f *.class`

2. Compile Java Project
`javac main.java`

2. Run the Java Model
`java main 2 "C:/JESS_DRIVE/Learn/SWEN90004 - 2020 S1 - Modelling Complex Software Systems/mcss-assignment2-daisyworld/ExportSimulationResult.csv"`

- The first parameter when running the program is the scenario number.
- The second parameter is the filepath where the simulation results should be saved.


## General way of running experiments

1. Update the parameters in `Pramas.java`
2. follow the steps in `Running the program` section of this readme file.


## Examples of how to run experiments

### Scenario: Ramp up ramp down
1. In `Params.java`, changed the `RAMP_UP_RAMP_DOWN` parameter to the initial luminosity in the environment
2. Compile the code: `javac main.java`
3. Run the code using `0` as the scenario: `java main 0 "filepath/filename.csv"`

### Scenario: Low solar luminosity
1. In `Params.java`, changed the `LOW_SOLAR_LUMINOSITY` parameter to the initial luminosity in the environment
2. Compile the code: `javac main.java`
3. Run the code using `1` as the scenario: `java main 1 "filepath/filename.csv"`

### Scenario: Our solar luminosity
1. In `Params.java`, changed the `OUR_SOLAR_LUMINOSITY` parameter to the initial luminosity in the environment
2. Compile the code: `javac main.java`
3. Run the code using `2` as the scenario: `java main 2 "filepath/filename.csv"`

### Scenario: High solar luminosity
1. In `Params.java`, changed the `HIGH_SOLAR_LUMINOSITY` parameter to the initial luminosity in the environment
2. Compile the code: `javac main.java`
3. Run the code using `3` as the scenario: `java main 3 "filepath/filename.csv"`
