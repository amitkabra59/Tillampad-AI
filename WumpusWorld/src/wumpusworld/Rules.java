/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wumpusworld;
import java.util.*;

/**
 *
 * @author Kraiper
 */
public class Rules 
{
    private World w;
    public List<worldInfo> worldList = new ArrayList<worldInfo>();
    
    private int goal[] = new int[2];
    
    public class worldInfo
    {
        public int x;
        public int y;
        
        // Wumpus pit stench breese
        public boolean contains[] = new boolean[4];
        // Wumpus pit
        public boolean used[] = new boolean[2];
        public boolean trueDanger[] = new boolean[2];
        public boolean safe = false;
    }
    
    public Rules(World world)
    {
        w = world;
        for(int i = 1;i < w.getSize()+1;i++)
        {
            for(int j = 1;j < w.getSize()+1;j++)
            {
                worldInfo temp = new worldInfo();
                temp.x = j;
                temp.y = i;
                
                worldList.add(temp);
            }
        }
    }
    
    public void updateWorld()
    {
        int px,py;
        int plp = 0;
        px = w.getPlayerX();
        py = w.getPlayerY();
        goal[0] = -1;
        goal[1] = -1;
        for(int i = 0;i < worldList.size();i++)
        {
            if(worldList.get(i).x == px && worldList.get(i).y == py)
            {
                updatePos(i);
                plp = i;
                break;
            }
        }
        if(worldList.get(plp).contains[2]) // stench
        {
            setDangerZone(px,py,0);
        }
        if(worldList.get(plp).contains[3]) // brese
        {
            setDangerZone(px,py,1);
        }
        setTrueDanger();
        clearFalseDanger();
        for(int i = 0; i < worldList.size();i++)
        {
            if( worldList.get(i).contains[2] == false && 
                worldList.get(i).contains[3] == false &&
                w.isVisited(worldList.get(i).x, worldList.get(i).y)) // empty
            {
                setSafeZone(worldList.get(i).x, worldList.get(i).y);
            }
        }
        setGoal(false);
        for(int i = 0; i < worldList.size();i++)
        {
            if(w.hasGlitter(worldList.get(i).x, worldList.get(i).y))
            {
                goal[0] = worldList.get(i).x;
                goal[1] = worldList.get(i).y;
            }
        }
        pathToGoal();
    }
    
    private void updatePos(int i)
    {
        worldList.get(i).safe = true;
        if(w.hasBreeze(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).contains[3] = true;
        }
        if(w.hasStench(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).contains[2] = true;
        }
        if(w.hasPit(worldList.get(i).x, worldList.get(i).y))
        {
            setPit(worldList.get(i).x, worldList.get(i).y);
        }
        if(w.hasWumpus(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).safe = false;
            worldList.get(i).contains[0] = true;
        }
    }
    
    private void setDangerZone(int x,int y,int danger)
    {
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;
                    }
            }
            if(worldList.get(i).x == x-1 && worldList.get(i).y == y )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;

                    }
            }
            if(worldList.get(i).x == x && worldList.get(i).y == y+1 )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;

                    }
            }
            if(worldList.get(i).x == x && worldList.get(i).y == y-1 )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;

                    }
            }
        }
    }
    
    private void setSafeZone(int x,int y)
    {
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                worldList.get(i).safe = true;
            }
            if(worldList.get(i).x == x-1 && worldList.get(i).y == y )
            {
                worldList.get(i).safe = true;
            }
            if(worldList.get(i).x == x && worldList.get(i).y == y+1 )
            {
                worldList.get(i).safe = true;
            }
            if(worldList.get(i).x == x && worldList.get(i).y == y-1 )
            {
                worldList.get(i).safe = true;
            }
        }
    }
    
    private void setTrueDanger()
    {
        for(int j = 0; j < 2;j++)
        {
            int x,y;
            for(int i = 0; i < worldList.size();i++)
            {
                if(worldList.get(i).contains[j] == true)
                {
                    x = worldList.get(i).x;
                    y = worldList.get(i).y;
                  
                    if(w.isVisited(x+1, y+1))
                    {
                        int markers = 0;
                        for(int k = 0; k < worldList.size();k++)
                        {
                            if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                            if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                        }
                        if(markers == 2)
                        {
                            for(int k = 0; k < worldList.size();k++)
                            {
                                if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                            }
                            worldList.get(i).trueDanger[j] = true;
                            break;
                        }
                    }
                     if(w.isVisited(x-1, y+1))
                    {
                        int markers = 0;
                        for(int k = 0; k < worldList.size();k++)
                        {
                            if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                            if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                        }
                        if(markers == 2)
                        {
                            for(int k = 0; k < worldList.size();k++)
                            {
                                if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                            }
                            worldList.get(i).trueDanger[j] = true;
                            break;
                        }
                    }   
                    if(w.isVisited(x-1, y-1))
                    {
                        int markers = 0;
                        for(int k = 0; k < worldList.size();k++)
                        {
                            if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                            if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                        }
                        if(markers == 2)
                        {
                            for(int k = 0; k < worldList.size();k++)
                            {
                                if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                            }
                            worldList.get(i).trueDanger[j] = true;
                            break;
                        }
                    }
                    if(w.isVisited(x+1, y-1))
                    {
                        int markers = 0;
                        for(int k = 0; k < worldList.size();k++)
                        {
                            if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                            if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                            {
                                if(worldList.get(k).contains[j+2] == true && worldList.get(k).used[j] == false)
                                {
                                    markers++;
                                }
                            }
                        }
                        if(markers == 2)
                        {
                            for(int k = 0; k < worldList.size();k++)
                            {
                                if(worldList.get(k).x == x+1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y+1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x-1 && worldList.get(k).y == y)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                                if(worldList.get(k).x == x && worldList.get(k).y == y-1)
                                {
                                    worldList.get(k).used[j] = true;
                                }
                            }
                            worldList.get(i).trueDanger[j] = true;
                            break;
                        }
                    }
                }
            }   
        }
    }
    
    private void clearFalseDanger()
    {   
        for(int j = 0; j < 2;j++)
        {
            for(int i = 0; i < worldList.size();i++)
            {
                if(worldList.get(i).contains[j] == true && worldList.get(i).trueDanger[j] == false)
                {
                    int x = worldList.get(i).x;
                    int y = worldList.get(i).y;

                    for(int k = 0; k < worldList.size();k++)
                    {
                        if(worldList.get(k).x == x+1 && worldList.get(k).y == y+1)
                        {
                            if(worldList.get(k).trueDanger[j] == true)
                            {
                                worldList.get(i).contains[j] = false;
                                if(worldList.get(i).contains[0] == false)
                                    worldList.get(i).safe = true;
                            }
                        }
                        if(worldList.get(k).x == x+1 && worldList.get(k).y == y-1)
                        {
                            if(worldList.get(k).trueDanger[j] == true)
                            {
                                worldList.get(i).contains[j] = false;
                                if(worldList.get(i).contains[0] == false)
                                    worldList.get(i).safe = true;
                            }
                        }
                        if(worldList.get(k).x == x-1 && worldList.get(k).y == y+1)
                        {
                            if(worldList.get(k).trueDanger[j] == true)
                            {
                                worldList.get(i).contains[j] = false;
                                if(worldList.get(i).contains[0] == false)
                                    worldList.get(i).safe = true;
                            }
                        }
                        if(worldList.get(k).x == x-1 && worldList.get(k).y == y-1)
                        {
                            if(worldList.get(k).trueDanger[j] == true)
                            {
                                worldList.get(i).contains[j] = false;
                                if(worldList.get(i).contains[0] == false)
                                    worldList.get(i).safe = true;
                            }
                        }
                        
                    }
                    
                    if(w.isVisited(x, y) == true && w.hasPit(x, y) == false)
                    {
                        worldList.get(i).contains[1] = false;
                    }
                    if(w.isVisited(x, y) == true && w.hasWumpus(x, y) == false)
                    {
                        worldList.get(i).contains[0] = false;
                    }
                }
            }
        }
    }
    
    private void setGoal(boolean arbitary)
    {
        List<worldInfo> safeList = new ArrayList<worldInfo>();
        boolean trueWumpus = false;
        for(int i = 0; i < worldList.size();i++)
        {
            if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false && worldList.get(i).safe == true )
            {
                safeList.add(worldList.get(i));
            }
        }
        
        if(safeList.size() > 0)
        {
            int safe[] = new int[2];
            safe[0] = 100;
            safe[1] = 0;
            for(int i  = 0; i < safeList.size();i++)
            {
                if(Math.abs(safeList.get(i).x - w.getPlayerX()) + Math.abs(safeList.get(i).y - w.getPlayerY()) < safe[0])
                {
                    safe[0] = Math.abs(safeList.get(i).x - w.getPlayerX()) + Math.abs(safeList.get(i).y - w.getPlayerY());
                    safe[1] = i;
                }
            }
            goal[0] = safeList.get(safe[1]).x;
            goal[1] = safeList.get(safe[1]).y;
        }
        else if(w.wumpusAlive() == true)
        {
            for(int i = 0; i < worldList.size();i++)
            {
                if(worldList.get(i).trueDanger[0] == true)
                {
                    goal[0] = -1;
                    goal[1] = -1;
                    trueWumpus = true;
                }
            }
        }
        
        int delta[] = new int[2];
        delta[0] = 100;
        delta[1] = -1;
        
        if(safeList.size() == 0 && trueWumpus == false || arbitary == true)
        {
            for(int i = 0; i < worldList.size();i++)
            {
                if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false && worldList.get(i).trueDanger[0] == false && worldList.get(i).trueDanger[1] == false && worldList.get(i).contains[0] == false)
                {
                    int testD = Math.abs(worldList.get(i).x - w.getPlayerX()) + Math.abs(worldList.get(i).y - w.getPlayerY());
                    if( testD < delta[0])
                    {
                        if(possibleMove(worldList.get(i).x,worldList.get(i).y) == true)
                        {
                            delta[0] = (worldList.get(i).x - w.getPlayerX()) + Math.abs(worldList.get(i).y - w.getPlayerY());
                            delta[1] = i;
                        }
                    }
                }   
            }
            
            if(delta[1] == -1)
            {
                for(int i = 0; i < worldList.size();i++)
                {
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        int testD = Math.abs(worldList.get(i).x - w.getPlayerX()) + Math.abs(worldList.get(i).y - w.getPlayerY());
                        if( testD < delta[0])
                        {
                            delta[0] = (worldList.get(i).x - w.getPlayerX()) + Math.abs(worldList.get(i).y - w.getPlayerY());
                            delta[1] = i;
                        }
                    } 
                }
            }
            goal[0] = worldList.get(delta[1]).x;
            goal[1] = worldList.get(delta[1]).y;
        }

        System.out.println("Goal = " + goal[0] + " " + goal[1]);
    }
    
    private void pathToGoal()
    {
        int px = w.getPlayerX();
        int py = w.getPlayerY();
        
        int dx = goal[0] - px;
        int dy = goal[1] - py;
        
        int pi = 0;
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == px && worldList.get(i).y == py)
            {
                pi = i;
            }
        }
        
        if(Math.abs(dx) + Math.abs(dy) == 1)
        {
            if(shouldTurn(goal[0],goal[1]))
            {
                doTurn(goal[0],goal[1]);
            }
            else
            {
                w.doAction(World.A_MOVE);
            }
        }
        else if(goal[0] == -1 && goal[1] == -1)
        {
            killWumpus();
        }
        else
        {
            List<worldInfo> pathList = new ArrayList<worldInfo>();
            List<worldInfo> visitList = new ArrayList<worldInfo>();
            pathList.add(worldList.get(pi));

            
            while(true)
            {
                int tx = pathList.get(pathList.size()-1).x;
                int ty = pathList.get(pathList.size()-1).y;
                int bestChoise[] = new int[2];
                bestChoise[0] = 100;
                bestChoise[1] = 100;   
                dx = goal[0] - tx;
                dy = goal[1] - ty;
                
                if(Math.abs(dx) + Math.abs(dy) == 1)
                {
                    for(int i = 0; i < worldList.size();i++)
                    {
                        if(worldList.get(i).x == goal[0] && worldList.get(i).y == goal[1])
                        {
                            pathList.add(worldList.get(i));
                        }
                    }
                }
                else
                {
                    for(int i = 0; i < worldList.size();i++)
                    {
                        if(Math.abs(worldList.get(i).x - tx) + Math.abs(worldList.get(i).y - ty) == 1)
                        {
                            if( worldList.get(i).safe == true)
                            {
                                if(Math.abs(worldList.get(i).x - goal[0]) + Math.abs(worldList.get(i).y - goal[1]) < bestChoise[0] )
                                {
                                    boolean beenThere = false;
                                    for(int j = 0; j < pathList.size();j++)
                                    {
                                        if(pathList.get(j) == worldList.get(i))
                                        {
                                            beenThere = true;
                                        } 
                                    }
                                    if(beenThere == false)
                                    {
                                        bestChoise[0] = worldList.get(i).x - goal[0] + worldList.get(i).y - goal[1];
                                        bestChoise[1] = i;
                                    }
                                }
                            }
                        }
                    }

                    if(bestChoise[1] == 100)
                    {
                        pathList.remove(pathList.get(pathList.size()-1));
                    }
                    else
                    {
                        pathList.add(worldList.get(bestChoise[1]));
                    }
                }
                
                if(pathList.get(pathList.size()-1).x == goal[0] && pathList.get(pathList.size()-1).y == goal[1])
                {
                    break;
                }
                
                if(pathList.size() > 1000)
                {
                    boolean trueWumpus = false;
                    for(int i = 0; i < worldList.size();i++)
                    {
                        if(worldList.get(i).trueDanger[1] == true)
                        {
                            trueWumpus = true;
                        }
                    }
                    if(trueWumpus == true && w.hasArrow() == true)
                    {
                        killWumpus();
                    }
                    else
                    {
                        break;
                    }
                }
            }
            
            for(int i = 0; i < pathList.size();i++)
            {
                if(shouldTurn(pathList.get(i).x,pathList.get(i).y))
                {
                    doTurn(pathList.get(i).x,pathList.get(i).y);
                }
                else
                {
                    w.doAction(World.A_MOVE);
                }
            }
        }
    }
    
    private boolean shouldTurn(int x, int y)
    {
        int px = w.getPlayerX();
        int py = w.getPlayerY();
        
        int dx = x - px;
        int dy = y - py;
        
        if(dx == 1 && w.getDirection() == World.DIR_RIGHT)
        {
            return false;
        }
        else if(dx == -1 && w.getDirection() == World.DIR_LEFT)
        {
            return false;
        }
        else if(dy == 1 && w.getDirection() == World.DIR_UP)
        {
            return false;
        }
        else if(dy == -1 && w.getDirection() == World.DIR_DOWN)
        {
            return false;
        }
        
        return true;
    }
    
    private void doTurn(int x, int y)
    {
        int px = w.getPlayerX();
        int py = w.getPlayerY();
        
        int dx = x - px;
        int dy = y - py;
        
        int pDir = w.getDirection();
        
        
        
        if(dx > 0)
        {
            if(pDir == 0)
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 2)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 3)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
        }
        else if(dx < 0)
        {
            if(pDir == 0)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 2)
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 1)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
        }
        else if(dy > 0)
        {
            if(pDir == 1)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 2)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 3)
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
        }
        if(dy < 0)
        {
            if(pDir == 0)
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 1)
            {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            }
            else if(pDir == 3)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
        }
    }
    
    private void killWumpus()
    {
        int px = w.getPlayerX();
        int py = w.getPlayerY();
        
        int dx = goal[0] - px;
        int dy = goal[1] - py;
        
        int pi = 0;
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == px && worldList.get(i).y == py)
            {
                pi = i;
            }
        }
        
        for(int i = 0; i < worldList.size();i++)
            {
                if(worldList.get(i).trueDanger[0] == true)
                {
                    goal[0] = worldList.get(i).x;
                    goal[1] = worldList.get(i).y;
                }
            }
        
        
        List<worldInfo> pathList = new ArrayList<worldInfo>();
        pathList.add(worldList.get(pi));

        while(pathList.get(pathList.size()-1).x != goal[0] && pathList.get(pathList.size()-1).y != goal[1])
        {
            int tx = pathList.get(pathList.size()-1).x;
            int ty = pathList.get(pathList.size()-1).y;
            int bestChoise[] = new int[2];
            bestChoise[0] = 100;
            bestChoise[1] = 100;   
            if(Math.abs(dx) + Math.abs(dy) == 1)
            {
                for(int i = 0; i < worldList.size();i++)
                {
                    if(worldList.get(i).x == goal[0] && worldList.get(i).y == goal[1])
                    {
                        pathList.add(worldList.get(i));
                    }
                }
            }
            else
            {
                for(int i = 0; i < worldList.size();i++)
                {
                    if(Math.abs(worldList.get(i).x - tx) + Math.abs(worldList.get(i).y - ty) == 1)
                    {
                        if(Math.abs(worldList.get(i).x - goal[0]) + Math.abs(worldList.get(i).y - goal[1]) < bestChoise[0] )
                        {
                            boolean beenThere = false;
                            for(int j = 0; j < pathList.size();j++)
                            {
                                if(pathList.get(j) == worldList.get(i))
                                {
                                    beenThere = true;
                                } 
                            }
                            if(beenThere == false)
                            {
                                bestChoise[0] = worldList.get(i).x - goal[0] + worldList.get(i).y - goal[1];
                                bestChoise[1] = i;
                            }
                        }
                    }
                }

                if(bestChoise[1] == 100)
                {
                    pathList.add(pathList.get(pathList.size()-2));
                }
                else
                {
                    pathList.add(worldList.get(bestChoise[1]));
                }
            }
        }

        for(int i = 0; i < pathList.size();i++)
        {
            if(shouldTurn(pathList.get(i).x,pathList.get(i).y))
            {
                doTurn(pathList.get(i).x,pathList.get(i).y);
            }
            else
            {
                w.doAction(World.A_MOVE);
            }
        }
        
        
        if(shouldTurn(goal[0], goal[1]))
        {
            int pDir = w.getDirection();
            px = w.getPlayerX();
            py = w.getPlayerY();
            dx = goal[0] - px;
            dy = goal[1] - py;
        
        
            if(dx > 0)
            {
                if(pDir == 0)
                {
                    w.doAction(World.A_TURN_RIGHT);
                }
                else if(pDir == 2)
                {
                    w.doAction(World.A_TURN_LEFT);
                }
                else if(pDir == 3)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                }
            }
            else if(dx < 0)
            {
                if(pDir == 0)
                {
                    w.doAction(World.A_TURN_LEFT);
                }
                else if(pDir == 2)
                {
                    w.doAction(World.A_TURN_RIGHT);
                }
                else if(pDir == 1)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                }
            }
            else if(dy > 0)
            {
                if(pDir == 1)
                {
                    w.doAction(World.A_TURN_LEFT);
                }
                else if(pDir == 2)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                }
                else if(pDir == 3)
                {
                    w.doAction(World.A_TURN_RIGHT);
                }
            }
            if(dy < 0)
            {
                if(pDir == 0)
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                }
                else if(pDir == 1)
                {
                    w.doAction(World.A_TURN_RIGHT);
                }
                else if(pDir == 3)
                {
                    w.doAction(World.A_TURN_LEFT);
                }
            }
            
            w.doAction(World.A_SHOOT);

            
        }
        else
        {
            w.doAction(World.A_SHOOT);
        }
        
        for(int i = 0;i < worldList.size();i++)
        {
            if(worldList.get(i).contains[0] == true)
            {
                worldList.get(i).contains[0] = false;
                worldList.get(i).trueDanger[0] = false;
            }    
            if(worldList.get(i).contains[2] == true)
            {
                worldList.get(i).contains[2] = false;
            }

        }
        
    }
    
    private void setPit(int px,int py)
    {
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == px && worldList.get(i).y == py)
            {
                worldList.get(i).contains[1] = true;
                worldList.get(i).trueDanger[1] = true;
                worldList.get(i).safe = false;

            }
            if(worldList.get(i).x == px+1 && worldList.get(i).y == py)
            {
                worldList.get(i).contains[3] = true;
                worldList.get(i).used[1] = true;
            }
            if(worldList.get(i).x == px && worldList.get(i).y == py+1)
            {
                worldList.get(i).contains[3] = true;
                worldList.get(i).used[1] = true;
            }
            if(worldList.get(i).x == px-1 && worldList.get(i).y == py)
            {
                worldList.get(i).contains[3] = true;
                worldList.get(i).used[1] = true;
            }
            if(worldList.get(i).x == px && worldList.get(i).y == py-1)
            {
                worldList.get(i).contains[3] = true;
                worldList.get(i).used[1] = true;
            }   
        }
    }
    
    private boolean possibleMove(int x, int y)
    {
        int px = w.getPlayerX();
        int py = w.getPlayerY();
        
        int dx = x - px;
        int dy = y - py;
        
        int pi = 0;
        for(int i = 0; i < worldList.size();i++)
        {
            if(worldList.get(i).x == px && worldList.get(i).y == py)
            {
                pi = i;
            }
        }
        
        if(Math.abs(dx) + Math.abs(dy) == 1)
        {
            if(shouldTurn(x,y))
            {
                doTurn(x,y);
            }
            else
            {
                w.doAction(World.A_MOVE);
            }
        }
        else
        {
            List<worldInfo> pathList = new ArrayList<worldInfo>();
            pathList.add(worldList.get(pi));

            
            while(true)
            {
                int tx = pathList.get(pathList.size()-1).x;
                int ty = pathList.get(pathList.size()-1).y;
                int bestChoise[] = new int[2];
                bestChoise[0] = 100;
                bestChoise[1] = 100;   
                dx = x - tx;
                dy = y - ty;
                
                if(Math.abs(dx) + Math.abs(dy) == 1)
                {
                    for(int i = 0; i < worldList.size();i++)
                    {
                        if(worldList.get(i).x == x && worldList.get(i).y == y)
                        {
                            pathList.add(worldList.get(i));
                        }
                    }
                }
                else
                {
                    for(int i = 0; i < worldList.size();i++)
                    {
                        if(Math.abs(worldList.get(i).x - tx) + Math.abs(worldList.get(i).y - ty) == 1)
                        {
                            if( worldList.get(i).safe == true)
                            {
                                if(Math.abs(worldList.get(i).x - x) + Math.abs(worldList.get(i).y - y) < bestChoise[0] )
                                {
                                    boolean beenThere = false;
                                    for(int j = 0; j < pathList.size();j++)
                                    {
                                        if(pathList.get(j) == worldList.get(i))
                                        {
                                            beenThere = true;
                                        } 
                                    }
                                    if(beenThere == false)
                                    {
                                        bestChoise[0] = worldList.get(i).x - x + worldList.get(i).y - y;
                                        bestChoise[1] = i;
                                    }
                                }
                            }
                        }
                    }

                    if(bestChoise[1] == 100)
                    {
                        return false;
                    }
                    else
                    {
                        pathList.add(worldList.get(bestChoise[1]));
                    }
                }
                
                if(pathList.get(pathList.size()-1).x == x && pathList.get(pathList.size()-1).y == y)
                {
                    break;
                }
                
                if(pathList.size() > 1000)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
}
