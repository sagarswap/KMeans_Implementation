import java.util.*;

public class Project2 {
    static String path=".//Data//Iris//Iris//iris.data";
    //static String path=".//Data//wine//wine//wine.data";
    public static void main(String[] args) {
        System.out.println("program execution started");
        DataLoader dataLoader=new DataLoader(path);
        List<List<Double>> data=dataLoader.loadData();
        System.out.println("Data Read!");
        int[] labels=dataLoader.getLabels();
        int cols=data.get(0).size(); //get number of features
        int rows=data.size();
        KMeans model=new KMeans(3, cols, rows, "mahanalobis");
        model.fit(data, labels);
        System.out.println("Data Fitted into model");
        model.run(dataLoader.getLabels());
    }
}
