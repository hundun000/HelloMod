package arknights.cards.base;
import basemod.AutoAdd;
import basemod.abstracts.CustomCard;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import arknights.ArknightsMod;
import arknights.cards.base.component.BasicSetting;
import arknights.cards.base.component.UpgradeSetting;
import arknights.cards.operator.PromotionState;
import arknights.characters.ArknightsPlayer;
import arknights.variables.ExtraVariable;

public abstract class ArknightsModCard extends CustomCard {
	protected final static DamageType MAGICDAMAGETYP_DAMAGE_TYPE = DamageType.HP_LOSS;
	
	protected final CardStrings cardStrings;
    
    protected UpgradeSetting upgradeSetting = new UpgradeSetting();
    
    /*
     * XXXX:  当前属性值，结算了power加成等
     * baseXXX: 基础值，未结算power加成等。卡片效果是“基础伤害下降”，指的是该属性
     * XXXXUpgraded: instance当前的baseXXX和instance初始化时的baseXXX不一样。如升级或卡片效果是“基础伤害下降”。
     * XXXModified: instance当前的XXX和instance的baseXXX不一样。如因为power。
     */
    // extra magic number slots
    private int[] extraMagicNumbers = new int[ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE];        
    public int[] baseExtraMagicNumbers = new int[ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE];
    public boolean[] extraMagicNumberUpgradeds = new boolean[ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE];
    public boolean[] extraMagicNumberModifieds = new boolean[ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE];
    
    private int spCount = -1;
    private int spThreshold = -1;
    private GainSpType gainSpType = GainSpType.NO_SP;
    
    public enum GainSpType {
        NO_SP,
        ON_DRAWN,
        ON_USE,
        
    }
    
    /**
     * auto generate fields which always same or similar
     */
    public ArknightsModCard(
            final String id,
            final String img,
            final int cost,
            final CardType type,
            final CardRarity rarity,
            final CardTarget target) {
        this(id, img, cost, type, ArknightsPlayer.Enums.ARKNIGHTS_CARD_COLOR, rarity, target);
    }
    
    /**
     * template-project recommend
     */
    public ArknightsModCard(final String id,
                               final String img,
                               final int cost,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
        super.isCostModified = false;
        super.isCostModifiedForTurn = false;
        super.isDamageModified = false;
        super.isBlockModified = false;
        super.isMagicNumberModified = false;
        
        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        this.spCount = 0;
        
    }
    
    protected void initBaseFields(BasicSetting basicSetting) {
        if (basicSetting.getDamage() != null) {
            this.baseDamage = basicSetting.getDamage();
        }
        if (basicSetting.getBlock() != null) {
            this.baseBlock = basicSetting.getBlock();
        }
        if (basicSetting.getMagicNumber() != null) {
            this.baseMagicNumber = basicSetting.getMagicNumber();
            this.magicNumber = this.baseMagicNumber;
        }
        
        for (int i = 0; i < ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE; i++) {
            if (basicSetting.getExtraMagicNumber(i) != null) {
                this.baseExtraMagicNumbers[i] = basicSetting.getExtraMagicNumber(i);
                this.extraMagicNumbers[i] =  this.baseExtraMagicNumbers[i];
            }
        }
    }



    protected void setUpgradeInfo(UpgradeSetting upgradeSetting) {
        this.upgradeSetting = upgradeSetting;
    }
    
    
    public void addSpCount(int amount) {
    	ArknightsMod.logger.info("{} addSpCount {} + {}", this.toIdString(), spCount, amount);
    	spCount += amount;
        if (isSpCountReachThreshold()) {
            spCount = spThreshold;
        }
    }

    
    public void setSpThreshold(Integer spThreshold) {
        this.spThreshold = spThreshold;
    }
    
    public Integer getSpThreshold() {
        return spThreshold;
    }
    
    public int getSpCount() {
        return spCount;
    }
    
    public boolean isSpCountReachThreshold() {
        return spThreshold != -1 && spCount >= spThreshold;
    }

    
    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        for (int i = 0; i < ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE; i++) {
            if (extraMagicNumberUpgradeds[i]) {
                extraMagicNumbers[i] = baseExtraMagicNumbers[i];
                extraMagicNumberModifieds[i] = true;
            }
        }
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (upgradeSetting.getPlusDamage() != null) {
                upgradeDamage(upgradeSetting.getPlusDamage());
            }
            if (upgradeSetting.getNewCost() != null) {
                upgradeBaseCost(upgradeSetting.getNewCost());
            }
            if (upgradeSetting.getPlusBlock() != null) {
                upgradeBlock(upgradeSetting.getPlusBlock());
            }
            if (upgradeSetting.getPlusMagicNumber() != null) {
                upgradeMagicNumber(upgradeSetting.getPlusMagicNumber());
            }
            
            for (int i = 0; i < ExtraVariable.EXTRA_MAGIC_NUMBER_SIZE; i++) {
                if (upgradeSetting.getPlusExtraMagicNumber(i) != null) {
                    upgradeExtraMagicNumber(i, upgradeSetting.getPlusExtraMagicNumber(i));
                }
            }
            
            if (cardStrings.UPGRADE_DESCRIPTION != null) {
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            }
            
            if (upgradeSetting.isUpgradeCardToPreview()) {
                this.cardsToPreview.update();
            }
            
            initializeDescription();
        }
    }
    
    
    
    
    public void applyPowersWithTempAddBaseDamage(int tempAddDamage) {
        int originBaseDamage = this.baseDamage;
        this.baseDamage += tempAddDamage;
        
        super.applyPowers();
        
        this.baseDamage = originBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }
    
    public void calculateCardDamageWithTempAddBaseDamage(AbstractMonster arg0, int tempAddDamage) {
        int originBaseDamage = this.baseDamage;
        this.baseDamage += tempAddDamage;
        
        super.calculateCardDamage(arg0);
        
        this.baseDamage = originBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    /**
     * 直接用途是修改base；
     * 因为base被修改，所以Upgraded=true；
     * 因为base被修改，所以value已过时，重置为base，等待重新计算其他加成。
     */
    protected void upgradeExtraMagicNumber(int index, int amount) {
        upgradeExtraMagicNumber(index, amount, true);
    }
    
    public int getExtraMagicNumber(int index) {
        return extraMagicNumbers[index];
    }
    
    protected void resetExtraMagicNumber(int index, int resetValue) {
        baseExtraMagicNumbers[index] = resetValue; 
        extraMagicNumbers[index] = baseExtraMagicNumbers[index];
        extraMagicNumberUpgradeds[index] = false;
    }
    
    protected void upgradeExtraMagicNumber(int index, int amount, boolean manualUpgraded) {
        baseExtraMagicNumbers[index] += amount; 
        extraMagicNumbers[index] = baseExtraMagicNumbers[index];
        extraMagicNumberUpgradeds[index] = manualUpgraded;
    }
    
    
    
    public void clearSpCount() {
        this.spCount = 0;
        ArknightsMod.logger.info("{} SpCount cleared", this.toIdString());
    }
    
    protected boolean needSetBorderOnGlow() {
        return false;
    }
    
    @Override
    public void triggerOnGlowCheck() {
        if (needSetBorderOnGlow()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
    
    
    
    public String toIdString() {
        return this.cardID + "[" + this.uuid + "]";
    }
    
    /**
     * 应把牌组中牌的必要的field传递给抽牌堆中的copy
     */
    @Override
    public AbstractCard makeCopy() {
        ArknightsModCard copy = (ArknightsModCard)super.makeCopy();
        copy.timesUpgraded = this.timesUpgraded;
        copy.updateNameWithPromotionLevel();
        return copy;
    }
    
    protected void updateNameWithPromotionLevel() {

            if (this.upgraded) {
                if (this.timesUpgraded == 1) {
                    this.name = cardStrings.NAME + "+";
                } else {
                    this.name = cardStrings.NAME + "+" + this.timesUpgraded;
                }
                
            } else {
                this.name = cardStrings.NAME;
            }

    }
    
    


    
    
    

}