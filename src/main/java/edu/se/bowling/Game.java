package edu.se.bowling;

public class Game {

    private static final int NUMBER_OF_FRAMES = 10;
    private static final int MAX_NUMBER_OF_ROLLS = 21;
    private static final int NUMBER_OF_PINS = 10;

    private final int[] rolls = new int[MAX_NUMBER_OF_ROLLS];
    private int currentRoll = 0;

    public void roll(int pins) {
        validateGameNotFinished();
        validatePinsRange(pins);
        validateRollAccordingToGameState(pins);
        rolls[currentRoll++] = pins;
    }

    public int score() {
        int score = 0;
        int rollIndex = 0;

        for (int frame = 0; frame < NUMBER_OF_FRAMES; frame++) {
            if (isStrike(rollIndex)) {
                score += NUMBER_OF_PINS + strikeBonus(rollIndex);
                rollIndex++;
            } else if (isSpare(rollIndex)) {
                score += NUMBER_OF_PINS + spareBonus(rollIndex);
                rollIndex += 2;
            } else {
                score += sumOfBallsInFrame(rollIndex);
                rollIndex += 2;
            }
        }

        return score;
    }

    private void validateGameNotFinished() {
        if (isGameFinished()) {
            throw new IllegalStateException("Cannot roll after game is finished.");
        }
    }

    private void validatePinsRange(int pins) {
        if (pins < 0 || pins > NUMBER_OF_PINS) {
            throw new IllegalArgumentException("Pins must be between 0 and 10.");
        }
    }

    private void validateRollAccordingToGameState(int pins) {
        int[] state = getCurrentFrameAndBall();
        int frame = state[0];
        int ball = state[1];
        int firstRollPins = state[2];

        if (frame < 9) {
            validateRegularFrame(pins, ball, firstRollPins);
        } else {
            validateTenthFrame(pins);
        }
    }

    private void validateRegularFrame(int pins, int ball, int firstRollPins) {
        if (ball == 2 && firstRollPins + pins > NUMBER_OF_PINS) {
            throw new IllegalArgumentException("Pin count in a frame cannot exceed 10.");
        }
    }

    private void validateTenthFrame(int pins) {
        int tenthStart = getTenthFrameStartIndex();
        int rollsInTenth = currentRoll - tenthStart;

        if (rollsInTenth == 0) {
            return;
        }

        int first = rolls[tenthStart];

        if (rollsInTenth == 1) {
            if (first != NUMBER_OF_PINS && first + pins > NUMBER_OF_PINS) {
                throw new IllegalArgumentException(
                    "Pin count in tenth frame cannot exceed 10 unless first roll is a strike."
                );
            }
            return;
        }

        int second = rolls[tenthStart + 1];

        if (rollsInTenth == 2) {
            boolean strike = first == NUMBER_OF_PINS;
            boolean spare = first != NUMBER_OF_PINS && first + second == NUMBER_OF_PINS;

            if (!strike && !spare) {
                throw new IllegalStateException("No bonus roll allowed in tenth frame.");
            }

            if (strike && second != NUMBER_OF_PINS && second + pins > NUMBER_OF_PINS) {
                throw new IllegalArgumentException("Invalid bonus rolls after strike in tenth frame.");
            }
            return;
        }

        throw new IllegalStateException("Cannot roll more than three times in tenth frame.");
    }

    private boolean isGameFinished() {
        int frame = 0;
        int rollIndex = 0;

        while (frame < 9 && rollIndex < currentRoll) {
            if (rolls[rollIndex] == NUMBER_OF_PINS) {
                rollIndex++;
            } else {
                if (rollIndex + 1 >= currentRoll) {
                    return false;
                }
                rollIndex += 2;
            }
            frame++;
        }

        if (frame < 9) {
            return false;
        }

        int rollsInTenth = currentRoll - rollIndex;
        if (rollsInTenth < 2) {
            return false;
        }

        int first = rolls[rollIndex];
        int second = rolls[rollIndex + 1];

        if (first == NUMBER_OF_PINS || first + second == NUMBER_OF_PINS) {
            return rollsInTenth >= 3;
        }

        return true;
    }

    private int[] getCurrentFrameAndBall() {
        int frame = 0;
        int rollIndex = 0;

        while (frame < 9 && rollIndex < currentRoll) {
            if (rolls[rollIndex] == NUMBER_OF_PINS) {
                rollIndex++;
            } else {
                if (rollIndex + 1 >= currentRoll) {
                    return new int[] {frame, 2, rolls[rollIndex]};
                }
                rollIndex += 2;
            }
            frame++;
        }

        if (frame < 9) {
            return new int[] {frame, 1, 0};
        }

        int rollsInTenth = currentRoll - rollIndex;
        int firstRollPins = rollsInTenth >= 1 ? rolls[rollIndex] : 0;

        return new int[] {9, rollsInTenth + 1, firstRollPins};
    }

    private int getTenthFrameStartIndex() {
        int frame = 0;
        int rollIndex = 0;

        while (frame < 9) {
            if (rolls[rollIndex] == NUMBER_OF_PINS) {
                rollIndex++;
            } else {
                rollIndex += 2;
            }
            frame++;
        }

        return rollIndex;
    }

    private int strikeBonus(int rollIndex) {
        return rolls[rollIndex + 1] + rolls[rollIndex + 2];
    }

    private int spareBonus(int rollIndex) {
        return rolls[rollIndex + 2];
    }

    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == NUMBER_OF_PINS;
    }

    private boolean isSpare(int rollIndex) {
        return sumOfBallsInFrame(rollIndex) == NUMBER_OF_PINS;
    }

    private int sumOfBallsInFrame(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1];
    }
}