package ai;

import ai.Global;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import kalaha.*;
import java.util.*;
import java.util.List;

/**
 * This is the main class for your Kalaha AI bot. Currently
 * it only makes a random, valid move each turn.
 * 
 * @author Johan Hagelbäck
 */
public class AIClient implements Runnable
{
    private int player;
    private JTextArea text;
    
    private PrintWriter out;
    private BufferedReader in;
    private Thread thr;
    private Socket socket;
    private boolean running;
    private boolean connected;
    	
    /**
     * Creates a new client.
     */
    public AIClient()
    {
	player = -1;
        connected = false;
        
        //This is some necessary client stuff. You don't need
        //to change anything here.
        initGUI();
	
        try
        {
            addText("Connecting to localhost:" + KalahaMain.port);
            socket = new Socket("localhost", KalahaMain.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            addText("Done");
            connected = true;
        }
        catch (Exception ex)
        {
            addText("Unable to connect to server");
            return;
        }
    }
    
    /**
     * Starts the client thread.
     */
    public void start()
    {
        //Don't change this
        if (connected)
        {
            thr = new Thread(this);
            thr.start();
        }
    }
    
    /**
     * Creates the GUI.
     */
    private void initGUI()
    {
        //Client GUI stuff. You don't need to change this.
        JFrame frame = new JFrame("My AI Client");
        frame.setLocation(Global.getClientXpos(), 445);
        frame.setSize(new Dimension(420,250));
        frame.getContentPane().setLayout(new FlowLayout());
        
        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(400, 210));
        
        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
    }
    
    /**
     * Adds a text string to the GUI textarea.
     * 
     * @param txt The text to add
     */
    public void addText(String txt)
    {
        //Don't change this
        text.append(txt + "\n");
        text.setCaretPosition(text.getDocument().getLength());
    }
    
    /**
     * Thread for server communication. Checks when it is this
     * client's turn to make a move.
     */
    long startT;

    
    public void run()
    {
        String reply;
        running = true;
        
        try
        {
            while (running)
            {
                //Checks which player you are. No need to change this.
                if (player == -1)
                {
                    out.println(Commands.HELLO);
                    reply = in.readLine();

                    String tokens[] = reply.split(" ");
                    player = Integer.parseInt(tokens[1]);
                    
                    addText("I am player " + player);
                }
                
                //Check if game has ended. No need to change this.
                out.println(Commands.WINNER);
                reply = in.readLine();
                if(reply.equals("1") || reply.equals("2") )
                {
                    int w = Integer.parseInt(reply);
                    if (w == player)
                    {
                        addText("I won!");
                    }
                    else
                    {
                        addText("I lost...");
                    }
                    running = false;
                }
                if(reply.equals("0"))
                {
                    addText("Even game!");
                    running = false;
                }

                //Check if it is my turn. If so, do a move
                out.println(Commands.NEXT_PLAYER);
                reply = in.readLine();
                if (!reply.equals(Errors.GAME_NOT_FULL) && running)
                {
                    int nextPlayer = Integer.parseInt(reply);

                    if(nextPlayer == player)
                    {
                        startT = System.currentTimeMillis();
                        out.println(Commands.BOARD);
                        String currentBoardStr = in.readLine();
                        boolean validMove = false;
                        while (!validMove)
                        {
                            startT = System.currentTimeMillis();
                            //This is the call to the function for making a move.
                            //You only need to change the contents in the getMove()
                            //function.
                            GameState currentBoard = new GameState(currentBoardStr);
                            int cMove = getMove(currentBoard);
                            
                            //Timer stuff
                            long tot = System.currentTimeMillis() - startT;
                            double e = (double)tot / (double)1000;
                            
                            out.println(Commands.MOVE + " " + cMove + " " + player);
                            reply = in.readLine();
                            if (!reply.startsWith("ERROR"))
                            {
                                validMove = true;
                                addText("Made move " + cMove + " in " + e + " secs");
                            }
                        }
                    }
                }
                
                //Wait
                Thread.sleep(100);
            }
	}
        catch (Exception ex)
        {
            running = false;
        }
        
        try
        {
            socket.close();
            addText("Disconnected from server");
        }
        catch (Exception ex)
        {
            addText("Error closing connection: " + ex.getMessage());
        }
    }
    
    /**
     * This is the method that makes a move each time it is your turn.
     * Here you need to change the call to the random method to your
     * Minimax search.
     * 
     * @param currentBoard The current board state
     * @return Move to make (1-6)
     */
    public int getMove(GameState currentBoard)
    {
        int myMove = 0;

        myMove = GetIterativeMove(currentBoard.clone());
        
        return myMove;
    }
    
    /**
     * Returns a random ambo number (1-6) used when making
     * a random move.
     * 
     * @return Random ambo number
     */
   

    
    
    public class leaf
    {
        public int score;
        public int move;
        public int delta;
        List<leaf> leafNode;

        public leaf() {
            this.leafNode = new ArrayList<leaf>();
            this.delta = 0;
        }
        

    };
    
    leaf leafOrigin = new leaf();// = new ArrayList<leaf>;
    int endOfTime = 4;

    


    
    public int GetIterativeMove(GameState currentBoard)
    {
        //if(!leafOrigin.leafNode.isEmpty())
        leafOrigin.leafNode.clear();
        int goalDepth = 4;
        int choise = 0;
        while((System.currentTimeMillis() - startT) / (double)1000 < endOfTime)
        {
            addText("Depth: " + goalDepth);

            createTree(currentBoard,0,goalDepth,leafOrigin);
            utility(leafOrigin, currentBoard, 0,goalDepth - 1);

            if((System.currentTimeMillis() - startT) / (double)1000 < endOfTime)
            {
                leafOrigin.delta = -1;
                for(int i = 0; i < leafOrigin.leafNode.size();i++)
                {
                    int temp = leafOrigin.leafNode.get(i).delta;
                    if(temp > leafOrigin.delta)
                    {
                        leafOrigin.delta = temp;
                        choise = leafOrigin.leafNode.get(i).move;
                    }
                }
                if(leafOrigin.delta >= 100)
                {
                    break;
                }
                goalDepth = goalDepth + 2;
            }
            else
            {
                addText("Aborted out of time. ");
            }
        }
        return choise;
    }
    
    
    public void createTree(GameState currentBoard,int depth,int goalDepth,leaf node)
    {
        
        if((System.currentTimeMillis() - startT) / (double)1000 < endOfTime)
        {
            
            node.score = currentBoard.getScore(player);

            if(depth < goalDepth)
            {
                for(int i = 1;i < 7;i++)
                {
                    if(currentBoard.moveIsPossible(i))
                    {
                        GameState tempBoard = currentBoard.clone();
                        tempBoard.makeMove(i);

                        leaf tempNode = new leaf();
                        tempNode.move = i;
                        node.leafNode.add(tempNode);

                        createTree(tempBoard,depth + 1, goalDepth,  node.leafNode.get(node.leafNode.size()-1));

                    }
                }
            }
        }
    }
    
    
    public void utility(leaf _leafOrigin, GameState _currentBoard,int _depth, int _goalDepth)
    {
        if((System.currentTimeMillis() - startT) / (double)1000 < endOfTime)
        {
            

            if(_currentBoard.gameEnded() == false)
            {
                if(_depth < _goalDepth)
                {
                    for(int i = 0; i < _leafOrigin.leafNode.size();i++)
                    {
                        GameState nextBoard = _currentBoard.clone();

                        nextBoard.makeMove(_leafOrigin.leafNode.get(i).move);

                        utility(_leafOrigin.leafNode.get(i),nextBoard,_depth + 1,_goalDepth);
                    }
                }
                _leafOrigin.delta = -1;
                if(_currentBoard.getNextPlayer() == player) // max
                {
                    _leafOrigin.delta = 0;
                    for(int i = 0; i < _leafOrigin.leafNode.size();i++)
                    {
                        int temp = _leafOrigin.leafNode.get(i).score - _leafOrigin.score;
                        if(temp > _leafOrigin.delta)
                        {
                            _leafOrigin.delta = temp + _leafOrigin.leafNode.get(i).delta;;
                        }
                    }
                }
                else                    // min
                {
                    _leafOrigin.delta = 100;
                    for(int i = 0; i < _leafOrigin.leafNode.size();i++)
                    {
                        int temp = _leafOrigin.leafNode.get(i).score - _leafOrigin.score;
                        if(temp < _leafOrigin.delta)
                        {
                            _leafOrigin.delta = temp + _leafOrigin.leafNode.get(i).delta;
                        }
                    }
                }
            }
            else
            {
                if(_currentBoard.getWinner() == player)
                    _leafOrigin.delta = 100;
                else
                    _leafOrigin.delta = 0;

            }
        }
    }
}