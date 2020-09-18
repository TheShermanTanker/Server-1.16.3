package net.minecraft.world.level.pathfinder;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.List;

public class Path {
    private final List<Node> nodes;
    private Node[] openSet;
    private Node[] closedSet;
    private int nextNodeIndex;
    private final BlockPos target;
    private final float distToTarget;
    private final boolean reached;
    
    public Path(final List<Node> list, final BlockPos fx, final boolean boolean3) {
        this.openSet = new Node[0];
        this.closedSet = new Node[0];
        this.nodes = list;
        this.target = fx;
        this.distToTarget = (list.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target));
        this.reached = boolean3;
    }
    
    public void advance() {
        ++this.nextNodeIndex;
    }
    
    public boolean notStarted() {
        return this.nextNodeIndex <= 0;
    }
    
    public boolean isDone() {
        return this.nextNodeIndex >= this.nodes.size();
    }
    
    @Nullable
    public Node getEndNode() {
        if (!this.nodes.isEmpty()) {
            return (Node)this.nodes.get(this.nodes.size() - 1);
        }
        return null;
    }
    
    public Node getNode(final int integer) {
        return (Node)this.nodes.get(integer);
    }
    
    public void truncateNodes(final int integer) {
        if (this.nodes.size() > integer) {
            this.nodes.subList(integer, this.nodes.size()).clear();
        }
    }
    
    public void replaceNode(final int integer, final Node cwy) {
        this.nodes.set(integer, cwy);
    }
    
    public int getNodeCount() {
        return this.nodes.size();
    }
    
    public int getNextNodeIndex() {
        return this.nextNodeIndex;
    }
    
    public void setNextNodeIndex(final int integer) {
        this.nextNodeIndex = integer;
    }
    
    public Vec3 getEntityPosAtNode(final Entity apx, final int integer) {
        final Node cwy4 = (Node)this.nodes.get(integer);
        final double double5 = cwy4.x + (int)(apx.getBbWidth() + 1.0f) * 0.5;
        final double double6 = cwy4.y;
        final double double7 = cwy4.z + (int)(apx.getBbWidth() + 1.0f) * 0.5;
        return new Vec3(double5, double6, double7);
    }
    
    public BlockPos getNodePos(final int integer) {
        return ((Node)this.nodes.get(integer)).asBlockPos();
    }
    
    public Vec3 getNextEntityPos(final Entity apx) {
        return this.getEntityPosAtNode(apx, this.nextNodeIndex);
    }
    
    public BlockPos getNextNodePos() {
        return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos();
    }
    
    public Node getNextNode() {
        return (Node)this.nodes.get(this.nextNodeIndex);
    }
    
    @Nullable
    public Node getPreviousNode() {
        return (this.nextNodeIndex > 0) ? ((Node)this.nodes.get(this.nextNodeIndex - 1)) : null;
    }
    
    public boolean sameAs(@Nullable final Path cxa) {
        if (cxa == null) {
            return false;
        }
        if (cxa.nodes.size() != this.nodes.size()) {
            return false;
        }
        for (int integer3 = 0; integer3 < this.nodes.size(); ++integer3) {
            final Node cwy4 = (Node)this.nodes.get(integer3);
            final Node cwy5 = (Node)cxa.nodes.get(integer3);
            if (cwy4.x != cwy5.x || cwy4.y != cwy5.y || cwy4.z != cwy5.z) {
                return false;
            }
        }
        return true;
    }
    
    public boolean canReach() {
        return this.reached;
    }
    
    public String toString() {
        return new StringBuilder().append("Path(length=").append(this.nodes.size()).append(")").toString();
    }
    
    public BlockPos getTarget() {
        return this.target;
    }
    
    public float getDistToTarget() {
        return this.distToTarget;
    }
}
