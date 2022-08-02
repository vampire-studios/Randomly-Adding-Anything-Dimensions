package io.github.vampirestudios.raa_dimension.generation.dimensions;

import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomToolMaterial implements Tier {

    private transient ResourceLocation materialId;
    private int durability;
    private float miningSpeed;
    private float attackDamage;
    private int miningLevel;
    private int enchantability;
    private float hoeAttackSpeed;
    private float axeAttackDamage;
    private float axeAttackSpeed;
    private float swordAttackDamage;

    public CustomToolMaterial(ResourceLocation materialId, int durability, float miningSpeed, float attackDamage, int miningLevel,
                              int enchantability, float hoeAttackSpeed, float axeAttackDamage, float axeAttackSpeed, float swordAttackDamage) {
        this.materialId = materialId;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.hoeAttackSpeed = hoeAttackSpeed;
        this.axeAttackDamage = axeAttackDamage;
        this.axeAttackSpeed = axeAttackSpeed;
        this.swordAttackDamage = swordAttackDamage;
    }

    @Override
    public int getUses() {
        return durability;
    }

    @Deprecated
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public float getSpeed() {
        return miningSpeed;
    }

    @Deprecated
    public void setMiningSpeed(float miningSpeed) {
        this.miningSpeed = miningSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Deprecated
    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    @Override
    public int getLevel() {
        return miningLevel;
    }

    @Deprecated
    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Deprecated
    public void setEnchantability(int enchantability) {
        this.enchantability = enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Registry.ITEM.get(Utils.addSuffixToPath(materialId, "_cobblestone")));
    }

    public float getHoeAttackSpeed() {
        return hoeAttackSpeed;
    }

    public float getAxeAttackDamage() {
        return axeAttackDamage;
    }

    public float getAxeAttackSpeed() {
        return axeAttackSpeed;
    }

    public float getSwordAttackDamage() {
        return swordAttackDamage;
    }

}
