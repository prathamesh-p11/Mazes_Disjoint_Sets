package Maze;

public class Cell {
    public int walls;
    //public String name;
    private Cell root;
    private int rank; 
    public Cell() 
    {
        this.rank = 0;
        this.walls = ~0;
    }
    public Cell(int t, int z, int y, int x)
    {
        this.walls = ~0;          //initially all walls are up for all cells
        //name = String.valueOf(t) + String.valueOf(z) + String.valueOf(y) + String.valueOf(x);
        this.root = null;
        this.rank = 0;
    }
    public void SetRank(int r)
    {
        rank = r;
    }
    public int getRank()
    {
        return rank;
    }

    public void setRoot(Cell newRoot)
    {
        root = newRoot;
    }
    public Cell getRoot()
    {
        return root;
    }

    public boolean CheckWall(Walls w, int t, int z, int y, int x)
    {
        
        //00 = 0 = there's no wall to break
        //01 = -1 = wall in -ve direction
        //10 = 1 = wall in +ve direction
        //11 = -999 = wall in +ve and -ve direction        
        if(w == Walls.xPositive)
        {
            if((walls&0b00000011) == 0b00000001) // && x+1 < dim)
                return true;
            else
                return false;
        }
        else if(w == Walls.xNegative)
        {
            if((walls&0b00000011) == 0b00000010)
                return true;
            else
                return false;
        }
        else if(w == Walls.yPositive)
        {
            if((walls&0b00001100) == 0b00000100)
                return true;
            else
                return false;
        }
        else if(w == Walls.yNegative)
        {
            if((walls&0b00001100) == 0b00001000)
                return true;
            else
                return false;
        }
        
        else if(w == Walls.zPositive)
        {
            if((walls&0b00110000) == 0b00010000)
                return true;
            else
                return false;
        }
        else if(w == Walls.zNegative)
        {
            if((walls&0b00110000) == 0b00100000)
                return true;
            else
                return false;
        }
        
        else if(w == Walls.tPositive)
        {
            if((walls&0b11000000) == 0b01000000)
                return true;
            else
                return false;
        }
        else if(w == Walls.tNegative)
        {
            if((walls&0b11000000) == 0b10000000)
                return true;
            else
                return false;
        }

        else
            return true;

    }
    
    public void removeWall(Walls w)
    {
        if(w == Walls.xPositive)
            walls = walls & 0b11111110;
        else if(w == Walls.xNegative)
            walls = walls & ~0b11111101;

        else if(w == Walls.yPositive)
            walls = walls & 0b11111011;
        else if(w == Walls.yNegative)
            walls = walls & ~0b11110111;

        else if(w == Walls.zPositive)
            walls = walls & 0b11101111;
        else if(w == Walls.zNegative)
            walls = walls & ~0b11011111;
        
        else if(w == Walls.tPositive)
            walls = walls & 0b10111111;
        else if(w == Walls.tNegative)
            walls = walls & ~0b01111111;        
    }
}
