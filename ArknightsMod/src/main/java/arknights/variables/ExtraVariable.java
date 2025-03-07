package arknights.variables;

import basemod.abstracts.DynamicVariable;

import com.megacrit.cardcrawl.cards.AbstractCard;

import arknights.cards.base.ArknightsModCard;

public abstract class ExtraVariable extends DynamicVariable {
    
    public static final int GENERAL_2nd_MAGIC_NUMBER_INDEX = 0;
    public static final int GENERAL_3rd_MAGIC_NUMBER_INDEX = 1;
    
    public static final int MAGIC_DAMAGE_INDEX = 3;
    
    public static final int EXTRA_MAGIC_NUMBER_SIZE = 4;
    
    
    private final int extraIndex;
    private final String key;
    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.

    public ExtraVariable(int extraIndex, String key) {
        this.extraIndex = extraIndex;
        this.key = key;
    }
    
    @Override
    public String key() {
        return key;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((ArknightsModCard) card).extraMagicNumberModifieds[extraIndex];
    }

    @Override
    public int value(AbstractCard card) {
        return ((ArknightsModCard) card).getExtraMagicNumber(extraIndex);
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((ArknightsModCard) card).baseExtraMagicNumbers[extraIndex];
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((ArknightsModCard) card).extraMagicNumberUpgradeds[extraIndex];
    }
}