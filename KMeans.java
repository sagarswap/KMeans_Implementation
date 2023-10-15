import java.util.*;

public class KMeans{
    private int k, features, sample; //Number of classes
    private Node centroids[];
    private Node nodes[];
    String dType;
    public KMeans(int k, int cols, int rows, String dType){
        this.k=k;
        features=cols;
        sample=rows;
        centroids=new Node[k];
        nodes=new Node[rows];
        this.dType=new String(dType);
    }

    //Uses the data list from the DataLoader to create appropriate nodes as well as centroids
    public void fit(List<List<Double>> data){
        Random rand=new Random();
        //create Node objects, each represents a row of data
        for(int i=0;i<sample;i++)
            nodes[i]=new Node(data.get(i));
        
        double mini[]=new double[features];
        double maxi[]=new double[features];
        for(int j=0;j<features;j++){
            mini[j]=data.get(0).get(j);
            maxi[j]=data.get(0).get(j);
        }
        for(int i=1;i<sample;i++){
            for(int j=0;j<features;j++){
                double datum=data.get(i).get(j);
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
            List<Double> randAttrs=new ArrayList<>(features);
            for(int j=0;j<features;j++){
                double datum=rand.nextDouble(maxi[j]-mini[j])+mini[j];
                randAttrs.add(j, datum);
            }
            centroids[i]=new Node(randAttrs, i);
            centroids[i].printAttrs();
        }
    }

    public void run(int[] ansCheck){
        boolean chk;
        do{
            System.out.println("Cluster Cycle Started");
            chk=clusterRun();
        }while(!chk);
        System.out.println("Clustering Completed");
        int ans[]=new int[sample];
        for(int i=0;i<sample;i++){
            ans[i]=nodes[i].getState();
        }
        checkAccuracy(ansCheck, ans);
    }

    private void checkAccuracy(int[] key, int[] myAns){
        if(key.length!=myAns.length){
            System.out.println("The lengths of answers do not match");
            return;
        }
        int an1[]=new int[k], an2[]=new int[k];
        for(int i=0;i<sample;i++){
            an1[key[i]]++;
            an2[myAns[i]]++;
        }
        for(int i=0;i<k;i++){
            System.out.println(an1[i]+" "+an2[i]);
        }
    }

    private boolean clusterRun(){
        Node newCentroids[]=new Node[k];
        int kCount[]=new int[k];
        for(int i=0;i<k;i++){ //initializing new centroids
            newCentroids[i]=new Node(features, i);
            kCount[i]=0;
        }
        //iterating through all nodes to find their closest centroid and change their state accordingly
        for(Node node:nodes){ 
            double distance=Double.MAX_VALUE;
            for(int i=0; i<k; i++){
                Node centroid=centroids[i];
                double d=getDistance(node, centroid);
                if(d<distance){
                    node.setState(i);
                    distance=d;
                }
            }

            for(int i=0; i<features; i++){
                newCentroids[node.getState()].addAttrs(node);
                kCount[node.getState()]++;
            }
        }
        
        for(int i=0;i<k;i++)
            newCentroids[i].divide(kCount[i]); //finds new centroid
        //Check if centroids are close enough
        double diff=0.0;
        for(int i=0;i<k;i++)
            diff=getDistance(centroids[i],newCentroids[i]);
        for(int i=0;i<k;i++)
            this.centroids[i]=newCentroids[i];
        System.out.println(diff);
        if(diff<0.00001)
            return true;
        else
            return false;
    }

    private double getDistance(Node node, Node centroid){
        double dist=0.0;
        for(int i=0;i<features;i++){
            double f1=node.getFeature(i), f2=centroid.getFeature(i);
            if(dType.equals("euclidian"))
                dist+=Math.pow(f2-f1, 2);
            else if(dType.equals("mahanalobis")){
                double diffMatrix[]=new double[features];
                for(int j=0;j<features;j++)
                    diffMatrix[j]=node.getFeature(j)-centroid.getFeature(j);
                double covMatrix[][]=getRandomMatrix();
            }
            else
                System.out.println("Invalid Distance Option");   
        }
        return dist;
    }

    /**
     * Generates a random positive diagonal matrix to be used in Mahanalobis Distance Calculation
     * @return positive diagonal matrix of dimensions features x features
     */
    public double[][] getRandomMatrix(){
        Random rand=new Random();
        double matrix[][]=new double[features][features];
        for(int i=0;i<features;i++)
            matrix[i][i]=rand.nextDouble(0.0, 1.0);
        return matrix;
    }
}