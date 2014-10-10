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
        for(int i = 1;i < Math.sqrt(w.getSize());i++)
        {
            for(int j = 1;j < Math.sqrt(w.getSize());j++)
            {
                worldInfo temp = new worldInfo();
                temp.x = i;
                temp.y = j;
                
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
        if( worldList.get(plp).contains[0] == false && 
            worldList.get(plp).contains[1] == false && 
            worldList.get(plp).contains[2] == false && 
            worldList.get(plp).contains[3] == false) // empty
        {
            setSafeZone(px,py);
        }
        
        setTrueDanger();
    }
    
    private void updatePos(int i)
    {
        if(w.hasBreeze(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).safe = true;
            worldList.get(i).contains[3] = true;
        }
        if(w.hasStench(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).safe = true;
            worldList.get(i).contains[2] = true;
        }
        if(w.hasPit(worldList.get(i).x, worldList.get(i).y))
        {
            worldList.get(i).safe = false;
            worldList.get(i).contains[1] = true;
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
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;

                    }
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(worldList.get(i).safe == false)
                    if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                    {
                        worldList.get(i).safe = false;
                        worldList.get(i).contains[danger] = true;

                    }
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
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
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                worldList.get(i).safe = true;
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                worldList.get(i).safe = true;
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
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
                        for(int k = 0; i < worldList.size();k++)
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
                        }
                    }
                     if(w.isVisited(x-1, y+1))
                    {
                        int markers = 0;
                        for(int k = 0; i < worldList.size();k++)
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
                        }
                    }   
                    if(w.isVisited(x-1, y-1))
                    {
                        int markers = 0;
                        for(int k = 0; i < worldList.size();k++)
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
                        }
                    }
                    if(w.isVisited(x+1, y-1))
                    {
                        int markers = 0;
                        for(int k = 0; i < worldList.size();k++)
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
                    
                }
            }
        }
    }
    
    
    
    
    
}
