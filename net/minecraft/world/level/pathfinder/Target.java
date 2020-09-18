package net.minecraft.world.level.pathfinder;

public class Target extends Node {
    private float bestHeuristic;
    private Node bestNode;
    private boolean reached;
    
    public Target(final Node cwy) {
        super(cwy.x, cwy.y, cwy.z);
        this.bestHeuristic = Float.MAX_VALUE;
    }
    
    public void updateBest(final float float1, final Node cwy) {
        if (float1 < this.bestHeuristic) {
            this.bestHeuristic = float1;
            this.bestNode = cwy;
        }
    }
    
    public Node getBestNode() {
        return this.bestNode;
    }
    
    public void setReached() {
        this.reached = true;
    }
}
