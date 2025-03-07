package arknights.orbs;

import static arknights.ArknightsMod.makeOrbPath;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import arknights.ArknightsMod;
import arknights.util.LocalizationUtils;

/**
 * @author hundun
 * Created on 2020/11/25
 */
public class FreezingSupportDrone extends AbstractModOrb {
    public static final String ID = ArknightsMod.makeID(FreezingSupportDrone.class);
    public static final String IMG_PATH = makeOrbPath(FreezingSupportDrone.class.getSimpleName() + ".png");
   
    // Standard ID/Description
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ID);
    public static final String[] DESC = orbString.DESCRIPTION;



    // special const
    private static final int BASE_EVOKE_DAMAGE = 0; 
    private static final int BASE_PASSIVE_DAMAGE = 2;  
    
    
    public FreezingSupportDrone() {
        super(ID, BASE_PASSIVE_DAMAGE, BASE_EVOKE_DAMAGE, "", "", IMG_PATH);
        

        
        
        this.evokeAmountChargeSpeed = 5;
        updateDescription();
    }
    @Override
    public AbstractOrb makeCopy() {
        return new MeeBoo();
    }

    @Override
    public void onEvoke() {
        
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }


    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = LocalizationUtils.formatDescription(orbString.DESCRIPTION[0], this.passiveAmount, this.evokeAmountChargeSpeed, this.evokeAmount);
    }
    
    @Override
    public void onStartOfTurn() {
        chargeEvokeAmount();
        float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }
        addToBot(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.FROST), speedTime));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.passiveAmount, true));
    }
    @Override
    public void onOverload() {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.evokeAmount, true));
        addToBot(new SFXAction("TINGSHA")); 
    }

}
