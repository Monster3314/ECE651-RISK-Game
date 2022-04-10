package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Unit;

public class GameIO {
  private ObjectInputStream input;
  private ObjectOutputStream output;
  private Socket server;

  public GameIO(ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
    //this.input = new ObjectInputStream(server.getInputStream());
    //this.output = new ObjectOutputStream(server.getOutputStream());
    input = ois;
    output = oos;
  }

  public GameIO(Socket server) throws IOException {
    this.server = server;
    this.input = new ObjectInputStream(server.getInputStream());
    this.output = new ObjectOutputStream(server.getOutputStream());
  }

  public void setSocket(Socket server) {
    this.server = server;
  }

  /**
   * send placements to server
   * @param placements that map a territory name to a list of units placed
   * @throws IOException when failed to send  
   */
  public void sendPlacements(Map<String, List<Unit>> placements) throws IOException{
    output.writeObject(placements);
    output.flush();
    output.reset();
  }
  
  /**
   * sendActions will send the list of actions to the server 
   * @param toSend is a list of valid actions to send to the server
   * @throws IOException when failed to send to the server  
   */  
  public void sendActions(List<Action> toSend) throws IOException {
    output.writeObject(toSend);
    output.flush();
    output.reset();
  }
  /**
   * recieve color string from server
   * @throws ClassNotFoundException when casting failed
   * @throws IOException when nothing is fetched from server
   * @return the string that represents color  
   */
  public String recvColor() throws ClassNotFoundException, IOException {
    return (String) input.readObject();
  }
  
  /**
   * recieve game model from server
   * @throws ClassNotFoundException when casting failed
   * @throws IOException when nothing is fetched from server
   * @return the game  
   */
  public GameInfo recvGame() throws ClassNotFoundException, IOException {
    return (GameInfo) input.readObject();
  }
  /**
   * recvUnitsToPlace get the number of basic units to place
   * @throws ClassNotFoundException when failed to cast
   * @throws IOException when nothing fetched  
   */  
  @SuppressWarnings("unchecked")
  public List<Unit> recvUnitsToPlace() throws ClassNotFoundException, IOException{
     return (List<Unit>) input.readObject();
  }

  /**
   * close objectInputStream and objectOutputStream  
   */
  public void close() throws IOException{
    server.close();
    input.close();
    output.close();
  }
  

}
