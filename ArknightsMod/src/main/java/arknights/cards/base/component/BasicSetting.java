package arknights.cards.base.component;

import java.util.HashMap;
import java.util.Map;

import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

import arknights.variables.ExtraVariable;

/**
 * @author hundun
 * Created on 2020/11/16
 */
public class BasicSetting {
    private Integer damage;
    private Integer block;
    private Integer regainBlock;
    private Integer magicNumber;
    private boolean spellDamageType;
    private Map<Integer, Integer> extraMagicNumbers = new HashMap<>(ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE);
    public Integer getDamage() {
        return damage;
    }
    public BasicSetting setDamage(Integer damage) {
        this.damage = damage;
        return this;
    }
    public Integer getBlock() {
        return block;
    }
    public BasicSetting setBlock(Integer block) {
        this.block = block;
        return this;
    }
    public Integer getMagicNumber() {
        return magicNumber;
    }
    public Integer getRegainBlock() {
        return regainBlock;
    }
    public boolean isSpellDamageType() {
        return spellDamageType;
    }
    public BasicSetting setMagicNumber(Integer magicNumber) {
        this.magicNumber = magicNumber;
        return this;
    }
    public Integer getExtraMagicNumber(int index) {
        return extraMagicNumbers.get(index);
    }
    public BasicSetting setExtraMagicNumber(int index, Integer value) {
        this.extraMagicNumbers.put(index, value);
        return this;
    }
    public BasicSetting setRegainBlock(Integer regainBlock) {
        this.regainBlock = regainBlock;
        return this;
    }
    public BasicSetting enableSpellDamageType() {
        this.spellDamageType = true;
        return this;
    }
}
