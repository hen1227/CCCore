package com.henhen1227.cccore.chatGames;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.commands.PointsCommand;
import com.henhen1227.cccore.networking.NetworkManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WordUnscramble extends ChatGame {
    private List<String> words = new ArrayList<>();
    private String currentWord;
    private FileConfiguration wordConfig;

    public WordUnscramble() {
        super("WordUnscramble");
        loadWords();
        victoryPoints = 10;
    }

    private void loadWords() {
        File wordFile = new File(CCCore.instance.getDataFolder(), "randomwords.txt");
        if (!wordFile.exists()) {
            wordFile.getParentFile().mkdirs();
            CCCore.instance.saveResource("randomwords.txt", false);
        }

        try (Scanner scanner = new Scanner(wordFile)) {
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String scrambleWord(String word) {
        List<Character> letters = new ArrayList<>();
        for (char letter : word.toCharArray()) {
            letters.add(letter);
        }
        Collections.shuffle(letters);
        StringBuilder scrambled = new StringBuilder(word.length());
        for (char letter : letters) {
            scrambled.append(letter);
        }
        return scrambled.toString();
    }

    @Override
    public void handleResponse(Player player, String message) {
        if (message.equalsIgnoreCase(currentWord)) {  // word is the current scrambled word
            // Player guessed the word correctly, reward them and end the game
            endGame(player);
        }
    }

    @Override
    public void startGame() {
        active = true;

        if (words.isEmpty()) {
            Bukkit.getLogger().info("No words available for WordUnscramble game.");
            return;
        }
        Random rand = new Random();
        currentWord = words.get(rand.nextInt(words.size()));
        String scrambled = scrambleWord(currentWord);

        // Send the scrambled word to chat and start listening for player responses
        // This logic will depend on your specific server setup
        Bukkit.getLogger().info("Scrambled word: " + scrambled);
        Bukkit.getLogger().info("Actual word " + currentWord);

        TextComponent message =  Component.text("[CCCore] ").color(NamedTextColor.GREEN)
                .append(Component.text("First person to unscramble the word: ").color(NamedTextColor.WHITE))
                .append(Component.text(scrambled.toUpperCase()).color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" wins!").color(NamedTextColor.WHITE));

        ChatManager.message(message);

        // Start a timer to end the game after 270 seconds
        Bukkit.getScheduler().runTaskLater(CCCore.instance, () -> {
            if(active) {
                ChatManager.message("Time's up!");
                endGame(null);
            }
        }, 20 * 270);
    }

    public void endGame(Player winner) {
        if(winner == null) {
            ChatManager.message("No one guessed \"" + currentWord + "\" in time!");
            ChatGameManager.endGame();
            return;
        }

        active = false;

        // Reward the player and clear the active game
        PointsCommand.awardPoints(5, winner.getName());
        NetworkManager.recordWin(winner, getUniqueId());
        ChatGameManager.endGame();
    }
}