# Bowling Game Kata

This repository contains my solution for **Challenge #2** of the Software Engineering course.

The project is based on the Bowling Game Kata and was developed using **TDD**, **JUnit**, **Maven**, and **Git**.

## Changes made

Starting from the base project provided by the professor, I extended the solution by:

- adding custom unit tests,
- improving the `Game` class implementation,
- validating invalid rolls,
- preventing impossible frame scores,
- controlling the rules of the tenth frame,
- preventing rolls after the game is finished,
- making the solution more fault-tolerant and reliable.

## Tests included

The test suite covers:

- gutter game,
- all ones,
- spare scoring,
- strike scoring,
- perfect game,
- negative rolls,
- rolls greater than 10,
- invalid frame totals,
- invalid extra rolls,
- tenth frame validation.

## Run the project

To run the tests:

```bash
mvn clean test