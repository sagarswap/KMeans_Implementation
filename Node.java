import java.util.*;

public class Node {
    private int state;
    private float features[];

    public Node(List<Float> attrs, int state){
        features=new float[attrs.size()];
        this.state=state; //default state is unassigned
        for(int i=0;i<attrs.size();i++)
            features[i]=attrs.get(i);
    }

    public int getFeatureSize(){
        return features.length;
    }

    public float[] getFeatures(){
        return features;
    }

    public int getState(){
        return state;
    }

    public float getFeature(int i){
        return features[i];
    }
}
