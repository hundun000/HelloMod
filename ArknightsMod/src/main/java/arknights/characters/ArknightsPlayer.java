package arknights.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import arknights.ArknightsMod;
import arknights.cards.*;
import arknights.cards.operator.FangDeploy;
import arknights.cards.operator.TexasDeploy;
import arknights.relics.BattleRecords;
import arknights.relics.HumanResource;
import arknights.relics.UrsusBreadRelic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static arknights.ArknightsMod.*;
import static arknights.characters.ArknightsPlayer.Enums.ARKNIGHTS_CARD_COLOR;

import java.util.ArrayList;
import java.util.Arrays;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in DefaultMod-character-Strings.json in the resources

public abstract class ArknightsPlayer extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(ArknightsMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass ARKNIGHTS_PLAYER_CLASS;
        @SpireEnum(name = "ARKNIGHTS_CARD_COLOR")
        public static AbstractCard.CardColor ARKNIGHTS_CARD_COLOR;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_3_STAR_CARD_COLOR")
        public static AbstractCard.CardColor ARKNIGHTS_OPERATOR_3_STAR_CARD_COLOR;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_4_STAR_CARD_COLOR")
        public static AbstractCard.CardColor ARKNIGHTS_OPERATOR_4_STAR_CARD_COLOR;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_5_STAR_CARD_COLOR")
        public static AbstractCard.CardColor ARKNIGHTS_OPERATOR_5_STAR_CARD_COLOR;
    }
    
    public static class LibraryTypeEnums {
        @SpireEnum(name = "ARKNIGHTS_CARD_COLOR")  
        public static CardLibrary.LibraryType ARKNIGHTS_CARD_LIBRARY_TYPE;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_3_STAR_CARD_COLOR")  
        public static CardLibrary.LibraryType ARKNIGHTS_OPERATOR_3_STAR_CARD_LIBRARY_TYPE;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_4_STAR_CARD_COLOR")  
        public static CardLibrary.LibraryType ARKNIGHTS_OPERATOR_4_STAR_CARD_LIBRARY_TYPE;
        @SpireEnum(name = "ARKNIGHTS_OPERATOR_5_STAR_CARD_COLOR")  
        public static CardLibrary.LibraryType ARKNIGHTS_OPERATOR_5_STAR_CARD_LIBRARY_TYPE;
    }
    
    public static final Color MOVING_CARDS_TRAIL_COLOR = ArknightsMod.DEFAULT_GRAY;

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 3;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("DefaultCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer1.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer2.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer3.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer4.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer5.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer6.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer1d.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer2d.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer3d.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer4d.png",
            ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public ArknightsPlayer(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/orb/vfx.png", null,
                new SpriterAnimation(
                        ArknightsMod.IMAGES_FOLDER + "/char/defaultCharacter/Spriter/theDefaultAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_DEFAULT_SHOULDER_2, // campfire pose
                THE_DEFAULT_SHOULDER_1, // another campfire pose
                THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                THE_DEFAULT_SKELETON_ATLAS,
                THE_DEFAULT_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    
    
    

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }
    
    

    

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return ARKNIGHTS_CARD_COLOR;
    }

    @Override
    public Color getCardTrailColor() {
        return MOVING_CARDS_TRAIL_COLOR;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new FangDeploy();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }


    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return arknights.ArknightsMod.DEFAULT_GRAY;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return arknights.ArknightsMod.DEFAULT_GRAY;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
