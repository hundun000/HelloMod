package arknights.cards;

import static arknights.DefaultMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;

import arknights.DefaultMod;
import arknights.cards.AbstractModCard.BasicSetting;
import arknights.cards.AbstractModCard.UpgradeSetting;
import arknights.characters.Doctor;

/**
 * @author hundun
 * Created on 2020/11/05
 */
public class FourStarVanguardDeploy extends AbstractModCard {

    public static final String ID = DefaultMod.makeID(FourStarVanguardDeploy.class);
    public static final String IMG = DefaultMod.makeCardPngPath(FourStarVanguardDeploy.class);

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; 
    private static final CardTarget TARGET = CardTarget.ENEMY;  
    private static final CardType TYPE = CardType.ATTACK;       
    
    private static final int COST = 1;  
    
    // special const
    private static final int GIVE_ENERGY_NUM = 1;  
    
    public FourStarVanguardDeploy() { 
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        setBasicInfo(new BasicSetting()
                .setDamage(7)
                .setMagicNumber(GIVE_ENERGY_NUM)
                );
        setUpgradeInfo(new UpgradeSetting()
                .setPlusDamage(3)
                );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(
                new ApplyPowerAction(
                    p,
                    p,
                    new EnergizedBluePower(p, this.magicNumber),
                    this.magicNumber
                )
            );
    }


}
