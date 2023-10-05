import java.util.*;

public class KMeans{
    private int k, features, sample; //Number of classes
    private Node centroid[];
    private Node nodes[];
    public KMeans(int k, int cols, int rows){
        this.k=k;
        features=cols;
        sample=rows;
        centroid=new Node[k];
        nodes=new Node[rows];
    }

    public void fit(List<List<Float>> data){
        Random rand=new Random();
        //create Node objects, each represents a row of data
        for(int i=0;i<sample;i++){
            int state=rand.nextInt(k)+1;
            nodes[i]=new Node(data.get(i), state);
        }

        float mini[]=new float[features];
        float maxi[]=new float[features];
        for(int j=0;j<features;j++){
            mini[j]=data.get(0).get(j);
            maxi[j]=data.get(0).get(j);
        }
        for(int i=1;i<sample;i++){
            for(int j=0;j<features;j++){
                float datum=data.get(i).get(j);
                if(mini[j]>datum)
                    mini[j]=datum;
                if(maxi[j]<datum)
                    maxi[j]=datum;
            }
        }
        for(int i=0;i<features;i++){
            maxi[i]*=1.1;
            mini[i]*=0.9;
        }

        //Create random centroids
        for(int i=0;i<k;i++){
            List<Float> randAttrs=new ArrayList<>(features);
            for(int j=0;j<features;j++){
                float datum=rand.nextFloat(maxi[j]-mini[j])+mini[j];
                randAttrs.add(j, datum);
            }
            centroid[i]=new Node(randAttrs, 0);
        }
    }
}