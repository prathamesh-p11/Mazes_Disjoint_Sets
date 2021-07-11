package Maze;

import java.util.*;
public enum Walls
{
        tPositive,
        tNegative,
        zPositive,
        zNegative,
        yPositive,
        yNegative,
        xPositive,
        xNegative;
        
        public static Walls GetRandomWall() {
                Random randomwall = new Random();
                return values()[randomwall.nextInt(values().length)];
        }
}
