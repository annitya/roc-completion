package Completions;

import java.util.ArrayList;

public class Container
{
    public String name;
    public Integer level;
    public ArrayList<Completion> objects;
    public ArrayList<Container> children;
    public Boolean raw;
}
