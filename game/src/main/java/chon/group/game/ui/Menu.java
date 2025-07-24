package chon.group.game.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {
    private List<String> options;
    private int selectedOptionIndex;

    public Menu() {
        this.options = new ArrayList<>(Arrays.asList("New Game", "Continue", "Exit"));
        this.selectedOptionIndex = 0;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public void selectNextOption() {
        selectedOptionIndex = (selectedOptionIndex + 1) % options.size();
    }

    public void selectPreviousOption() {
        selectedOptionIndex = (selectedOptionIndex - 1 + options.size()) % options.size();
    }

    public String getSelectedOption() {
        return options.get(selectedOptionIndex);
    }
}