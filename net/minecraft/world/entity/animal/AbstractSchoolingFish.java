package net.minecraft.world.entity.animal;

import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import java.util.stream.Stream;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public abstract class AbstractSchoolingFish extends AbstractFish {
    private AbstractSchoolingFish leader;
    private int schoolSize;
    
    public AbstractSchoolingFish(final EntityType<? extends AbstractSchoolingFish> aqb, final Level bru) {
        super(aqb, bru);
        this.schoolSize = 1;
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
    }
    
    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }
    
    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }
    
    @Override
    protected boolean canRandomSwim() {
        return !this.isFollower();
    }
    
    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }
    
    public AbstractSchoolingFish startFollowing(final AbstractSchoolingFish azv) {
        (this.leader = azv).addFollower();
        return azv;
    }
    
    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }
    
    private void addFollower() {
        ++this.schoolSize;
    }
    
    private void removeFollower() {
        --this.schoolSize;
    }
    
    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level.random.nextInt(200) == 1) {
            final List<AbstractFish> list2 = this.level.<AbstractFish>getEntitiesOfClass((java.lang.Class<? extends AbstractFish>)this.getClass(), this.getBoundingBox().inflate(8.0, 8.0, 8.0));
            if (list2.size() <= 1) {
                this.schoolSize = 1;
            }
        }
    }
    
    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }
    
    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 121.0;
    }
    
    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.0);
        }
    }
    
    public void addFollowers(final Stream<AbstractSchoolingFish> stream) {
        stream.limit((long)(this.getMaxSchoolSize() - this.schoolSize)).filter(azv -> azv != this).forEach(azv -> azv.startFollowing(this));
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        if (aqz == null) {
            aqz = new SchoolSpawnGroupData(this);
        }
        else {
            this.startFollowing(((SchoolSpawnGroupData)aqz).leader);
        }
        return aqz;
    }
    
    public static class SchoolSpawnGroupData implements SpawnGroupData {
        public final AbstractSchoolingFish leader;
        
        public SchoolSpawnGroupData(final AbstractSchoolingFish azv) {
            this.leader = azv;
        }
    }
}
