import java.util.*;

public class KMeans{
    private int k, features, sample; //Number of classes
    private Node centroids[];
    private Node nodes[];
    private int labels[];
    private double covMatrix[][];
    String dType;
    public KMeans(int k, int cols, int rows, String dType){
        this.k=k;
        features=cols;
        sample=rows;
        centroids=new Node[k];
        nodes=new Node[rows];
        this.dType=new String(dType);
        this.labels=new int[rows];
    }

    //Uses the data list from the DataLoader to create appropriate nodes as well as centroids
    public void fit(List<List<Double>> data, int[] labels){
        Random rand=new Random();
        //create Node objects, each represents a row of data
        for(int i=0;i<sample;i++)
            nodes[i]=new Node(labels[i], data.get(i));
        
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

        //Create random centroids
        for(int i=0;i<k;i++){
            List<Double> randAttrs=new ArrayList<>(features);
            for(int j=0;j<features;j++){
                double datum=rand.nextDouble(maxi[j]-mini[j])+mini[j];
                randAttrs.add(j, datum);
            }
            centroids[i]=new Node(randAttrs, i);
            //centroids[i].printAttrs();
        }
        //Create covMatrix if Mahanalobis Distance
        if(dType.equals("mahanalobis"))
            covMatrix=getRandomMatrix();
    }

    public void run(int[] ansCheck){
        clusterRun();
        System.out.println("Clustering Completed");
        checkAccuracy();
    }

    private void checkAccuracy(){
        for(int cluster=0; cluster<this.k; cluster++){
            int[] countClass=new int[k];
            int tp=0;
            int fp=0;
            int fn=0;
            System.out.println("_________________________");
            Node centroid=centroids[cluster];
            if(centroid.getState()!=cluster)
                System.out.println("Error!");
            
            for(Node node:nodes){ //first find majority class
                if(node.getAns()==cluster)
                    countClass[node.getState()]++;
                //System.out.println(node.getAns());
            }
            int curStat=-1, stateCount=-1;
            for(int i=0;i<k;i++)
                System.out.print(countClass[i]+" ");
            System.out.println();
            for(int i=0;i<k;i++){
                if(countClass[i]>stateCount){
                    stateCount=countClass[i];
                    curStat=i;
                }
            }
            System.out.println("Current State = "+curStat);
            for(Node node:nodes){
                if(curStat==node.getState() && node.getState()==node.getAns()){
                    tp++;
                    //System.out.print("*");
                }
                if(curStat==node.getState() && node.getState()!=node.getAns()){
                    fp++;
                }
                if(curStat!=node.getState() && curStat==node.getAns())
                    fn++;
            }
            System.out.println("tp = "+tp+"\tfp = "+fp+"\t fn = "+fn );

            double precision=tp*100.0/(tp+fp);
            double recall=tp*100.0/(tp+fn);
            double f1=2.0*precision*recall/(precision+recall);
            int sum=0;
            for(int i=0;i<k;i++)
                sum+=countClass[i];
            double accuracy=tp*100.0/sum;
            System.out.println("Results of Cluster No. "+(cluster+1));
            System.out.println("Accuracy = "+accuracy+"%");
            System.out.println("Precision = "+precision+"%");
            System.out.println("Recall = "+recall+"%");
            System.out.println("f1 Score = "+f1);
            System.out.println("__________________________");
        }
    }

    private void clusterRun(){
        Node newCentroids[]=new Node[k];
        int kCount[]=new int[k];
        for(int i=0;i<k;i++){ //initializing new centroids
            newCentroids[i]=new Node(features, i);
            kCount[i]=0;
        }
        boolean change=false;;
        //iterating through all nodes to find their closest centroid and change their state accordingly
        do{
            for(Node node:nodes){ 
                double distance=Double.MAX_VALUE;
                for(int i=0; i<k; i++){
                    Node centroid=centroids[i];
                    double d=getDistance(node, centroid);
                    if(d<distance){
                        node.setState(centroid.getState());
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
            change=false;
            for(int i=0;i<sample;i++){
                if(labels[i]!=nodes[i].getState()){
                    change=true;
                    //System.out.println("Change in labels detected");
                    labels[i]=nodes[i].getState();
                }
            }
        }while(change);
    }

    private double getDistance(Node node, Node centroid){
        double dist=0.0;

        if(dType.equals("mahanalobis")){
            double diffMatrix[]=new double[features];
            for(int j=0;j<features;j++)
                diffMatrix[j]=node.getFeature(j)-centroid.getFeature(j);
            double prodMat[]=matrixMultiply(diffMatrix, covMatrix);
            dist=matrixMultiply(prodMat, diffMatrix);
        }
        else if (dType.equals("euclidian")){
            for(int i=0;i<features;i++){
                double f1=node.getFeature(i), f2=centroid.getFeature(i);
                dist+=Math.pow(f2-f1, 2);
            }
        }
        else
            System.out.println("Invalid Distance Option");
        return dist;
    }

    /**
     * Generates a random positive diagonal matrix to be used in Mahanalobis Distance Calculation
     * @return positive diagonal matrix of dimensions features x features
     */
    private double[][] getRandomMatrix(){
        Random rand=new Random();
        double matrix[][]=new double[features][features];
        for(int i=0;i<features;i++)
            matrix[i][i]=rand.nextDouble(0.0, 1.0);
        return matrix;
    }

    private double[] matrixMultiply(double diffMatrix[], double covMatrix[][]){
        double ans[]=new double[features];
        for(int i=0;i<features;i++) //Since we know that we are dealing with a positive diagonal matrix, I have decided to simplify the algorithm and decrease time complexity
            ans[i]=diffMatrix[i]*covMatrix[i][i];
        return ans;
    }

    private double matrixMultiply(double matrix1[], double matrix2[]){
        double dist=0.0;
        for(int i=0;i<features;i++)
            dist+=matrix1[i]*matrix2[i];
        return dist;
    }
}