import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataLoader {
    private String path; //Stores relative path of data file
    private int[] labels;
    public DataLoader(String path){
        this.path=path;
    }    

    public List<List<Double>> loadData(){
        List<String> lines = Collections.emptyList();  
        try{   
            lines=Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);   
        }   
        catch (IOException e){   
            e.printStackTrace();   
        }  
        labels=new int[lines.size()];
        int counter=0;
        List<List<Double>> ans = new ArrayList<List<Double>>();  
        //dataset specific operations based on their respective feature set.
        if(path.contains("wine")){
            for(String s: lines){
                String[] tokens=s.split(",");
                List<Double> a=new ArrayList<>();
                for(int i=1;i<14;i++)
                    a.add(Double.parseDouble(tokens[i]));
                ans.add(a);
                labels[counter]=Integer.parseInt(tokens[0])-1;
                counter++;
            }
        } 
        else{
            for(String s: lines){
                String[] tokens=s.split(",");
                List<Double> a=new ArrayList<>();
                for(int i=0;i<tokens.length-1;i++)
                    a.add(Double.parseDouble(tokens[i]));
                ans.add(a);
                if(tokens[tokens.length-1].equals("Iris-setosa"))
                    labels[counter]=0;
                else if(tokens[tokens.length-1].equals("Iris-versicolor"))
                    labels[counter]=1;
                else if(tokens[tokens.length-1].equals("Iris-virginica"))
                    labels[counter]=2;
                counter++;
            }
        }
        return ans;   
    }  

    public int[] getLabels(){
        return labels;
    }
}
