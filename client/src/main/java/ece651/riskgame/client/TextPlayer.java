package ece651.riskgame.client;

import ece651.riskgame.shared.GameInfo;

public class TextPlayer {
  final String color;
  final GameInfo theGame;
  final GameView view;
  public TextPlayer(GameInfo g, String color) {
    this.color = color;
    this.theGame = g;
    this.view = new GameTextView(theGame);
  }
  public String display() {
    return view.displayGame();
  }
}
