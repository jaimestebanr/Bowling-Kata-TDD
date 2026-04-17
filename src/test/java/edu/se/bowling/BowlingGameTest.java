package edu.se.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BowlingGameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void shouldScoreZeroForGutterGame() {
        rollMany(20, 0);
        assertEquals(0, game.score());
    }

    @Test
    void shouldScoreTwentyForAllOnes() {
        rollMany(20, 1);
        assertEquals(20, game.score());
    }

    @Test
    void shouldScoreSpareWithNextRollBonus() {
        rollSpare();
        game.roll(3);
        rollMany(17, 0);
        assertEquals(16, game.score());
    }

    @Test
    void shouldScoreStrikeWithNextTwoRollsBonus() {
        rollStrike();
        game.roll(3);
        game.roll(4);
        rollMany(16, 0);
        assertEquals(24, game.score());
    }

    @Test
    void shouldScorePerfectGame() {
        rollMany(12, 10);
        assertEquals(300, game.score());
    }

    @Test
    void shouldRejectNegativePins() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(-1));
    }

    @Test
    void shouldRejectMoreThanTenPins() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }

    @Test
    void shouldRejectFramePinCountGreaterThanTen() {
        game.roll(8);
        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }

    @Test
    void shouldRejectRollingAfterGameIsOverForOpenTenthFrame() {
        rollMany(18, 0);
        game.roll(3);
        game.roll(6);

        assertThrows(IllegalStateException.class, () -> game.roll(1));
    }

    @Test
    void shouldRejectRollingAfterPerfectGame() {
        rollMany(12, 10);

        assertThrows(IllegalStateException.class, () -> game.roll(10));
    }

    @Test
    void shouldAllowBonusRollAfterTenthFrameSpare() {
        rollMany(18, 0);
        game.roll(5);
        game.roll(5);
        game.roll(7);

        assertEquals(17, game.score());
    }

    @Test
    void shouldAllowTwoBonusRollsAfterTenthFrameStrike() {
        rollMany(18, 0);
        game.roll(10);
        game.roll(7);
        game.roll(2);

        assertEquals(19, game.score());
    }

    @Test
    void shouldRejectThirdRollInTenthFrameWithoutSpareOrStrike() {
        rollMany(18, 0);
        game.roll(3);
        game.roll(4);

        assertThrows(IllegalStateException.class, () -> game.roll(1));
    }

    @Test
    void shouldRejectInvalidSecondRollInTenthFrameWhenNoStrike() {
        rollMany(18, 0);
        game.roll(8);

        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }

    @Test
    void shouldRejectInvalidBonusCombinationAfterStrikeInTenthFrame() {
        rollMany(18, 0);
        game.roll(10);
        game.roll(7);

        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }

    private void rollMany(int n, int pins) {
        for (int i = 0; i < n; i++) {
            game.roll(pins);
        }
    }

    private void rollSpare() {
        game.roll(5);
        game.roll(5);
    }

    private void rollStrike() {
        game.roll(10);
    }
}