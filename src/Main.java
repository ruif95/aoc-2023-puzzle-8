import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int [] numberOfInstancesPerCardId;
    public static final String WRONG_INPUT_FILE_MESSAGE = "Wrong file";
    public static final String INPUT_FILENAME = "input";

    public static void main(String[] args) throws IOException {
        List<String> inputLines = extractInputLines();
        numberOfInstancesPerCardId = new int[inputLines.size()];
        inputLines.forEach(Main::toCard);

         int result = IntStream.of(numberOfInstancesPerCardId).sum();

        System.out.println("A-ha! The number of points the cards are worth is: " + result);
    }

    private static void toCard(String inputLine) {
        int cardNumber = Integer.parseInt(inputLine.substring(inputLine.indexOf(' '), inputLine.indexOf(':')).trim());
        String inputLineBody = inputLine.substring(inputLine.indexOf(':') + 1).trim();
        String[] winningNumbersAndHand = inputLineBody.split(" \\| ");

        new Card(cardNumber,
                Arrays.stream(winningNumbersAndHand[0].trim().split("\\s+"))
                      .map(Integer::parseInt)
                      .collect(Collectors.toSet()),
                Arrays.stream(winningNumbersAndHand[1].trim().split("\\s+"))
                      .map(Integer::parseInt)
                      .toList());
    }

    private static List<String> extractInputLines() throws IOException {
        try (InputStream resource = Main.class.getResourceAsStream(INPUT_FILENAME)) {
            if (resource == null) {
                throw new RuntimeException(WRONG_INPUT_FILE_MESSAGE);
            }

            return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))
                    .lines()
                    .toList();
        }
    }

    static class Card {
        int cardNumber;
        Set<Integer> winningNumbers;
        List<Integer> numbersYouHave;

        public Card(int cardNumber, Set<Integer> winningNumbers, List<Integer> numbersYouHave) {
            this.cardNumber = cardNumber;
            this.winningNumbers = winningNumbers;
            this.numbersYouHave = numbersYouHave;

            calculatePoints();
        }

        private void calculatePoints() {
            int cardId = cardNumber;
            numberOfInstancesPerCardId[cardId - 1]++;

            for (Integer number : numbersYouHave) {
                if (winningNumbers.contains(number)) {
                    if (cardId < numberOfInstancesPerCardId.length) {
                        numberOfInstancesPerCardId[cardId] += numberOfInstancesPerCardId[cardNumber - 1];
                        cardId++;
                    }
                }
            }
        }
    }
}
