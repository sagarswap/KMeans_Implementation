import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataLoader {
    private String path; //Stores relative path of data file
    public DataLoader(String path){
        this.path=path;
    }    

    public List<List<Float>> loadData(){
        List<String> lines = Collections.emptyList();  
        try{   
            lines=Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);   
        }   
        catch (IOException e){   
            e.printStackTrace();   
        }  
        List<List<Float>> ans = new ArrayList<List<Float>>();  
        //dataset specific operations based on their respective feature set.
        if(path.contains("wine")){
            for(String s: lines){
                String[] tokens=s.split(",");
                List<Float> a=new ArrayList<>();
                for(int i=1;i<14;i++)
                    a.add(Float.parseFloat(tokens[i]));
                ans.add(a);
            }
        } 
        else{
            for(String s: lines){
                String[] tokens=s.split(",");
                List<Float> a=new ArrayList<>();
                for(int i=0;i<tokens.length-1;i++)
                    a.add(Float.parseFloat(tokens[i]));
                ans.add(a);
            }
        }
        return ans;   
    }  
}
