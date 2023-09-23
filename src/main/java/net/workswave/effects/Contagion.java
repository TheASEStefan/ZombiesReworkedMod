package net.workswave.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.workswave.registry.RottedMobEffects;

import java.util.ArrayList;
import java.util.List;

public class Contagion extends MobEffect {


    public Contagion() {
        super(MobEffectCategory.HARMFUL, -13434778);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Items.MILK_BUCKET));
        list.add(new ItemStack(Items.CAKE));
        return list;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
        }
    }

    public boolean isDurationEffectTick(int duration, int intensity) {
        if (this == RottedMobEffects.CONTAGION.get()) {
            int i = 100 >> intensity;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return false;
    }
}
