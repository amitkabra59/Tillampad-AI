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
        public boolean safe = true;
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
                if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                {
                    worldList.get(i).safe = false;
                    worldList.get(i).contains[danger] = true;
                }
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                {
                    worldList.get(i).safe = false;
                    worldList.get(i).contains[danger] = true;

                }
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                {
                    worldList.get(i).safe = false;
                    worldList.get(i).contains[danger] = true;

                }
            }
            if(worldList.get(i).x == x+1 && worldList.get(i).y == y )
            {
                if(w.isVisited(worldList.get(i).x, worldList.get(i).y) == false)
                {
                    worldList.get(i).safe = false;
                    worldList.get(i).contains[danger] = true;

                }
            }
        }
    }
}
