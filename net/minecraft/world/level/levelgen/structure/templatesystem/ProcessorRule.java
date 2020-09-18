package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ProcessorRule {
    public static final Codec<ProcessorRule> CODEC;
    private final RuleTest inputPredicate;
    private final RuleTest locPredicate;
    private final PosRuleTest posPredicate;
    private final BlockState outputState;
    @Nullable
    private final CompoundTag outputTag;
    
    public ProcessorRule(final RuleTest csr1, final RuleTest csr2, final BlockState cee) {
        this(csr1, csr2, PosAlwaysTrueTest.INSTANCE, cee, (Optional<CompoundTag>)Optional.empty());
    }
    
    public ProcessorRule(final RuleTest csr1, final RuleTest csr2, final PosRuleTest csl, final BlockState cee) {
        this(csr1, csr2, csl, cee, (Optional<CompoundTag>)Optional.empty());
    }
    
    public ProcessorRule(final RuleTest csr1, final RuleTest csr2, final PosRuleTest csl, final BlockState cee, final Optional<CompoundTag> optional) {
        this.inputPredicate = csr1;
        this.locPredicate = csr2;
        this.posPredicate = csl;
        this.outputState = cee;
        this.outputTag = (CompoundTag)optional.orElse(null);
    }
    
    public boolean test(final BlockState cee1, final BlockState cee2, final BlockPos fx3, final BlockPos fx4, final BlockPos fx5, final Random random) {
        return this.inputPredicate.test(cee1, random) && this.locPredicate.test(cee2, random) && this.posPredicate.test(fx3, fx4, fx5, random);
    }
    
    public BlockState getOutputState() {
        return this.outputState;
    }
    
    @Nullable
    public CompoundTag getOutputTag() {
        return this.outputTag;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ProcessorRule>create((java.util.function.Function<RecordCodecBuilder.Instance<ProcessorRule>, ? extends App<RecordCodecBuilder.Mu<ProcessorRule>, ProcessorRule>>)(instance -> instance.<RuleTest, RuleTest, PosAlwaysTrueTest, BlockState, java.util.Optional<CompoundTag>>group(RuleTest.CODEC.fieldOf("input_predicate").forGetter((java.util.function.Function<Object, RuleTest>)(csn -> csn.inputPredicate)), RuleTest.CODEC.fieldOf("location_predicate").forGetter((java.util.function.Function<Object, RuleTest>)(csn -> csn.locPredicate)), PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter((java.util.function.Function<Object, PosAlwaysTrueTest>)(csn -> csn.posPredicate)), BlockState.CODEC.fieldOf("output_state").forGetter((java.util.function.Function<Object, BlockState>)(csn -> csn.outputState)), CompoundTag.CODEC.optionalFieldOf("output_nbt").forGetter((java.util.function.Function<Object, java.util.Optional<CompoundTag>>)(csn -> Optional.ofNullable(csn.outputTag)))).<ProcessorRule>apply(instance, ProcessorRule::new)));
    }
}
