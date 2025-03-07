package arknights.cards.operator;

import java.util.Arrays;

import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;

import arknights.ArknightsMod;
import arknights.cards.AmiyaStrike;
import arknights.cards.AmiyaChimera;
import arknights.cards.AmiyaSpiritBurst;
import arknights.cards.base.ArknightsModCard;
import arknights.cards.base.BaseDeployCard;

/**
 * @author hundun
 * Created on 2021/02/06
 */
public class AmiyaDeploy extends BaseDeployCard {

    public static final String ID = ArknightsMod.makeID(AmiyaDeploy.class);
    public static final String IMG = ArknightsMod.makeCardPngPath(ArknightsModCard.class);


    public AmiyaDeploy() { 
        super(ID, IMG);
        initGiveCardsSetting(Arrays.asList(new AmiyaStrike(), new AmiyaSpiritBurst(), new AmiyaChimera()));
        initStar(5);
    }
    
    @Override
    protected void updateCurrentGiveCards() {
        super.updateCurrentGiveCards();
        
        // 升变后
        if (this.timesUpgraded >= 4) {
            currentGiveCards.add(baseGiveCards.get(3).makeCopy());
            currentGiveCards.add(baseGiveCards.get(4).makeCopy());
        }
        if (this.timesUpgraded >= 5) {
            currentGiveCards.get(3).upgrade();
            currentGiveCards.get(4).upgrade();
        }
    }
}
