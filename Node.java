import java.util.*;

public class Node {
    private int state, ans;
    private double features[];

    public Node(List<Double> attrs, int state){ //used for centroid
        features=new double[attrs.size()];
        this.state=state; //default state is unassigned
        this.ans=state;
        for(int i=0;i<attrs.size();i++)
            features[i]=attrs.get(i);
    }

    public Node(List<Double> attrs){
        features=new double[attrs.size()];
        this.state=-1; //default state is unassigned
        for(int i=0;i<attrs.size();i++)
            features[i]=attrs.get(i);
    }

    public Node(int label, List<Double> attrs){ //used for creating data nodes
        features=new double[attrs.size()];
        this.state=-1; //default state is unassigned
        this.ans=label;
        for(int i=0;i<attrs.size();i++)
            features[i]=attrs.get(i);
    }

    public Node(int attrSize, int state){
        features=new double[attrSize];
        this.state=state;
    }

    public int getFeatureSize(){
        return features.length;
    }

    public double[] getFeatures(){
        return features;
    }

    public int getState(){
        return state;
    }

    public int getAns(){
        return ans;
    }

    public double getFeature(int i){
        return features[i];
    }

    public void setState(int state){
        this.state=state;
    }

    public void addAttrs(Node target){
        if(features.length!=target.getFeatureSize()){
            System.out.println("Error: Feature sizes not matching");
            return;
        }
        for(int i=0;i<features.length;i++)
            features[i]+=target.getFeature(i);
    }

    public void divide(int div){
        for(int i=0;i<features.length;i++)
            features[i]/=div;
    }

    public void printAttrs(){
        System.out.print("Sate = "+getState()+" => ");
        for(int i=0;i<features.length;i++)
            System.out.print(features[i]+" ");
        System.out.println();
    }
}
