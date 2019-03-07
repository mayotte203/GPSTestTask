package com.epam.impl;

import java.util.*;
import java.io.*;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;

public class MyGPS implements GpsNavigator{
    private Map<String,ArrayList<Road>> points = new HashMap<>();
    private Map<String,Path> paths = new HashMap<>();
    private Map<String,Boolean> isChecked = new HashMap<>();
    private Map<String,Boolean> isRepeated = new HashMap<>();

    public void readData(String filePath) throws IOException{
        FileReader file = new FileReader(filePath);
        Scanner scanner = new Scanner(file);
        ArrayList<String[]> data = new ArrayList<>();
        String str;
        while(scanner.hasNextLine())
        {
            str = scanner.nextLine();
            if(!str.matches("\\w+\\s\\w+\\s\\d+\\s\\d+\\s*$"))
            {
                throw(new IOException("Invalid file format"));
            }
            data.add(str.split(" "));
        }
        for(int i = 0; i < data.size(); i++)
        {
            if(!points.containsKey(data.get(i)[0]))
            {
                points.put(data.get(i)[0], new ArrayList<Road>());
                paths.put(data.get(i)[0], new Path(new ArrayList<String>(), -1));
            }
            if(!points.containsKey(data.get(i)[1]))
            {
                points.put(data.get(i)[1], new ArrayList<Road>());
                paths.put(data.get(i)[1], new Path(new ArrayList<String>(), -1));
            }
            points.get(data.get(i)[0]).add(new Road(data.get(i)[1], Integer.parseInt(data.get(i)[2]), Integer.parseInt(data.get(i)[3])));
        }

    }

    @Override
    public Path findPath(String pointA, String pointB) throws Exception{
        if(!points.containsKey(pointA) || !points.containsKey(pointB))
        {
            throw(new Exception("Invalid point"));
        }

        for (Map.Entry<String, Path> entry : paths.entrySet()) {
            entry.setValue(new Path(new ArrayList<String>(), -1));
            isChecked.put(entry.getKey(), false);
            isRepeated.put(entry.getKey(), false);
        }

        paths.put(pointA, new Path(Collections.singletonList(pointA), 0));
        String currentPoint = pointA;

        int minCost = -1;
        while(true) {
            ArrayList<Road> roads = points.get(currentPoint);
            int size = points.get(currentPoint).size();
            for (int i = 0; i < size; i++) {
                if (paths.get(roads.get(i).destination).cost == -1
                        || paths.get(roads.get(i).destination).cost >= paths.get(currentPoint).cost + roads.get(i).getCost()) {
                    if(paths.get(roads.get(i).destination).cost > paths.get(currentPoint).cost + roads.get(i).getCost() || paths.get(roads.get(i).destination).cost == -1)
                    {
                        isRepeated.put(roads.get(i).destination, false);
                    }
                    else
                    {
                        isRepeated.put(roads.get(i).destination, true);
                    }

                    List<String> path = new ArrayList<String> (paths.get(currentPoint).path);
                    path.add(roads.get(i).destination);
                    paths.put(roads.get(i).destination, new Path(path, paths.get(currentPoint).cost + roads.get(i).getCost()));
                }
            }
            isChecked.put(currentPoint, true);
            String oldPoint = currentPoint;
            minCost = -1;
            for (Map.Entry<String, Path> entry : paths.entrySet()) {
                if (!isChecked.get(entry.getKey()) && (minCost == -1 || minCost > entry.getValue().cost) && paths.get(entry.getKey()).cost != -1) {
                    minCost = entry.getValue().cost;
                    currentPoint = entry.getKey();
                }
            }
            if (currentPoint == oldPoint)
            {
                throw(new Exception("No path"));
            }
            if(currentPoint.equals(pointB))
            {
                Path resultPath = paths.get(pointB);
                for (Map.Entry<String, Boolean> entry : isRepeated.entrySet()) {
                    if(entry.getValue())
                    {
                        if(resultPath.path.contains(entry.getKey()))
                        {
                            throw(new Exception("More than one path found"));
                        }
                    }
                }
                return resultPath;
            }
        }
    }
}
