package Maze;
import java.util.*;

public class StudentSolver //Maze class
{
    private int dimension;
    public Cell c[][][][]; 
    private int NumberOfSets = 0;
    Walls w;
    public static byte b[];

    StudentSolver()
    {
        setDimension();
        c = new Cell[dimension][dimension][dimension][dimension];       //Cells in 4D structure
        for(int t = 0; t<dimension; t++)
        {
            for(int z = 0; z<dimension; z++)
            {
                for(int y = 0; y<dimension; y++)
                {
                    for(int x = 0; x<dimension; x++)
                    {
                        TEMPLOG("Creating cell" + t + z + y + x, 0, false);
                        this.c[t][z][y][x] = new Cell(t,z,y,x);
                        this.c[t][z][y][x].setRoot(c[t][z][y][x]);
                    }
                }
            }
        }//for loop ending
        
        setNumberOfSets();
        b = new byte[NumberOfSets];
    }//constructor ending

    public void TEMPLOG(String message, int newLines, boolean toPrint)
    {
        Boolean print = toPrint;
        //Boolean print = false;
        if(print == true)
        {
            for(int i=0;i<newLines; i++)
                System.out.println();
            System.out.println(message);
        }
    }

    public void setDimension()
    {
        while(dimension <= 0 || dimension > 40)
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter dimension for maze: (N x N x N x N)");
            dimension = scan.nextInt();
            scan.close();
        }
    }

    public int getDimension()
    {
        return dimension;
    }
    
    public Cell getCell(int t, int z, int y, int x)
    {
        return c[t][z][y][x];
    }

    private void setNumberOfSets()
    {
        NumberOfSets = (int)Math.pow(dimension,4); //initially each Cell is a set by itself 
    }
    
    public int getNumberOfSets()
    {
        return NumberOfSets;
    }

    public int GetRandomNumberInRange(int low, int high)        //Should return random number between [low, high)
    {
        return (int) ((Math.random() * (high - low)) + low);
    }
    public void generateMaze(StudentSolver m)
    {
        TEMPLOG("Generating MAZE!!", 2, false);
        Cell CurrentCell;
        Cell Neighbor;
        while(NumberOfSets!=1)      //Repeat till we don't get final single set of all sets
        {
            TEMPLOG("OuterWhile", 0, false);
            boolean walldown = false;
            while(!walldown)
            {
                TEMPLOG("InnerWhile", 0, false);
                //Get random cell coordinates
                int t1, z1, y1, x1;
                t1 = GetRandomNumberInRange(0, dimension);
                z1 = GetRandomNumberInRange(0, dimension);
                y1 = GetRandomNumberInRange(0, dimension);
                x1 = GetRandomNumberInRange(0, dimension);
                TEMPLOG("rand cell coord = " + t1 + z1 + y1 + x1, 2, false);
                CurrentCell = m.c[t1][z1][y1][x1];

                HashMap<Walls, Boolean> wallToTakeDown = new HashMap<Walls, Boolean>();
                for(Walls w : Walls.values())       //Create a hashMap to store whether we can take down a wall or not
                {
                    TEMPLOG("W == " + w.toString(), 4, false);
                    Boolean canTakeDown = CurrentCell.CheckWall(w, t1, z1, y1, x1);
                    if(canTakeDown && !wallToTakeDown.containsKey(w))             //only store walls which we can take down
                        wallToTakeDown.put(w, canTakeDown);
                }
               
                Walls SelectedWall;
                do
                {            
                    TEMPLOG("Wall Logic", 0, false);
                    do      //randomly select a wall and repeat till we get a valid wall
                    {
                        SelectedWall = Walls.GetRandomWall();
                    }while(wallToTakeDown.containsKey(SelectedWall) && wallToTakeDown.get(SelectedWall)!=true);

                    TEMPLOG("TAKE DOWN : " + SelectedWall.toString(),0, false);
                    wallToTakeDown.remove(SelectedWall);


                    if(SelectedWall == Walls.xPositive)
                    {
                        if(x1+1 < dimension)
                            Neighbor = c[t1][z1][y1][x1 + 1];
                        else
                            Neighbor = CurrentCell;
                    }
                    else if(SelectedWall == Walls.xNegative)
                    {
                        if(x1-1 >= 0)
                            Neighbor = c[t1][z1][y1][x1 - 1];
                        else
                            Neighbor = CurrentCell;
                    }
                    
                    
                    else if(SelectedWall == Walls.yPositive)
                    {
                        if(y1+1 < dimension)
                            Neighbor = c[t1][z1][y1+1][x1];
                        else
                            Neighbor = CurrentCell;
                    }
                    else if(SelectedWall == Walls.yNegative)
                    {
                        if(y1-1 >= 0)
                            Neighbor = c[t1][z1][y1-1][x1];
                        else
                            Neighbor = CurrentCell;
                    }
                    
                    
                    else if(SelectedWall == Walls.zPositive)
                    {
                        if(z1+1 < dimension)
                            Neighbor = c[t1][z1+1][y1][x1];
                        else
                            Neighbor = CurrentCell;
                    }
                    else if(SelectedWall == Walls.zNegative)
                    {
                        if(z1-1 >= 0)
                            Neighbor = c[t1][z1-1][y1][x1];
                        else
                            Neighbor = CurrentCell;
                    }

                    
                    else if(SelectedWall == Walls.tPositive)
                    {
                        if(t1+1 < dimension)
                            Neighbor = c[t1+1][z1][y1][x1];
                        else
                            Neighbor = CurrentCell;
                    }
                    else if(SelectedWall == Walls.tNegative)
                    {
                        if(t1-1 >= 0)
                            Neighbor = c[t1-1][z1][y1][x1];
                        else
                            Neighbor = CurrentCell;
                    }
                    else
                    {
                        Neighbor = CurrentCell;
                    }

                    if(Neighbor == null)
                    {
                        Neighbor = CurrentCell;
                    }
                }while(find(CurrentCell) == find(Neighbor) && (!wallToTakeDown.isEmpty()) );

                //two cells have same root => they belong to same set => loop!!!
                if(find(CurrentCell) != find(Neighbor))
                {
                    TEMPLOG("Union", 0, false);
                    //Put them into same set by using union by rank
                    union(CurrentCell, Neighbor);
                    TEMPLOG("THIS WALL IS SELECTED :" + SelectedWall.toString(), 1, false);
                    CurrentCell.removeWall(SelectedWall);
                    
                    if(SelectedWall == Walls.xPositive)
                        Neighbor.removeWall(Walls.xNegative);
                    else if(SelectedWall == Walls.xNegative)
                        Neighbor.removeWall(Walls.xPositive);
                    
                    else if(SelectedWall == Walls.yPositive)
                        Neighbor.removeWall(Walls.yNegative);
                    else if(SelectedWall == Walls.yNegative)
                        Neighbor.removeWall(Walls.yPositive);

                    else if(SelectedWall == Walls.zPositive)
                        Neighbor.removeWall(Walls.zNegative);
                    else if(SelectedWall == Walls.zNegative)
                        Neighbor.removeWall(Walls.zPositive);

                    else if(SelectedWall == Walls.tPositive)
                        Neighbor.removeWall(Walls.tNegative);
                    else if(SelectedWall == Walls.tNegative)
                        Neighbor.removeWall(Walls.tPositive);
                    TEMPLOG("compare root ends here", 2, false);
                    walldown = true;
                }
            }
        }
    
    }

    public Cell find(Cell c) 
    {
        TEMPLOG("FIND START", 2, false);
        //if current cell points to itself or is the root of set itself, return current //refer dia.
        if (c == c.getRoot()) 
            return c;
         
        //Recursively compress the path, go up and set every cell's root to the set's root
        else {
            
            TEMPLOG("PATH COMPRESSION CURRENTLY AT" + c.getRoot(), 1, false);
            c.setRoot(find(c.getRoot()));       //check TESTING.java to test first
            return c.getRoot();                 //return root of set
        }
    }

    //union two sets
    public void union(Cell set1, Cell set2) 
    {
        TEMPLOG("IN UNION", 2, false);

        TEMPLOG("BEFORE FIND 1 ERROR!!", 5, false);
        set1 = find(set1);
        TEMPLOG("AFTER FIND 1 ERROR?", 2, false);

        TEMPLOG("BEFORE FIND 2 ERROR!!", 5, false);
        set2 = find(set2);
        TEMPLOG("AFTER FIND 2 ERROR?", 2, false);

        TEMPLOG("NUMSET" + NumberOfSets, 2, false);   
        if(set1 != set2)  //not forming loop
        {
            //union by rank
            //r1 > r2 => root = r1 and vice versa
            //r1 == r2 => root = r1 + 1
            if(set1.getRank() > set2.getRank())
                set2.setRoot(set1);
            else if(set1.getRank() < set2.getRank())
                set1.setRoot(set2);
            else
            {
                set2.setRoot(set1);
                set1.SetRank(set1.getRank()+1);
            }
            
            NumberOfSets -=1;
            TEMPLOG("NUMSET" + NumberOfSets, 2, false);   
        }
    }


    public static byte[] solve(int n)
    {
        return b;
    }

    public static void main(String[] args)
    {
        StudentSolver s = new StudentSolver();
        s.generateMaze(s);
        s.TEMPLOG("Printing maze", 2, true);
        int i = 0;
        for(int t = 0; t<s.getDimension(); t++)
        {
            for(int z = 0; z<s.getDimension(); z++)
            {
                for(int y = 0; y<s.getDimension(); y++)
                {
                    for(int x = 0; x<s.getDimension(); x++)
                    {
                        b[i] = ((byte)(s.c[t][z][y][x].walls & 0b11111111));
                        //s.TEMPLOG(b[i] + "   " + t+z+y+x, 0, true);
                        s.TEMPLOG(b[i] + "   " + t+z+y+x, 0, false);

                    }
                }
            }
        }
    }
}